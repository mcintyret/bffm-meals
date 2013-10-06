package com.mcintyret.bffm.gui;

import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;

import java.util.*;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class AdjustableMeal {

    private final Map<FoodType, Integer> ingredients = new HashMap<>();

    private final List<AdjustableMealListener> listeners = new ArrayList<>();

    public void adjustIngredientAmount(FoodType foodType, int grams) {
        Integer prev = ingredients.put(foodType, grams);
        if (!Objects.equals(prev, grams)) {
            for (AdjustableMealListener listener : listeners) {
                if (prev == null) {
                    listener.onIngredientAdded(foodType, grams);
                } else {
                    listener.onIngredientChanged(foodType, prev, grams);
                }
            }
        }
    }

    public void removeIngredient(FoodType foodType) {
        Integer val = ingredients.remove(foodType);
        if (val != null) {
            for (AdjustableMealListener listener : listeners) {
                listener.onIngredientRemoved(foodType, val);
            }
        }
    }

    public float getAmountOfNutrient(Nutrient nutrient) {
        float total = 0;
        for (Map.Entry<FoodType, Integer> entry : ingredients.entrySet()) {
            total += entry.getKey().getAmountOfNutrient(nutrient, entry.getValue());
        }
        return total;
    }

    public float totalCalories() {
        return getAmountOfNutrient(Nutrient.ENERC_KCAL);
    }

    public float caloriesFromFat() {
        float total = 0;
        for (Map.Entry<FoodType, Integer> entry : ingredients.entrySet()) {
            total += entry.getKey().caloriesFromFat(entry.getValue());
        }
        return total;
    }

    public float caloriesFromProtein() {
        float total = 0;
        for (Map.Entry<FoodType, Integer> entry : ingredients.entrySet()) {
            total += entry.getKey().caloriesFromProtein(entry.getValue());
        }
        return total;
    }

    public float caloriesFromCarbs() {
        float total = 0;
        for (Map.Entry<FoodType, Integer> entry : ingredients.entrySet()) {
            total += entry.getKey().caloriesFromCarbs(entry.getValue());
        }
        return total;
    }

    public void addListener(AdjustableMealListener listener) {
        listeners.add(listener);
    }

    public static interface AdjustableMealListener {

        void onIngredientAdded(FoodType newFoodType, int amount);

        void onIngredientChanged(FoodType foodType, int oldValue, int newValue);

        void onIngredientRemoved(FoodType foodType, int valueAtRemoval);

    }

}
