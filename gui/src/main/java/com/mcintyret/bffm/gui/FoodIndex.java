package com.mcintyret.bffm.gui;

import com.mcintyret.bffm.load.Loaders;
import com.mcintyret.bffm.types.FoodType;

import java.io.IOException;
import java.util.*;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class FoodIndex {

    private static final CaseInsensitiveMultiMap<FoodType> index = new CaseInsensitiveMultiMap<>();

    static {
        makeIndex();
    }

    private static void makeIndex() {
        try {
            for (FoodType foodType : Loaders.loadAllFoodTypes()) {
                addToIndex(foodType);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load FoodTypes to generate index");
        }
    }

    public static void addToIndex(FoodType foodType) {
        String name = foodType.getFoodMetadata().getDescription();
        name = name.replaceAll("[,\\.\\-'\"]", " ");

        for (String word : name.split("\\s+")) {
            index.put(word, foodType);
        }
    }

    public static Collection<FoodType> query(String query) {
        return query(query.split("\\s+"));
    }

    public static Collection<FoodType> query(String[] words) {
        Set<FoodType> set = new HashSet<>();
        boolean first = true;
        for (String word : words) {
            if (first) {
                set.addAll(index.get(word));
            } else {
                set.retainAll(index.get(word));
            }
            if (set.isEmpty()) {
                return Collections.emptyList();
            }
            first = false;
        }

        List<FoodType> list = new ArrayList<>(set);
        Collections.sort(list, Loaders.getFoodTypeComparator());
        return list;
    }

    private static class CaseInsensitiveMultiMap<V> {

        private final Map<String, Set<V>> map = new HashMap<>();

        public void put(String key, V value) {
            key = toKey(key);
            Set<V> set = map.get(key);
            if (set == null) {
                set = new HashSet<>();
                map.put(key, set);
            }
            set.add(value);
        }

        private String toKey(String s) {
            return s.trim().toLowerCase();
        }

        public Set<V> get(String key) {
            Set<V> set = map.get(toKey(key));
            return set == null ? Collections.<V>emptySet() : Collections.unmodifiableSet(set);
        }

    }

}
