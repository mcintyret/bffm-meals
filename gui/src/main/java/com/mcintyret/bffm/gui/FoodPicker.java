package com.mcintyret.bffm.gui;

import com.mcintyret.bffm.Data;
import com.mcintyret.bffm.types.FoodType;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class FoodPicker {

    private final JComboBox<FoodType> combo = new JComboBox<>(Data.foodTypeVector());

    private boolean ignore = false;

    public FoodPicker() {
        combo.setMaximumRowCount(20);
        combo.setEditable(true);

        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ignore) {
                    ignore = !ignore;
                    String text = ((JTextComponent) combo.getEditor().getEditorComponent()).getText();
                    System.out.println(text);
                    Collection<FoodType> matches = FoodIndex.query(text);
                    combo.setModel(new DefaultComboBoxModel<>(new Vector<>(matches)));
                } else {
                    ignore = false;
                }
            }
        });
    }

    public JComboBox<FoodType> getCombo() {
        return combo;
    }
}
