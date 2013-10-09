package com.mcintyret.bffm.load;

import com.mcintyret.bffm.types.FoodType;

import java.io.IOException;

/**
 * User: mcintyret2
 * Date: 09/10/2013
 */
public interface FoodTypeManager extends FoodTypeLoader {

    void store(FoodType foodType) throws IOException;
}
