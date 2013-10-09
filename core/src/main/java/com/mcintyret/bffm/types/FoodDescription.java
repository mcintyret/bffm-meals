package com.mcintyret.bffm.types;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class FoodDescription {

    public static final float DEFAULT_PROTEIN_CALORIE_FACTOR = 4.0F;

    public static final float DEFAULT_FAT_CALORIE_FACTOR = 9.0F;

    public static final float DEFAULT_CARB_CALORIE_FACTOR = 4.0F;

    private String id;

    private String description;

    private Source source;

    private float proteinCalorieFactor = DEFAULT_PROTEIN_CALORIE_FACTOR;

    private float fatCalorieFactor = DEFAULT_FAT_CALORIE_FACTOR;

    private float carbCalorieFactor = DEFAULT_CARB_CALORIE_FACTOR;

    public FoodDescription(String description, Source source, String id, float proteinCalorieFactor, float fatCalorieFactor, float carbCalorieFactor) {
        this.description = description;
        this.source = source;
        this.id = id;

        this.proteinCalorieFactor = proteinCalorieFactor;
        this.fatCalorieFactor = fatCalorieFactor;
        this.carbCalorieFactor = carbCalorieFactor;
    }

    public FoodDescription(String description, Source source, String id) {
        this.description = description;
        this.source = source;
        this.id = id;
    }

    public FoodDescription() {

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

    public Source getSource() {
        return source;
    }
}
