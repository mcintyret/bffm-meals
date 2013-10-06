package com.mcintyret.bffm.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class CaloriesTarget {

    private int totalCalories;

    private int fatCaloriesPercent;

    private int fatCalories;

    private int proteinCaloriesPercent;

    private int proteinCalories;

    private int carbCaloriesPercent;

    private int carbCalories;

    private final List<CaloriesTargetListener> listeners = new ArrayList<>();

    public CaloriesTarget(int fatCaloriesPercent, int proteinCaloriesPercent, int carbCaloriesPercent) {
        this.fatCaloriesPercent = fatCaloriesPercent;
        this.proteinCaloriesPercent = proteinCaloriesPercent;
        this.carbCaloriesPercent = carbCaloriesPercent;
    }
    
    public void setFatCaloriesPercent(int fatCaloriesPercent) {
        this.fatCaloriesPercent = fatCaloriesPercent;
        this.fatCalories = (int) (fatCaloriesPercent * (float) totalCalories / 100F);
        notifyListeners();
    }
    
    public void setCarbCaloriesPercent(int carbCaloriesPercent) {
        this.carbCaloriesPercent = carbCaloriesPercent;
        this.carbCalories = (int) (carbCaloriesPercent * (float) totalCalories / 100F);
        notifyListeners();
    }
    
    public void setProteinCaloriesPercent(int proteinCaloriesPercent) {
        this.proteinCaloriesPercent = proteinCaloriesPercent;
        this.proteinCalories = (int) (proteinCaloriesPercent * (float) totalCalories / 100F);
        notifyListeners();
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
        this.fatCalories = (int) (fatCaloriesPercent * (float) totalCalories / 100F);
        this.carbCalories = (int) (carbCaloriesPercent * (float) totalCalories / 100F);
        this.proteinCalories = (int) (proteinCaloriesPercent * (float) totalCalories / 100F);
        notifyListeners();
    }

    private void notifyListeners() {
        boolean validPercentages = fatCaloriesPercent + carbCaloriesPercent + proteinCaloriesPercent == 100;
        for (CaloriesTargetListener listener : listeners) {
            listener.onChange(this, validPercentages);
        }
    }

    public void addListener(CaloriesTargetListener listener) {
        listeners.add(listener);
    }

    public int getTotalCaloriesPercent() {
        return 100;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public int getFatCaloriesPercent() {
        return fatCaloriesPercent;
    }

    public int getFatCalories() {
        return fatCalories;
    }

    public int getProteinCaloriesPercent() {
        return proteinCaloriesPercent;
    }

    public int getProteinCalories() {
        return proteinCalories;
    }

    public int getCarbCaloriesPercent() {
        return carbCaloriesPercent;
    }

    public int getCarbCalories() {
        return carbCalories;
    }

    public static interface CaloriesTargetListener {

        void onChange(CaloriesTarget target, boolean validPercentages);

    }

}
