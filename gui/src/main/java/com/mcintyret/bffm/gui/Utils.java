package com.mcintyret.bffm.gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class Utils {

    public static float parseFloat(JTextComponent comp) {
        return parseFloat(comp, comp.getText());
    }

    public static float parseFloat(JTextComponent comp, String text) {
        if ("".equals(text)) {
            return 0F;
        } else {
            try {
                return Float.parseFloat(text);
            } catch (Exception e) {
                return parseFloat(comp, JOptionPane.showInputDialog(comp, text + " is not a valid amount. Try harder.", "error", JOptionPane.ERROR_MESSAGE));
            }
        }
    }

    public static int parseInt(JTextComponent comp) {
        return parseInt(comp, comp.getText());
    }

    public static int parseInt(JTextComponent comp, String text) {
        if ("".equals(text)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(text);
            } catch (Exception e) {
                return parseInt(comp, JOptionPane.showInputDialog(comp, text + " is not a valid amount. Try harder.", "error", JOptionPane.ERROR_MESSAGE));
            }
        }
    }

}
