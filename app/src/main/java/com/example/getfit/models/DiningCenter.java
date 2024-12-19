package com.example.getfit.models;

/**
 * The DiningCenter class represents a dining center within the app. It contains
 * information about the dining center such as its name, type, icon resource ID,
 * and a slug used for fetching the menu of that dining center.
 */
public class DiningCenter {

    private String name;
    private String type;
    private int iconResId;  // This will hold the resource ID for the icon
    private String slug; // Used to get that location's menu

    /**
     * Constructor for the DiningCenter class.
     * Initializes the dining center with the provided values.
     *
     * @param name       the name of the dining center
     * @param type       the type of the dining center (e.g., cafeteria, restaurant)
     * @param iconResId  the resource ID of the icon representing the dining center
     * @param slug       a unique identifier (slug) used for fetching the dining center's menu
     */
    public DiningCenter(String name, String type, int iconResId, String slug) {
        this.name = name;
        this.type = type;
        this.iconResId = iconResId;
        this.slug = slug;
    }

    /**
     * Gets the name of the dining center.
     *
     * @return the name of the dining center
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the dining center (e.g., cafeteria, restaurant).
     *
     * @return the type of the dining center
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the resource ID of the icon representing the dining center.
     *
     * @return the icon resource ID
     */
    public int getIconResId() {
        return iconResId;
    }

    /**
     * Gets the slug used to fetch the menu for this dining center.
     *
     * @return the slug of the dining center
     */
    public String getSlug() {
        return slug;
    }
}
