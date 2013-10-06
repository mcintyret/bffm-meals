package com.mcintyret.bffm.gui;

import com.mcintyret.bffm.Data;
import com.mcintyret.bffm.raw.FoodDescription;
import com.mcintyret.bffm.types.FoodType;
import com.mcintyret.bffm.types.Nutrient;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class NewFoodPanel extends JPanel {

    private final LabelledTextField name = new LabelledTextField("Name");

    private final Map<Nutrient, LabelledTextField> nutrients = new EnumMap<>(Nutrient.class);

    private final Set<LabelledTextField> required = new HashSet<>();

    public NewFoodPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(name);

        for (Nutrient nutrient : Nutrient.values()) {
            LabelledTextField field = new LabelledTextField(nutrient.getName());
            nutrients.put(nutrient, field);
            panel.add(field);
        }

        add(new JScrollPane(panel));

        required.add(name);
        required.add(nutrients.get(Nutrient.PROCNT));
        required.add(nutrients.get(Nutrient.ENERC_KCAL));
        required.add(nutrients.get(Nutrient.FAT));
        required.add(nutrients.get(Nutrient.CHOCDF));

        JButton add = new JButton("Add");
        add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean missing = false;
                for (LabelledTextField require : required) {
                    if ("".equals(require.getText())) {
                        missing = true;
                        require.field.setBackground(Color.RED);
                    } else {
                        require.field.setBackground(Color.WHITE);
                    }
                }

                if (missing) {
                    JOptionPane.showMessageDialog(NewFoodPanel.this, "Required fields must have values.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FoodDescription desc = new FoodDescription(name.getText());
                EnumMap<Nutrient, Float> nutrientVals = new EnumMap<>(Nutrient.class);
                Nutrient curr = null;
                try {
                    for (Map.Entry<Nutrient, LabelledTextField> entry : nutrients.entrySet()) {

                        String text = entry.getValue().getText();
                        if (!"".equals(text)) {
                            curr = entry.getKey();
                            Float val = Float.valueOf(entry.getValue().getText());
                            nutrientVals.put(curr, val);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NewFoodPanel.this, "Error parsing value for " + curr.getName() + ". Fix and try again!", "Fail", JOptionPane.ERROR_MESSAGE);
                }

                FoodType newFoodType = new FoodType(nutrientVals, desc);
                Data.addFoodType(newFoodType);
                FoodIndex.addToIndex(newFoodType);

                clear();
            }
        });
    }

    public void clear() {
        name.field.setText("");
        for (LabelledTextField nutrient : nutrients.values()) {
            nutrient.field.setText("");
        }
    }

    private static class LabelledTextField extends JPanel {

        private final JTextField field = new JTextField();

        public LabelledTextField(String labelText) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            JLabel label = new JLabel(labelText);

            label.setBorder(new LineBorder(Color.RED));

            add(label);
            add(Box.createHorizontalGlue());
            add(field);

            field.setMaximumSize(new Dimension(1000, 60));
            field.setPreferredSize(new Dimension(1000, 60));

            setMaximumSize(new Dimension(3000, 60));
        }

        public String getText() {
            return field.getText();
        }

    }
}
