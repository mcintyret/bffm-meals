package com.mcintyret.bffm.gather.tescos;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcintyret.bffm.raw.FoodDescription;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: tommcintyre
 * Date: 10/7/13
 */
public class TescosFoodGatherer {

    private static final Pattern KCAL_PATTERN = Pattern.compile("(\\d+)kcal", Pattern.CASE_INSENSITIVE);
    private static final Pattern KJ_PATTERN = Pattern.compile("(\\d+)kj", Pattern.CASE_INSENSITIVE);

    private static final float CALS_TO_JOULES = 4.2F;

    private static final Set<String> ZERO_EQUIVALENTS = ImmutableSet.of("trace", "Nil", "", "trace)", "-", "Trace", "nil");

    private static Document getUrl(String url) throws IOException {
        return Jsoup.connect(url).timeout(15000).get();
    }

    private static final Map<String, Nutrient> NUTRIENT_MAP = ImmutableMap.<String, Nutrient>builder()
            .put("Energy Kj", Nutrient.ENERC_KJ)
            .put("Energy Kcal", Nutrient.ENERC_KCAL)
            .put("Protein", Nutrient.PROCNT)
            .put("Carbohydrate", Nutrient.CHOCDF)
            .put("Carbohydrates", Nutrient.CHOCDF)
            .put("Sugars", Nutrient.SUGAR)
            .put("of which sugars", Nutrient.SUGAR)
            .put("(of which sugars", Nutrient.SUGAR)
            .put("(of which sugars)", Nutrient.SUGAR)
            .put("Fat", Nutrient.FAT)
            .put("Saturates", Nutrient.FASAT)
            .put("of which saturates", Nutrient.FASAT)
            .put("(of which saturates", Nutrient.FASAT)
            .put("Fibre", Nutrient.FIBTG)
            .put("Starch", Nutrient.STARCH)
            .put("Sodium*", Nutrient.NA)
            .put("Sodium", Nutrient.NA)
            .put("Magnesium", Nutrient.MG)
            .put("Potassium", Nutrient.K)
            .put("Iron", Nutrient.FE)
            .put("Zinc", Nutrient.ZN)
            .put("Vitamin A", Nutrient.RETOL)
            .put("Thiamin (Vitamin B1)", Nutrient.THIA)
            .put("Riboflavin (Vitamin B2)", Nutrient.RIBF)
            .put("Vitamin B6", Nutrient.VITB6A)
            .put("Vitamin C", Nutrient.VITC)
            .put("Vitamin D", Nutrient.VITD)
            .put("Vitamin E", Nutrient.TOCPHA)
            .put("Folic Acid", Nutrient.FOLAC)
            .put("Mono Unsaturates", Nutrient.FAMS)
            .put("Mono-Unsaturates", Nutrient.FAMS)
            .put("mono-unsaturates", Nutrient.FAMS)
            .put("monounsaturates", Nutrient.FAMS)
            .put("of which monounsaturates", Nutrient.FAMS)
            .put("Polyunsaturates", Nutrient.FAPU)
            .put("polyunsaturates", Nutrient.FAPU)
            .put("of which polyunsaturates", Nutrient.FAPU)
            .put("Pantothenic Acid", Nutrient.PANTAC)
            .put("Trans Fatty Acids", Nutrient.FATRN)
            .put("Cholesterol", Nutrient.CHOLE)
            .build();

    private static final Set<String> IGNORED_NUTRIENTS = ImmutableSet.of(
            "*Salt Equivalent",
            "Salt",
            "Salt Equivalent",
            "Salt equivalent",
            "Equivalent as salt",
            "",
            "Vitamins (total)");

    private static final Set<String> DEPARTMENTS = ImmutableSet.of(
            "Fresh Food", "Food Cupboard", "Bakery", "Frozen Food"
    );

    public List<FoodType> doIt() throws IOException {
        Document groceries = getUrl("http://www.tesco.com/groceries/");

        List<FoodType> allFood = new ArrayList<>();
        for (Element departmentLink : groceries.getElementsByClass("Groceries").first().getElementsByTag("a")) {
            String departmentName = departmentLink.text();
            if (DEPARTMENTS.contains(departmentName)) {
                allFood.addAll(getDepartmentFoods(departmentName, departmentLink.attr("href")));
            }
        }
        return allFood;
    }

    private Collection<FoodType> getDepartmentFoods(String departmentName, String url) throws IOException {
        List<FoodType> departmentFoods = new ArrayList<>();
        Document department = getUrl(url);

        for (Element subDepartmentLink : department.getElementById("superDeptItems").getElementsByTag("a")) {
            departmentFoods.addAll(getSubDepartmentFoods(subDepartmentLink.text(), subDepartmentLink.attr("href")));
        }
        return departmentFoods;
    }

