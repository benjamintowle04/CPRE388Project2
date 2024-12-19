package com.example.getfit.models;

/**
 * The Exercise class represents an individual exercise in a fitness app.
 * It stores information about the exercise such as its name, muscle group,
 * type, sets, reps, and difficulty level. This class also provides getter and
 * setter methods for each attribute and a toString() method for easy display.
 */
public class Exercise {

    private int id;
    private String name;
    private String muscleGroup;
    private String type;
    private int sets;
    private Object reps;  // reps can be either an Integer or a String
    private String difficulty;

    /**
     * Constructor for the Exercise class.
     * Initializes the exercise with the provided values.
     *
     * @param id           the unique identifier of the exercise
     * @param name         the name of the exercise
     * @param muscleGroup  the muscle group targeted by the exercise
     * @param type         the type of exercise (e.g., strength, cardio)
     * @param sets         the number of sets to be performed
     * @param reps         the number of repetitions, can be an Integer or String (e.g., "AMRAP" for As Many Reps As Possible)
     * @param difficulty   the difficulty level of the exercise (e.g., easy, medium, hard)
     */
    public Exercise(int id, String name, String muscleGroup, String type, int sets, Object reps, String difficulty) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.type = type;
        this.sets = sets;
        this.reps = reps;
        this.difficulty = difficulty;
    }

    // Getter and Setter methods

    /**
     * Gets the unique identifier of the exercise.
     *
     * @return the ID of the exercise
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the exercise.
     *
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the exercise.
     *
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the exercise.
     *
     * @param name the name of the exercise to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the muscle group targeted by the exercise.
     *
     * @return the muscle group
     */
    public String getMuscleGroup() {
        return muscleGroup;
    }

    /**
     * Sets the muscle group targeted by the exercise.
     *
     * @param muscleGroup the muscle group to set
     */
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    /**
     * Gets the type of the exercise (e.g., strength, cardio).
     *
     * @return the type of exercise
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the exercise (e.g., strength, cardio).
     *
     * @param type the type of exercise to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the number of sets to be performed for the exercise.
     *
     * @return the number of sets
     */
    public int getSets() {
        return sets;
    }

    /**
     * Sets the number of sets to be performed for the exercise.
     *
     * @param sets the number of sets to set
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Gets the number of repetitions for the exercise, or a string representing reps in some cases.
     *
     * @return the number of repetitions (Integer) or a string (e.g., "AMRAP")
     */
    public Object getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions for the exercise, or a string representing reps in some cases.
     *
     * @param reps the number of repetitions or a string (e.g., "AMRAP")
     */
    public void setReps(Object reps) {
        this.reps = reps;
    }

    /**
     * Gets the difficulty level of the exercise.
     *
     * @return the difficulty level of the exercise (e.g., easy, medium, hard)
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the exercise.
     *
     * @param difficulty the difficulty level to set (e.g., easy, medium, hard)
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns a string representation of the Exercise object, useful for display purposes.
     *
     * @return a string containing the exercise's details
     */
    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", muscleGroup='" + muscleGroup + '\'' +
                ", type='" + type + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
