package com.mcintyret.bffm.raw;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class FoodDescription {

    private static final float DEFAULT_PROTEIN_CALORIE_FACTOR = 4.0F;

    private static final float DEFAULT_FAT_CALORIE_FACTOR = 9.0F;

    private static final float DEFAULT_CARB_CALORIE_FACTOR = 4.0F;

    private String id;

    private String description;

    private float proteinCalorieFactor = DEFAULT_PROTEIN_CALORIE_FACTOR;

    private float fatCalorieFactor = DEFAULT_FAT_CALORIE_FACTOR;

    private float carbCalorieFactor = DEFAULT_CARB_CALORIE_FACTOR;

    public FoodDescription(Object[] rawData) {
        this.id = (String) rawData[0];
        this.description = (String) rawData[2];
        this.proteinCalorieFactor = orDefault((Float) rawData[11], DEFAULT_PROTEIN_CALORIE_FACTOR);
        this.fatCalorieFactor = orDefault((Float) rawData[12], DEFAULT_FAT_CALORIE_FACTOR);
        this.carbCalorieFactor = orDefault((Float) rawData[13], DEFAULT_CARB_CALORIE_FACTOR);
    }

    public FoodDescription(String description) {
        this.description = description;
        this.id = "NO ID";
    }

    public FoodDescription() {

    }

    private float orDefault(float in, float def) {
        return in == -1 ? def : in;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getProteinCalorieFactor() {
        return proteinCalorieFactor;
    }

    public float getFatCalorieFactor() {
        return fatCalorieFactor;
    }

    public float getCarbCalorieFactor() {
        return carbCalorieFactor;
    }
}
