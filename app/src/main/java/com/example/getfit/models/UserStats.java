package com.example.getfit.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * UserStats is a class that manages the user's statistics, including their calorie intake, points, and daily calorie goal range.
 * It provides functionality to track, update, and reset the total calories and points, as well as check if the user is within their calorie goal range.
 * The class also handles the logic for resetting values at midnight and managing the check-in status for the user.
 */
public class UserStats {

    private static final String PREFS_NAME = "userStatsPrefs";
    private static final String KEY_TOTAL_CALORIES = "totalCalories";
    private static final String KEY_LAST_RESET_TIMESTAMP = "lastResetTimestamp";
    private static final String KEY_POINTS = "total_points";
    private static final String KEY_LAST_CHECK_IN_DATE = "last_check_in_date";
    private static final String KEY_CALORIE_GOAL_LOWER = "calorieGoalLower";
    private static final String KEY_CALORIE_GOAL_UPPER = "calorieGoalUpper";
    private SharedPreferences sharedPreferences;

    /**
     * Constructor for the UserStats class.
     * Initializes the SharedPreferences object to manage user data.
     *
     * @param context the context used to access SharedPreferences
     */
    public UserStats(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves the user's daily calorie goal range (lower and upper bounds).
     *
     * @param lowerBound the lower bound of the calorie goal range
     * @param upperBound the upper bound of the calorie goal range
     */
    public void saveDailyCalorieGoalRange(int lowerBound, int upperBound) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CALORIE_GOAL_LOWER, lowerBound);
        editor.putInt(KEY_CALORIE_GOAL_UPPER, upperBound);
        editor.apply();
    }

    /**
     * Checks if the total calories consumed by the user fall within the daily calorie goal range.
     *
     * @param totalCalories the total calories consumed by the user
     * @return true if the calories are within the range, false otherwise
     */
    public boolean isWithinCalorieGoalRange(int totalCalories) {
        int[] calorieRange = getDailyCalorieGoalRange();
        int lowerBound = calorieRange[0];
        int upperBound = calorieRange[1];

        return totalCalories >= lowerBound && totalCalories <= upperBound;
    }

    /**
     * Retrieves the user's daily calorie goal range (lower and upper bounds).
     *
     * @return an array of integers where the first element is the lower bound and the second element is the upper bound
     */
    public int[] getDailyCalorieGoalRange() {
        int lowerBound = sharedPreferences.getInt(KEY_CALORIE_GOAL_LOWER, 0);
        int upperBound = sharedPreferences.getInt(KEY_CALORIE_GOAL_UPPER, 0);
        return new int[]{lowerBound, upperBound};
    }

    /**
     * Adds points to the user's total points if their total calories are within the daily calorie goal range.
     *
     * @param totalCalories the total calories consumed by the user
     * @param points        the number of points to be added
     */
    public void addPointsForCalorieRange(int totalCalories, int points) {
        if (isWithinCalorieGoalRange(totalCalories)) {
            addPoints(points);
        }
    }

    /**
     * Retrieves the current total calories consumed by the user.
     * Resets the calorie count if it's a new day (after midnight).
     *
     * @return the total calories consumed by the user
     */
    public int getTotalCalories() {
        if (isMidnightPassed()) {
            resetCalories();
        }
        return sharedPreferences.getInt(KEY_TOTAL_CALORIES, 0);
    }

    /**
     * Adds a specified number of calories to the user's total calories.
     * Resets the calorie count if it's a new day (after midnight).
     *
     * @param calories the number of calories to add
     */
    public void addCalories(int calories) {
        if (isMidnightPassed()) {
            resetCalories();
        }

        int currentTotal = getTotalCalories();
        int newTotal = currentTotal + calories;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_CALORIES, newTotal);
        editor.putLong(KEY_LAST_RESET_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * Resets the user's calorie count to zero and updates the reset timestamp.
     */
    public void resetCalories() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_CALORIES, 0);
        editor.putLong(KEY_LAST_RESET_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * Retrieves the current total points of the user.
     *
     * @return the total points of the user
     */
    public int getTotalPoints() {
        return sharedPreferences.getInt(KEY_POINTS, 0);
    }

    /**
     * Adds points to the user's total points.
     *
     * @param points the number of points to add
     */
    public void addPoints(int points) {
        int currentPoints = getTotalPoints();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_POINTS, currentPoints + points);
        editor.apply();
    }

    /**
     * Checks if it is a new day (midnight has passed since the last calorie reset).
     *
     * @return true if midnight has passed, false otherwise
     */
    private boolean isMidnightPassed() {
        long currentTime = System.currentTimeMillis();
        long lastResetTime = sharedPreferences.getLong(KEY_LAST_RESET_TIMESTAMP, 0);

        if (lastResetTime == 0) {
            return true;
        }

        Calendar currentCalendar = Calendar.getInstance();
        Calendar lastResetCalendar = Calendar.getInstance();
        lastResetCalendar.setTimeInMillis(lastResetTime);

        if (currentCalendar.get(Calendar.YEAR) > lastResetCalendar.get(Calendar.YEAR) ||
                currentCalendar.get(Calendar.DAY_OF_YEAR) > lastResetCalendar.get(Calendar.DAY_OF_YEAR)) {
            return true;
        }

        return false;
    }

    /**
     * Updates the check-in date to today's date.
     */
    public void updateCheckInDate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_CHECK_IN_DATE, currentDate);
        editor.apply();
    }

    /**
     * Checks if the user has already checked in today.
     *
     * @return true if the user has checked in today, false otherwise
     */
    public boolean hasCheckedInToday() {
        if (getTotalPoints() == 0) {
            return false;
        }

        String lastCheckInDate = sharedPreferences.getString(KEY_LAST_CHECK_IN_DATE, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return currentDate.equals(lastCheckInDate);
    }

    /**
     * Resets the user's total calories to zero.
     */
    public void resetTotalCalories() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_CALORIES, 0);
        editor.apply();
    }

    /**
     * Resets the user's points to zero.
     */
    public void resetPoints() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_POINTS, 0);
        editor.apply();
    }
}
