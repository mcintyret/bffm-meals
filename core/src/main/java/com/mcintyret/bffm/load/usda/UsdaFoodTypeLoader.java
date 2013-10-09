package com.mcintyret.bffm.load.usda;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mcintyret.bffm.load.FoodTypeLoader;
import com.mcintyret.bffm.load.usda.raw.NutrientData;
import com.mcintyret.bffm.load.usda.raw.Parsers;
import com.mcintyret.bffm.types.FoodDescription;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 08/10/2013
 */
public class UsdaFoodTypeLoader implements FoodTypeLoader {

    @Override
    public List<FoodType> load() throws IOException {
        final Map<String, FoodDescription> foodDescriptions = Maps.uniqueIndex(Parsers.foodDescriptionParser().parseList(),
            new Function<FoodDescription, String>() {
                @Override
                public String apply(FoodDescription input) {
                    return input.getId();
                }
            });


        Multimap<FoodDescription, NutrientData> multimap = Multimaps.index(Parsers.nutrientDataParser().parseList(),
            new Function<NutrientData, FoodDescription>() {
                @Override
                public FoodDescription apply(NutrientData input) {
                    return foodDescriptions.get(input.getFoodId());
                }
            });

        List<FoodType> foodTypes = Lists.newArrayList();

        for (Map.Entry<FoodDescription, Collection<NutrientData>> entry : multimap.asMap().entrySet()) {
            foodTypes.add(makeFoodType(entry.getKey(), entry.getValue()));
        }

        return foodTypes;
    }

    private static FoodType makeFoodType(FoodDescription foodDescription, Iterable<NutrientData> nutrientData) {

        EnumMap<Nutrient, Float> nutrients = new EnumMap<>(Nutrient.class);
        for (NutrientData nd : nutrientData) {
            Nutrient nutrient = Nutrient.forId(nd.getId());
            if (nutrient != null) {
                nutrients.put(nutrient, nd.getAmountIn100g());
            }
        }
        return new FoodType(nutrients, foodDescription);
    }
}
