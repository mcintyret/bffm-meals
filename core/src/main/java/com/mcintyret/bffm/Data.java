package com.mcintyret.bffm;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.mcintyret.bffm.raw.FoodDescription;
import com.mcintyret.bffm.raw.NutrientData;
import com.mcintyret.bffm.raw.Parsers;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class Data {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(
            new TypeToken<EnumMap<Nutrient, Float>>() {
            }.getType(),
            new EnumMapInstanceCreator<Nutrient, Float>(Nutrient.class))
        .create();


    private static class EnumMapInstanceCreator<K extends Enum<K>, V> implements InstanceCreator<EnumMap<K, V>> {

        private final Class<K> enumClazz;

        public EnumMapInstanceCreator(final Class<K> enumClazz) {
            super();
            this.enumClazz = enumClazz;
        }

        @Override
        public EnumMap<K, V> createInstance(final Type type) {
            return new EnumMap<>(enumClazz);
        }
    }

    private static final File USDA = new File(System.getProperty("user.dir") + "/core/src/main/resources/usdaFoodData.json");

    private static final File MINE = new File(System.getProperty("user.dir") + "/core/src/main/resources/myFoodData.json");

    private static final Comparator<FoodType> FOOD_TYPE_COMPARATOR = new Comparator<FoodType>() {
        @Override
        public int compare(FoodType o1, FoodType o2) {
            return o1.getFoodMetadata().getDescription().compareTo(o2.getFoodMetadata().getDescription());
        }
    };

    private static final List<FoodType> USDA_FOOD_TYPES = loadUsdaFoodTypes();

    private static final List<FoodType> MY_FOOD_TYPES = loadMyFoodTypes();

    private static final List<FoodType> ALL_FOOD_TYPES = combineAllFoodTypes();

    private static List<FoodType> combineAllFoodTypes() {
        List<FoodType> allFoodTypes = new ArrayList<>(USDA_FOOD_TYPES.size() + MY_FOOD_TYPES.size());
        allFoodTypes.addAll(USDA_FOOD_TYPES);
        allFoodTypes.addAll(MY_FOOD_TYPES);
        Collections.sort(allFoodTypes, FOOD_TYPE_COMPARATOR);
        return allFoodTypes;
    }

    private static final Vector<FoodType> ALL_FOOD_TYPES_VECTOR = new Vector<>(ALL_FOOD_TYPES);

    public static List<FoodType> foodTypes() {
        return Collections.unmodifiableList(ALL_FOOD_TYPES);
    }

    public static Vector<FoodType> foodTypeVector() {
        return ALL_FOOD_TYPES_VECTOR;
    }

    private static List<FoodType> loadFoodTypesFromJson(File file) {
        try (Reader reader = new BufferedReader(new FileReader(file))) {
            return GSON.fromJson(reader, new TypeToken<List<FoodType>>() {
            }.getType());
        } catch (IOException e) {
            return dealWithFailure(e);
        }
    }

    private static <V> V dealWithFailure(Exception e) {
        e.printStackTrace();
        System.exit(234);
        throw new AssertionError("Cannot happen");
    }

    private static List<FoodType> loadMyFoodTypes() {
        if (MINE.exists()) {
            return loadFoodTypesFromJson(MINE);
        } else {
            try {
                MINE.createNewFile();
                return Collections.emptyList();
            } catch (IOException e) {
                return dealWithFailure(e);
            }
        }
    }

    private static List<FoodType> loadUsdaFoodTypes() {
        try {
            if (USDA.exists()) {
                return loadFoodTypesFromJson(USDA);
            } else {
                USDA.createNewFile();

                final Map<String, FoodDescription> foodDescriptions = Maps.uniqueIndex(Parsers.foodDescriptionParser().parseList(),
                    new Function<FoodDescription, String>() {
                        @Override
                        public String apply(FoodDescription input) {
                            return input.getId();
                        }
                    });


                Multimap<FoodDescription, NutrientData> multimap = Multimaps.index(Parsers.nutrientDataParser().parseList(),
                    new Function<NutrientData, FoodDescription>() {
                        @Override
                        public FoodDescription apply(NutrientData input) {
                            return foodDescriptions.get(input.getFoodId());
                        }
                    });

                List<FoodType> foodTypes = Lists.newArrayList();
                for (Map.Entry<FoodDescription, Collection<NutrientData>> entry : multimap.asMap().entrySet()) {
                    foodTypes.add(new FoodType(entry.getKey(), entry.getValue()));
                }

                Collections.sort(foodTypes, FOOD_TYPE_COMPARATOR);

                try (Writer writer = new BufferedWriter(new FileWriter(USDA))) {
                    GSON.toJson(foodTypes, writer);
                }

                return foodTypes;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(234);
            throw new AssertionError("Cannot happen");
        }
    }

    public static void addFoodType(FoodType foodType) {
        ALL_FOOD_TYPES.add(foodType);
        Collections.sort(ALL_FOOD_TYPES, FOOD_TYPE_COMPARATOR);

        ALL_FOOD_TYPES_VECTOR.clear();
        ALL_FOOD_TYPES_VECTOR.addAll(ALL_FOOD_TYPES);

        MY_FOOD_TYPES.add(foodType);
        Collections.sort(MY_FOOD_TYPES, FOOD_TYPE_COMPARATOR);


        try (Writer writer = new BufferedWriter(new FileWriter(MINE))) {
            GSON.toJson(MY_FOOD_TYPES, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Comparator<FoodType> getFoodTypeComparator() {
        return FOOD_TYPE_COMPARATOR;
    }
}
