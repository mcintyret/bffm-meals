package com.mcintyret.bffm.gui;

import com.mcintyret.bffm.types.FoodType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.swing.Box.createHorizontalGlue;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class NewMealPanel extends JPanel {

    private static final int MAX_WIDTH = 3000;

    private final AdjustableMeal meal = new AdjustableMeal();

    private final CaloriesTarget caloriesTarget = new CaloriesTarget(20, 40, 40);

    public NewMealPanel() throws IOException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new MealControlPanel());
        add(new NewIngredientPanel());
        add(Box.createVerticalGlue());
        add(new AllIngredientsPanel());
        add(new MealInformationPanel());
    }

    private static final class MealControlPanel extends JPanel {
        private final JTextField mealName = new JTextField();

        private final JButton save = new JButton("Save");

        public MealControlPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(new JLabel("Meal Name: "));
            add(mealName);
            add(save);
            setMaximumSize(new Dimension(MAX_WIDTH, 50));
        }
    }

    private class NewIngredientPanel extends JPanel {

        private final JComboBox<FoodType> ingredients;

        private final JTextField amount = new JTextField();

        public NewIngredientPanel() throws IOException {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(new JLabel("New ingredient: "));
            ingredients = new FoodPicker().getCombo();
            add(ingredients);
            add(new JLabel("Initial amount: "));
            add(amount);

            JButton add = new JButton("Add");
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int amount = Utils.parseInt(NewIngredientPanel.this.amount);
                    FoodType foodType = (FoodType) ingredients.getSelectedItem();
                    meal.adjustIngredientAmount(foodType, amount);
                }
            });
            add(add);
            setMaximumSize(new Dimension(MAX_WIDTH, 50));

            ingredients.setMaximumRowCount(40);
        }

    }

    private final class IngredientsPanel extends JPanel {

        private final int maxFactor = 4;

        private final JSlider slider;

        private final JTextField amount = new JTextField();

        private IngredientsPanel(final FoodType foodType, int grams) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            add(new JLabel(foodType.getFoodMetadata().getDescription()));

            add(amount);
            setAmount(grams);

            add(new JLabel("g"));

            slider = new JSlider(0, grams * maxFactor, grams);

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int val = slider.getValue();
                    setAmount(val);
                    meal.adjustIngredientAmount(foodType, val);
                }
            });

            add(slider);

            JButton remove = new JButton("Remove");
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    meal.removeIngredient(foodType);
                }
            });
            add(remove);
            setMaximumSize(new Dimension(MAX_WIDTH, 50));

        }

        void setAmount(int val) {
            amount.setText(Integer.toString(val));
        }
    }

    private final class AllIngredientsPanel extends JPanel implements AdjustableMeal.AdjustableMealListener {

        private final Map<FoodType, IngredientsPanel> ingredientsPanels = new LinkedHashMap<>();

        public AllIngredientsPanel() {
            meal.addListener(this);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        @Override
        public void onIngredientAdded(FoodType newFoodType, int amount) {
            IngredientsPanel newPanel = new IngredientsPanel(newFoodType, amount);
            ingredientsPanels.put(newFoodType, newPanel);
            add(newPanel);
            NewMealPanel.this.validate();
        }

        @Override
        public void onIngredientChanged(FoodType foodType, int oldValue, int newValue) {
            // Do nothing
        }

        @Override
        public void onIngredientRemoved(FoodType foodType, int valueAtRemoval) {
            remove(ingredientsPanels.remove(foodType));
            NewMealPanel.this.validate();
        }
    }

    private class TargetPanel extends JPanel {

        private final float greenTarget = 0.02F;

        private final float yellowTarget = 0.08F;

        private final JTextField value = new JTextField();

        private final JTextField percent = new JTextField();

        private float lastVal;

        private final boolean total;

        public TargetPanel(String text, JTextField field, boolean total, DocumentListener listener) {
            this.total = total;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel top = new JPanel();
            top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
            top.add(new JLabel(text));
            top.add(createHorizontalGlue());
            add(top);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            panel.add(field);
            panel.add(value);
            panel.add(new JLabel("kcal, or "));
            panel.add(percent);
            panel.add(new JLabel("%"));

            value.setEditable(total);
            percent.setEnabled(!total);

            (total ? value : percent).getDocument().addDocumentListener(listener);

            add(panel);
            setPreferredSize(new Dimension(MAX_WIDTH, 100));
            setMaximumSize(new Dimension(MAX_WIDTH, 200));
        }

        public void onChange(int target) {
            onChange(lastVal, target);
        }

        public void onChange(float value) {
            onChange(value, Utils.parseInt(this.value));
        }

        public void onChange(float value, int target) {
            lastVal = value;
            if (!total) {
                this.value.setText(Integer.toString(target));
            }
            if (target > 0) {
                float change = Math.abs(1F - (value / target));

                if (change <= greenTarget) {
                    setColor(Color.GREEN);
                } else if (change <= yellowTarget) {
                    setColor(Color.YELLOW);
                } else {
                    setColor(Color.RED);
                }
            }

        }

        private void setColor(Color color) {
            value.setBackground(color);
            percent.setBackground(color);
        }
    }


    private final class MealInformationPanel extends JPanel implements AdjustableMeal.AdjustableMealListener, CaloriesTarget.CaloriesTargetListener {

        private final JTextField totalCalories = new JTextField();
        private final TargetPanel totalCaloriesTarget;

        private final JTextField caloriesFromFat = new JTextField();
        private final TargetPanel caloriesFromFatTarget;

        private final JTextField caloriesFromProtein = new JTextField();
        private final TargetPanel caloriesFromProteinTarget;

        private final JTextField caloriesFromCarbs = new JTextField();
        private final TargetPanel caloriesFromCarbsTarget;

        public MealInformationPanel() {
            meal.addListener(this);
            caloriesTarget.addListener(this);

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            totalCaloriesTarget = new TargetPanel("Total Calories", totalCalories, true, new SimpleDocumentListener() {
                @Override
                public void onUpdate(DocumentEvent e, String text) {
                    caloriesTarget.setTotalCalories(Utils.parseInt(totalCalories, text));
                }
            });
            add(totalCaloriesTarget);

            caloriesFromFatTarget = new TargetPanel("Calories From Fat", caloriesFromFat, false, new SimpleDocumentListener() {
                @Override
                public void onUpdate(DocumentEvent e, String text) {
                    caloriesTarget.setFatCaloriesPercent(Utils.parseInt(caloriesFromFat, text));
                }
            });
            add(caloriesFromFatTarget);

            caloriesFromProteinTarget = new TargetPanel("Calories From Protein", caloriesFromProtein, false, new SimpleDocumentListener() {
                @Override
                public void onUpdate(DocumentEvent e, String text) {
                    caloriesTarget.setProteinCaloriesPercent(Utils.parseInt(caloriesFromProtein, text));
                }
            });
            add(caloriesFromProteinTarget);


            caloriesFromCarbsTarget = new TargetPanel("Calories From Carbs", caloriesFromCarbs, false, new SimpleDocumentListener() {
                @Override
                public void onUpdate(DocumentEvent e, String text) {
                    caloriesTarget.setProteinCaloriesPercent(Utils.parseInt(caloriesFromCarbs, text));
                }
            });
            add(caloriesFromCarbsTarget);

            totalCaloriesTarget.percent.setText(Integer.toString(caloriesTarget.getTotalCaloriesPercent()));
            caloriesFromFatTarget.percent.setText(Integer.toString(caloriesTarget.getFatCaloriesPercent()));
            caloriesFromProteinTarget.percent.setText(Integer.toString(caloriesTarget.getProteinCaloriesPercent()));
            caloriesFromCarbsTarget.percent.setText(Integer.toString(caloriesTarget.getCarbCaloriesPercent()));

        }

        @Override
        public void onIngredientAdded(FoodType newFoodType, int amount) {
            updateInfo();
        }

        @Override
        public void onIngredientChanged(FoodType foodType, int oldValue, int newValue) {
            updateInfo();
        }

        @Override
        public void onIngredientRemoved(FoodType foodType, int valueAtRemoval) {
            updateInfo();
        }

        private String format(float f) {
            return String.format("%.2f", f);
        }

        private void updateInfo() {
            float total = meal.totalCalories();
            totalCalories.setText(format(total));
            totalCaloriesTarget.onChange(total);

            float fat = meal.caloriesFromFat();
            caloriesFromFat.setText(format(fat));
            caloriesFromFatTarget.onChange(fat);

            float protein = meal.caloriesFromProtein();
            caloriesFromProtein.setText(format(protein));
            caloriesFromProteinTarget.onChange(protein);

            float carbs = meal.caloriesFromCarbs();
            caloriesFromCarbs.setText(format(carbs));
            caloriesFromCarbsTarget.onChange(carbs);
        }

        @Override
        public void onChange(CaloriesTarget target, boolean validPercentages) {
            totalCaloriesTarget.onChange(target.getTotalCalories());
            caloriesFromFatTarget.onChange(target.getFatCalories());
            caloriesFromProteinTarget.onChange(target.getProteinCalories());
            caloriesFromCarbsTarget.onChange(target.getCarbCalories());

            if (!validPercentages) {
                totalCaloriesTarget.percent.setBackground(Color.RED);
                caloriesFromFatTarget.percent.setBackground(Color.RED);
                caloriesFromProteinTarget.percent.setBackground(Color.RED);
                caloriesFromCarbsTarget.percent.setBackground(Color.RED);
            }
        }
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            onUpdate(e, getDocumentText(e.getDocument()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onUpdate(e, getDocumentText(e.getDocument()));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onUpdate(e, getDocumentText(e.getDocument()));
        }

        private String getDocumentText(Document doc) {
            try {
                return doc.getText(0, doc.getLength());
            } catch (BadLocationException e) {
                throw new IllegalStateException(e);
            }
        }

        public abstract void onUpdate(DocumentEvent e, String text);

    }
}
