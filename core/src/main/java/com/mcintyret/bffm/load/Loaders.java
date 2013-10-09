package com.mcintyret.bffm.load;

import com.mcintyret.bffm.load.tescos.TescosFoodTypeLoader;
import com.mcintyret.bffm.load.usda.UsdaFoodTypeLoader;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Source;

import java.io.IOException;
import java.util.*;

/**
 * User: mcintyret2
 * Date: 08/10/2013
 */
public class Loaders {

    private static final Map<Source, FoodTypeManager> LOADERS = makeLoaderMap();

    private static Map<Source, FoodTypeManager> makeLoaderMap() {
        Map<Source, FoodTypeManager> temp = new EnumMap<>(Source.class);

        temp.put(Source.TESCOS, new JsonFileCachingFoodTypeReader(new TescosFoodTypeLoader(), "tescos.json"));
        temp.put(Source.USDA, new JsonFileCachingFoodTypeReader(new UsdaFoodTypeLoader(), "usda.json"));
        temp.put(Source.USER, new JsonFileCachingFoodTypeReader(NoOpFoodTypeLoader.INSTANCE, "user.json"));

        return Collections.unmodifiableMap(temp);
    }

    public static List<FoodType> loadFoodTypesForSource(Source source) throws IOException {
        return LOADERS.get(source).load();
    }

    private static boolean foodTypeAdded = true;

    private static final FoodTypeLoader ALL_FOOD_TYPES_LOADER = new FoodTypeLoader() {

        private final List<FoodType> allFoodTypes = new ArrayList<>(1000);

        @Override
        public List<FoodType> load() throws IOException {
            if (foodTypeAdded) {
                allFoodTypes.clear();
                for (Source source : Source.values()) {
                    allFoodTypes.addAll(loadFoodTypesForSource(source));
                }
                Collections.sort(allFoodTypes, FOOD_TYPE_COMPARATOR);
                foodTypeAdded = false;
            }
            return allFoodTypes;
        }
    };

    public static List<FoodType> loadAllFoodTypes() throws IOException {
        return ALL_FOOD_TYPES_LOADER.load();
    }

    public static void addFoodType(Source source, FoodType foodType) throws IOException {
        LOADERS.get(source).store(foodType);
        foodTypeAdded = true;
    }

    private static enum NoOpFoodTypeLoader implements FoodTypeLoader {
        INSTANCE;

        @Override
        public List<FoodType> load() throws IOException {
            return Collections.emptyList();
        }

    }

    private static final Comparator<FoodType> FOOD_TYPE_COMPARATOR = new Comparator<FoodType>() {
        @Override
        public int compare(FoodType o1, FoodType o2) {
            return o1.getFoodMetadata().getDescription().compareTo(o2.getFoodMetadata().getDescription());
        }
    };

    public static final Comparator<FoodType> getFoodTypeComparator() {
        return FOOD_TYPE_COMPARATOR;
    }
}