    private Collection<FoodType> getSubDepartmentFoods(String subDepartmentName, String url) throws IOException {
        List<FoodType> foods = new ArrayList<>();

        do {
            Document subDepartment = getUrl(url);

            for (Element food : subDepartment.getElementsByClass("inBasketInfoContainer")) {
                Element link = food.getElementsByTag("a").first();

                foods.add(getFood(link.attr("href")));
            }

            Element next = subDepartment.getElementsByClass("next").first().getElementsByTag("a").first();

            url = next == null ? null : next.attr("href");

        } while (url != null);
        return foods;
    }

    private FoodType getFood(String url) throws IOException {
        Document foodPage = getUrl("http://www.tesco.com" + url);

        String foodName = foodPage.getElementsByTag("h1").first().text();
        EnumMap<Nutrient, Float> nutrients = new EnumMap<>(Nutrient.class);

        Element nutrientsTable = foodPage.getElementById("detailsBox-1").getElementsByTag("table").first();

        if (nutrientsTable != null) {
            for (Element row : nutrientsTable.getElementsByTag("tbody").first().getElementsByTag("tr")) {
                Iterator<Element> cells = row.children().iterator();

                String type = cells.next().text();
                String val = cells.next().text();
                if ("Energy".equals(type)) {
                    parseEnergies(nutrients, val);
                } else {
                    Nutrient nutrient = NUTRIENT_MAP.get(type);
                    if (nutrient != null) {
                        float amount = 0;
                        if (!ZERO_EQUIVALENTS.contains(val)) {
                            amount = parseAmount(val);
                        }
                        if (nutrients.put(nutrient, amount) != null) {
                            System.out.println("DUPLICATE VALUES for nutrient '" + nutrient + "' for food: " + foodName);
                        }
                    } else if (!IGNORED_NUTRIENTS.contains(type)) {
                        System.err.println("UNRECOGNISED NUTRIENT TYPE: '" + type + "' for food: " + foodName);
                    }
                }
            }
        }

        FoodType foodType = new FoodType(nutrients, new FoodDescription(foodName));

        printFoodType(foodType);

        return foodType;
    }

    private float parseAmount(String amount) {
        try {
            int start = 0;
            for (int i = 0; i < amount.length(); i++) {
                char c = amount.charAt(i);
                if (c == '<') {
                    if (i != 0) {
                        throw new AssertionError("Found '<' in a weird place: " + amount);
                    }
                    start = 1;
                } else if (c == '.' || (c >= '0' && c <= '9')) {
                    // we're OK
                } else {
                    return Float.parseFloat(amount.substring(start, i));
                }
            }
            return Float.parseFloat(amount);
        } catch (Exception e) {
            System.err.println("UNRECOGNISED AMOUNT: '" + amount + "'");
            return 0;
        }
    }

    private void parseEnergies(EnumMap<Nutrient, Float> nutrients, String val) {
        float kj = forPattern(val, KJ_PATTERN);
        float kcal = forPattern(val, KCAL_PATTERN);

        if (kj == -1 && kcal == -1) {
            System.err.println("UNMATCHED ENERGY STRING: '" + val + "'");
            return;
        } else if (kj == -1) {
            // just kcal
            kj = CALS_TO_JOULES * kcal;
        } else if (kcal == -1) {
            // just kj
            kcal = kj / CALS_TO_JOULES;
        }

        nutrients.put(Nutrient.ENERC_KCAL, kcal);
        nutrients.put(Nutrient.ENERC_KJ, kj);
    }

    private float forPattern(String val, Pattern pattern) {
        Matcher matcher = pattern.matcher(val);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group(1));
        } else {
            return -1;
        }
    }

    private void printFoodType(FoodType foodType) {
        System.out.println(foodType.getFoodMetadata().getDescription());
//        System.out.println(foodType.getNutrientsPer100g().toString().replaceAll(", ", ",\n"));
//        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/core/src/main/resources/tescosFoodData.json");
        file.createNewFile();

        List<FoodType> tescoFoods = new TescosFoodGatherer().doIt();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(tescoFoods, writer);
        }
    }

    private String process(String input) {
        input = input.trim();
        input = input.replaceAll("\\(\\)-", "");
        input = input.toLowerCase();
        if (input.startsWith("of which ")) {
            input = input.substring("of which ".length());
        }
        return input;
    }
}
