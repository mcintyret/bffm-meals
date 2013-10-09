package com.mcintyret.bffm.types;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class FoodPortion {

    private final float grams;

    private final FoodType foodType;

    public FoodPortion(float grams, FoodType foodType) {
        this.grams = grams;
        this.foodType = foodType;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public float getGrams() {
        return grams;
    }

    public Float getAmountOfNutrient(Nutrient nutrient) {
        return foodType.getAmountOfNutrient(nutrient, grams);
    }

    public float totalCalories() {
        return foodType.totalCalories(grams);
    }

    public float caloriesFromFat() {
        return foodType.caloriesFromFat(grams);
    }

    public float caloriesFromProtein() {
        return foodType.caloriesFromProtein(grams);
    }

    public float caloriesFromCarbs() {
        return foodType.caloriesFromCarbs(grams);
    }
}
