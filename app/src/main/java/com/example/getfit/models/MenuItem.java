package com.example.getfit.models;

/**
 * The MenuItem class represents a menu item at a dining center. It contains
 * information about the name of the item and its total calorie count.
 */
public class MenuItem {

    private String name;  // Name of the menu item
    private int totalCal; // Total calories in the menu item

    /**
     * Constructor for the MenuItem class.
     * Initializes the menu item with the provided name and total calories.
     *
     * @param name     the name of the menu item
     * @param totalCal the total calorie count for the menu item
     */
    public MenuItem(String name, int totalCal) {
        this.name = name;
        this.totalCal = totalCal;
    }

    /**
     * Gets the name of the menu item.
     *
     * @return the name of the menu item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the total calorie count for the menu item.
     *
     * @return the total calories of the menu item
     */
    public int getTotalCal() {
        return totalCal;
    }
}
