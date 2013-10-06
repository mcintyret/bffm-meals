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
import java.lang.reflect.ParameterizedType;
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
            return new EnumMap<K, V>(enumClazz);
        }
    }

    private static final Type TYPE = new ParameterizedType() {
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{FoodType.class};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };


    private static final File FILE = new File(System.getProperty("user.dir") + "/core/src/main/resources/myFoodData.json");

    private static final Comparator<FoodType> FOOD_TYPE_COMPARATOR = new Comparator<FoodType>() {
        @Override
        public int compare(FoodType o1, FoodType o2) {
            return o1.getFoodMetadata().getDescription().compareTo(o2.getFoodMetadata().getDescription());
        }
    };

    private static final List<FoodType> FOOD_TYPES = loadFoodTypes();

    private static final Vector<FoodType> FOOD_TYPE_VECTOR = new Vector<>(FOOD_TYPES);

    public static List<FoodType> foodTypes() {
        return Collections.unmodifiableList(FOOD_TYPES);
    }

    public static Vector<FoodType> foodTypeVector() {
        return FOOD_TYPE_VECTOR;
    }

    private static List<FoodType> loadFoodTypes() {
        try {
            System.out.println(FILE);
            if (FILE.exists()) {
                try (Reader reader = new BufferedReader(new FileReader(FILE))) {
                    return GSON.fromJson(reader, new TypeToken<List<FoodType>>() {
                    }.getType());
                }
            } else {
                FILE.createNewFile();

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

                return foodTypes;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(234);
            throw new AssertionError("Cannot happen");
        }
    }

    public static void addFoodType(FoodType foodType) {
        FOOD_TYPES.add(foodType);
        Collections.sort(FOOD_TYPES, FOOD_TYPE_COMPARATOR);

        FOOD_TYPE_VECTOR.clear();
        FOOD_TYPE_VECTOR.addAll(FOOD_TYPES);

        try (Writer writer = new BufferedWriter(new FileWriter(FILE))) {
            GSON.toJson(FOOD_TYPES, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Comparator<FoodType> getFoodTypeComparator() {
        return FOOD_TYPE_COMPARATOR;
    }
}
