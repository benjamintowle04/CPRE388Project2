package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfit.models.Exercise;
import com.example.getfit.util.WorkoutParser;

import java.util.ArrayList;

/**
 * AddWorkoutActivity allows users to add a new workout/exercise by filling in the workout details.
 * The user can input the name, muscle group, sets, reps, type (Cardio or Strength), and difficulty level.
 * The exercise is saved to the system and the user is redirected to the MyFitnessActivity on success.
 */
public class AddWorkoutActivity extends AppCompatActivity {

    private EditText nameEditText, muscleGroupEditText, setsEditText, repsEditText;
    private Spinner typeSpinner, difficultySpinner;
    private Button saveButton;
    private String selectedType, selectedDifficulty;

    /**
     * Initializes the activity's UI elements and sets up event listeners for the spinners and save button.
     *
     * @param savedInstanceState the saved instance state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        // Initialize views
        nameEditText = findViewById(R.id.workoutName);
        muscleGroupEditText = findViewById(R.id.workoutMuscleGroup);
        setsEditText = findViewById(R.id.workoutSets);
        repsEditText = findViewById(R.id.workoutReps);
        saveButton = findViewById(R.id.saveWorkoutButton);

        // Initialize Spinners
        typeSpinner = findViewById(R.id.workoutType);
        difficultySpinner = findViewById(R.id.workoutDifficulty);

        // Set up Type Spinner (Cardio, Strength)
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.exercise_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Set up Difficulty Spinner (Easy, Medium, Hard)
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        // Set listeners to capture selected values
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedType = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedType = "Cardio"; // default value
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDifficulty = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedDifficulty = "Easy"; // default value
            }
        });

        // Set onClickListener for Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkout();
            }
        });
    }

    /**
     * Collects the input from the user, creates a new Exercise object, and attempts to save it to the system.
     * On success, the user is redirected to MyFitnessActivity.
     * If there's an error, an error message is displayed.
     */
    private void addWorkout() {
        // Get input from the user
        String name = nameEditText.getText().toString();
        String muscleGroup = muscleGroupEditText.getText().toString();
        int sets = Integer.parseInt(setsEditText.getText().toString());
        String reps = repsEditText.getText().toString();

        // Create new Exercise object
        Exercise newExercise = new Exercise(0, name, muscleGroup, selectedType, sets, reps, selectedDifficulty);

        // Save the exercise (this can be saved to a database, or shared between activities)
        WorkoutParser.addExercise(newExercise, new WorkoutParser.WorkoutCallback() {
            @Override
            public void onSuccess(ArrayList<Exercise> exercises) {
                // Go back to MyFitnessActivity and show success
                Intent intent = new Intent(AddWorkoutActivity.this, MyFitnessActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                // Show error message
                Toast.makeText(AddWorkoutActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
