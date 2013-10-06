package com.mcintyret.bffm.raw;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public class Parsers {

    public static GenericParser<FoodDescription> foodDescriptionParser() {
        return new GenericParser<FoodDescription>("/data/FOOD_DES.txt") {
            @Override
            protected FoodDescription parseItem(Object[] rawData) {
                return new FoodDescription(rawData);
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

}
