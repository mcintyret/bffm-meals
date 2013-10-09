package com.mcintyret.bffm.types;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class FoodType {

    private EnumMap<Nutrient, Float> nutrients;

    private FoodDescription foodDescription;

    public FoodType(EnumMap<Nutrient, Float> nutrients, FoodDescription foodDescription) {
        this.nutrients = nutrients;
        this.foodDescription = foodDescription;
    }

    public FoodType() {

    }

    public Map<Nutrient, Float> getNutrientsPer100g() {
        return Collections.unmodifiableMap(nutrients);
    }

    public FoodDescription getFoodMetadata() {
        return foodDescription;
    }

    public Float getAmountOfNutrient(Nutrient nutrient, float grams) {
        return nutrients.containsKey(nutrient) ? nutrients.get(nutrient) * grams / 100 : -1;
    }

    public float totalCalories(float grams) {
        return getAmountOfNutrient(Nutrient.ENERC_KCAL, grams);
    }

    public float caloriesFromFat(float grams) {
        return getAmountOfNutrient(Nutrient.FAT, grams) * getFoodMetadata().getFatCalorieFactor();
    }

    public float caloriesFromProtein(float grams) {
        return getAmountOfNutrient(Nutrient.PROCNT, grams) * getFoodMetadata().getProteinCalorieFactor();
    }

    public float caloriesFromCarbs(float grams) {
        return getAmountOfNutrient(Nutrient.CHOCDF, grams) * getFoodMetadata().getCarbCalorieFactor();
    }

    @Override
    public String toString() {
        return getFoodMetadata().getDescription();
    }

}
