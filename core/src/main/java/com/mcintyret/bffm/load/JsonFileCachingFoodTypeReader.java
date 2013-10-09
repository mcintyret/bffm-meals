package com.mcintyret.bffm.load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

/**
 * User: mcintyret2
 * Date: 08/10/2013
 */
class JsonFileCachingFoodTypeReader implements FoodTypeManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(
            new TypeToken<EnumMap<Nutrient, Float>>() {
            }.getType(),
            new EnumMapInstanceCreator<Nutrient, Float>(Nutrient.class))
        .create();


    private final FoodTypeLoader delegate;

    private final File file;

    private List<FoodType> foodTypes;

    public JsonFileCachingFoodTypeReader(FoodTypeLoader delegate, String filename) {
        this.delegate = delegate;
        this.file = new File(System.getProperty("user.dir") + "/core/src/main/resources/" + filename);
    }


    @Override
    public List<FoodType> load() throws IOException {
        if (foodTypes == null) {
            if (file.exists()) {
                try (Reader reader = new BufferedReader(new FileReader(file))) {
                    foodTypes = GSON.fromJson(reader, new TypeToken<List<FoodType>>() {
                    }.getType());
                }
            } else {
                foodTypes = delegate.load();
                Collections.sort(foodTypes, Loaders.getFoodTypeComparator());

                file.createNewFile();
                sortAndWriteFoodTypes();
            }
        }
        return foodTypes; // should always be sorted at this point
    }

    @Override
    public void store(FoodType foodType) throws IOException {
        if (foodTypes == null) {
            load();
            if (foodTypes != null) {
                foodTypes.add(foodType);
                sortAndWriteFoodTypes();
            } else {
                System.out.println("WARN: couldn't load foodTypes");
            }
        }
    }

    private void sortAndWriteFoodTypes() throws IOException {
        Collections.sort(foodTypes, Loaders.getFoodTypeComparator());
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            GSON.toJson(foodTypes, writer);
        }
    }


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
}
