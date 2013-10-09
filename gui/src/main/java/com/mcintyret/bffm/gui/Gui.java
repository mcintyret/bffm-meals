package com.mcintyret.bffm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * User: tommcintyre
 * Date: 10/6/13
 */
public class Gui {

    public static final JFrame FRAME = new JFrame("BFFM Meals");

    private static final JTabbedPane TABBED_PANE = new JTabbedPane(SwingConstants.TOP);

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize() {
        FRAME.setBounds(100, 100, 1000, 900);
        FRAME.getContentPane().setLayout(new BorderLayout(0, 0));
        FRAME.toFront();
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        menu.add(new JMenuItem("New Meal") {{
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            addTab("New Meal", new NewMealPanel());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(FRAME.getContentPane(), "Error loading data for New Meal page", "Error", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
            }
        });

        menu.add(new JMenuItem("New Food") {{
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addTab("New Food", new NewFoodPanel());
                    }
                });
            }
        });


        FRAME.getContentPane().add(menuBar, BorderLayout.NORTH);

        FRAME.getContentPane().add(TABBED_PANE, BorderLayout.CENTER);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Gui.FRAME.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ((JMenuItem) menu.getMenuComponent(0)).doClick();
    }

    public static void addTab(String title, Component component) {
        TABBED_PANE.addTab(title, null, component, null);
        TABBED_PANE.setTabComponentAt(TABBED_PANE.getTabCount() - 1, new ButtonTabComponent(TABBED_PANE));
        TABBED_PANE.setSelectedIndex(TABBED_PANE.getTabCount() - 1);
    }

}
