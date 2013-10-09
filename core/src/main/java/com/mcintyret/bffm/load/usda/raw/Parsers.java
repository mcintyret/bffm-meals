package com.mcintyret.bffm.load.usda.raw;

import com.mcintyret.bffm.types.FoodDescription;
import com.mcintyret.bffm.types.Source;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class Parsers {

    public static GenericParser<FoodDescription> foodDescriptionParser() {
        return new GenericParser<FoodDescription>("/data/FOOD_DES.txt") {
            @Override
            protected FoodDescription parseItem(Object[] rawData) {
                String id = (String) rawData[0];
                String description = (String) rawData[2];

                float proteinCalorieFactor = orDefault((Float) rawData[11], FoodDescription.DEFAULT_PROTEIN_CALORIE_FACTOR);
                float fatCalorieFactor = orDefault((Float) rawData[12], FoodDescription.DEFAULT_FAT_CALORIE_FACTOR);
                float carbCalorieFactor = orDefault((Float) rawData[13], FoodDescription.DEFAULT_CARB_CALORIE_FACTOR);

                return new FoodDescription(description, Source.USDA, id, proteinCalorieFactor, fatCalorieFactor, carbCalorieFactor);
            }
        };
    }

    public static GenericParser<NutrientData> nutrientDataParser() {
        return new GenericParser<NutrientData>("/data/NUT_DATA.txt") {
            @Override
            protected NutrientData parseItem(Object[] rawData) {
                return new NutrientData(rawData);
            }
        };
    }

    private static float orDefault(float in, float def) {
        return in == -1 ? def : in;
    }
}
