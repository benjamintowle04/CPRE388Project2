package com.example.getfit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfit.models.User;
import com.example.getfit.util.UserManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * UserSettingsActivity allows users to update their profile settings, including their name,
 * weight, target weight, and fitness intensity level. The activity fetches the current user data
 * from Firebase, loads it into the relevant fields, and allows users to modify and save the new data.
 * It uses Firebase to store the updated information.
 */
public class UserSettingsActivity extends AppCompatActivity {

    private EditText editName, editWeight, editTargetWeight;
    private Spinner spinnerIntensity;
    private Button confirmButton;

    /**
     * Called when the activity is created. Initializes the UI elements and sets up event listeners
     * for the confirm button. It also loads the current user data from Firebase.
     *
     * @param savedInstanceState A bundle containing any previously saved state of the activity.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Initialize Views
        editName = findViewById(R.id.editName);
        editWeight = findViewById(R.id.editWeight);
        editTargetWeight = findViewById(R.id.editTargetWeight);
        spinnerIntensity = findViewById(R.id.spinnerIntensity);
        confirmButton = findViewById(R.id.confirmButton);

        // Set up the spinner (dropdown) for fitness intensity levels
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.intensity_levels, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntensity.setAdapter(adapter);

        // Load current user data
        loadUserData();

        // Set confirm button click listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    /**
     * Loads the current user's data from Firebase and sets it in the corresponding UI fields.
     * If data is successfully fetched, the fields are populated with the current user information.
     * If fetching data fails, an error message is shown.
     */
    private void loadUserData() {
        UserManager.getInstance(this).fetchUserData()
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            // Set current values to the fields
                            editName.setText(user.getName());
                            editWeight.setText(String.valueOf(user.getWeight()));
                            editTargetWeight.setText(String.valueOf(user.getTargetWeight()));
                            // Assuming the intensity level is stored as a string
                            setSpinnerIntensity(user.getIntensityLevel());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UserSettingsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Saves the updated user data back to Firebase. It reads the updated data from the UI,
     * creates a new `User` object with the new values, and then updates the user settings in Firebase.
     * Upon successful save, a success message is displayed and the activity is closed.
     * If the save operation fails, an error message is shown.
     */
    private void saveUserData() {
        String name = editName.getText().toString().trim();
        int weight = Integer.parseInt(editWeight.getText().toString().trim());
        int targetWeight = Integer.parseInt(editTargetWeight.getText().toString().trim());
        String intensityLevel = spinnerIntensity.getSelectedItem().toString();

        // Create a User object with the new data
        User updatedUser = new User(name, UserManager.getInstance(this).getCurrentUser().getEmail(),
                UserManager.getInstance(this).getCurrentUser().getHeight(), weight, intensityLevel, targetWeight);

        // Save the updated user data to Firebase
        UserManager.getInstance(this).updateUserSettingsData(name, updatedUser.getHeight(), weight, targetWeight, intensityLevel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Show success message
                        Toast.makeText(UserSettingsActivity.this, "User data updated successfully", Toast.LENGTH_SHORT).show();

                        // Navigate back to the MyHealthActivity
                        finish(); // This will finish the current activity and return to the previous one
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UserSettingsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Sets the selected intensity level in the spinner based on the user's current intensity level.
     * This method ensures that the correct intensity level is selected when the user opens the settings screen.
     *
     * @param intensityLevel The intensity level to be selected (e.g., "Beginner", "Moderate", "Hard-core").
     */
    private void setSpinnerIntensity(String intensityLevel) {
        // Assuming the spinner contains values like "Beginner", "Moderate", "Hard-core"
        switch (intensityLevel) {
            case "Beginner":
                spinnerIntensity.setSelection(0);
                break;
            case "Moderate":
                spinnerIntensity.setSelection(1);
                break;
            case "Hard-core":
                spinnerIntensity.setSelection(2);
                break;
        }
    }
}
