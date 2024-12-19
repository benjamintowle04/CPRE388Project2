package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfit.models.User;
import com.example.getfit.models.UserStats;
import com.example.getfit.util.UserManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Activity class responsible for handling the user's bio data input, including weight, height,
 * target weight, and fitness intensity level. It calculates the user's daily calorie goal range
 * based on these inputs and updates the user data in the system.
 * <p>
 * This activity also displays a greeting message with the user's name and handles the submission
 * of the user's bio data for storage in Firebase.
 * </p>
 */
public class BioDataActivity extends AppCompatActivity {

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText targetWeightEditText;
    private Spinner intensitySpinner;
    private Button submitButton;
    private TextView userGreeting;
    private UserStats userStats; // Reference to UserStats for saving calorie range
    private String selectedIntensity;

    /**
     * Called when the activity is created. Initializes the UI elements and sets up listeners.
     * <p>
     * Sets up the intensity level spinner, fetches user data to display on the screen, and initializes
     * the UserStats for storing calorie data. It also sets up the submit button's click listener to
     * handle user input and data submission.
     * </p>
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_data);

        // Initialize views
        weightEditText = findViewById(R.id.weight_edt);
        heightEditText = findViewById(R.id.height_edt);
        targetWeightEditText = findViewById(R.id.target_weight_edt);
        intensitySpinner = findViewById(R.id.intensity_spinner);
        submitButton = findViewById(R.id.submit_btn);
        userGreeting = findViewById(R.id.greeting_text);

        // Set up the spinner (dropdown) for fitness intensity levels
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.intensity_levels, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(adapter);
        setUserDataOnScreen();

        // Initialize UserStats to save the calorie range
        userStats = new UserStats(this);

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String weightS = weightEditText.getText().toString();
                String heightS = heightEditText.getText().toString();
                String targetWeightS = targetWeightEditText.getText().toString();

                if (weightS.isEmpty() || heightS.isEmpty() || targetWeightS.isEmpty()) {
                    Toast.makeText(BioDataActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Calculate the daily calorie goal range based on user input
                    int weight = Integer.parseInt(weightS);
                    int height = Integer.parseInt(heightS);
                    int targetWeight = Integer.parseInt(targetWeightS);
                    selectedIntensity = intensitySpinner.getSelectedItem().toString();

                    // Calculate and save the calorie goal range
                    int[] calorieRange = calculateDailyCalorieGoalRange(weight, height, targetWeight, selectedIntensity);
                    userStats.saveDailyCalorieGoalRange(calorieRange[0], calorieRange[1]);

                    updateUserData(weight, height, targetWeight, selectedIntensity);

                    // Reset total calories and points
                    userStats.resetTotalCalories(); // Reset total calories
                    userStats.resetPoints(); // Reset points

                    // Proceed to the next activity
                    Toast.makeText(BioDataActivity.this, "Data Submitted!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BioDataActivity.this, MyHealthActivity.class));
                }
            }
        });
    }

    /**
     * Calculates the daily calorie goal range based on the user's weight, height, target weight,
     * and intensity level.
     * <p>
     * This method uses the Harris-Benedict equation to estimate the user's Basal Metabolic Rate (BMR),
     * applies an activity factor based on the selected intensity level, and adjusts the calorie goal
     * for weight loss or gain.
     * </p>
     *
     * @param weight The user's weight in kilograms.
     * @param height The user's height in centimeters.
     * @param targetWeight The user's target weight in kilograms.
     * @param intensityLevel The selected intensity level for exercise (Low, Medium, High).
     * @return An array of two integers representing the lower and upper bounds of the daily calorie goal.
     */
    private int[] calculateDailyCalorieGoalRange(int weight, int height, int targetWeight, String intensityLevel) {
        // Basic BMR calculation using the Harris-Benedict equation
        int bmr = (int) (10 * weight + 6.25 * height - 5 * 30 + 5); // Simplified for a 30-year-old male (you can add age as an input if needed)

        // Activity factor based on intensity
        double activityFactor = getActivityFactor(selectedIntensity);

        // Calculate TDEE
        int tdee = (int) (bmr * activityFactor);

        // Adjust for weight loss or gain
        int calorieGoal = tdee;

        // Modify based on target weight
        if (targetWeight < weight) {
            calorieGoal -= 500; // Caloric deficit for weight loss
        } else if (targetWeight > weight) {
            calorieGoal += 500; // Caloric surplus for weight gain
        }

        // Calculate a range (e.g., ±10% of the calorie goal)
        int lowerBound = (int) (calorieGoal * 0.9); // 10% less than the calorie goal
        int upperBound = (int) (calorieGoal * 1.1); // 10% more than the calorie goal

        return new int[]{lowerBound, upperBound};
    }

    /**
     * Returns the activity factor based on the selected intensity level.
     *
     * @param intensityLevel The intensity level selected by the user.
     * @return The activity factor to apply to the Basal Metabolic Rate (BMR).
     */
    private double getActivityFactor(String intensityLevel) {
        switch (intensityLevel) {
            case "Low":
                return 1.2;  // Sedentary (little to no exercise)
            case "Medium":
                return 1.55; // Moderate activity (moderate exercise 3-5 days/week)
            case "High":
                return 1.9;  // Very active (hard exercise 6-7 days/week)
            default:
                return 1.2;  // Default to low activity
        }
    }

    /**
     * Updates the user data in the UserManager with the new bio data.
     *
     * @param height The user's height in centimeters.
     * @param weight The user's weight in kilograms.
     * @param targetWeight The user's target weight in kilograms.
     * @param intensityLevel The selected intensity level for exercise.
     */
    private void updateUserData(int height, int weight, int targetWeight, String intensityLevel) {
        UserManager.getInstance(BioDataActivity.this)
                .updateUserBioData("DUMMY DATA BECAUSE WE WONT CHANGE THE NAME", height, weight, targetWeight, intensityLevel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully updated the user data
                        Toast.makeText(BioDataActivity.this, "User data updated!", Toast.LENGTH_SHORT).show();
                        Log.d("BioDataActivity", "User data updated successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Failed to update user data
                        Toast.makeText(BioDataActivity.this, "Error updating data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("BioDataActivity", "Error updating user data", e);
                    }
                });
    }

    /**
     * Fetches the current user data and displays it on the screen.
     */
    public void setUserDataOnScreen() {
        UserManager.getInstance(this).fetchUserData()
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            // Use the user object to update the UI
                            userGreeting.setText("Hello, " + user.getName() + "!");
                            Log.d("BioDataActivity", "Fetched user: " + user.getName());
                        } else {
                            Toast.makeText(BioDataActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BioDataActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
