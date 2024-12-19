package com.example.getfit.models;

/**
 * The User class represents the data of a user in the application. It contains
 * details such as the user's name, height, weight, intensity level, email,
 * and target weight. This class is designed to work with user data, especially
 * in contexts like fitness tracking and goal setting.
 */
public class User {

    private String name;            // The name of the user
    private int height;             // The height of the user in centimeters
    private int weight;             // The weight of the user in kilograms
    private String intensityLevel;  // The user's exercise intensity level (e.g., low, medium, high)
    private String email;           // The user's email address
    private int targetWeight;       // The user's target weight for fitness goals

    /**
     * Constructor to initialize the User object with provided values.
     *
     * @param name            the name of the user
     * @param email           the email address of the user
     * @param height          the height of the user in centimeters
     * @param weight          the weight of the user in kilograms
     * @param intensity_level the intensity level of the user's workout (e.g., low, medium, high)
     * @param target_weight   the target weight that the user aims to achieve
     */
    public User(String name, String email, int height, int weight, String intensity_level, int target_weight) {
        this.name = name;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.intensityLevel = intensity_level;
        this.targetWeight = target_weight;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the height of the user in centimeters.
     *
     * @return the height of the user
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the user in centimeters.
     *
     * @param height the height of the user
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the weight of the user in kilograms.
     *
     * @return the weight of the user
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the user in kilograms.
     *
     * @param weight the weight of the user
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the intensity level of the user's workout (e.g., low, medium, high).
     *
     * @return the intensity level of the user's workout
     */
    public String getIntensityLevel() {
        return intensityLevel;
    }

    /**
     * Sets the intensity level of the user's workout (e.g., low, medium, high).
     *
     * @param intensity_level the intensity level of the user's workout
     */
    public void setIntensityLevel(String intensity_level) {
        this.intensityLevel = intensity_level;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the target weight of the user.
     *
     * @return the target weight that the user aims to achieve
     */
    public int getTargetWeight() {
        return this.targetWeight;
    }

    /**
     * Sets the target weight of the user.
     *
     * @param target_weight the target weight that the user aims to achieve
     */
    public void setTargetWeight(int target_weight) {
        this.targetWeight = target_weight;
    }
}
