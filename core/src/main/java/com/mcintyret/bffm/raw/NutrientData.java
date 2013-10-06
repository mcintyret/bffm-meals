package com.mcintyret.bffm.raw;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class NutrientData {

    private final String foodId;

    private final int id;

    private final float amountIn100g;

    public NutrientData(Object[] rawData) {
        this.foodId = (String) rawData[0];
        this.id = Integer.parseInt((String) rawData[1]);
        this.amountIn100g = (Float) rawData[2];
    }

    public String getFoodId() {
        return foodId;
    }

    public int getId() {
        return id;
    }

    public float getAmountIn100g() {
        return amountIn100g;
    }
}
