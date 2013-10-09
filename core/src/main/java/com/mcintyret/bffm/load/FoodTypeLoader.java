package com.mcintyret.bffm.load;

import com.mcintyret.bffm.types.FoodType;

import java.io.IOException;
import java.util.List;

/**
 * User: mcintyret2
 * Date: 09/10/2013
 */
public interface FoodTypeLoader {

    List<FoodType> load() throws IOException;
}
