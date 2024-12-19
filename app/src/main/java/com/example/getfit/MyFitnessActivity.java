package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.adapter.ExerciseAdapter;
import com.example.getfit.models.Exercise;
import com.example.getfit.models.User;
import com.example.getfit.util.UserManager;
import com.example.getfit.util.WorkoutParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * The {@link MyFitnessActivity} class is responsible for managing the user's fitness-related data
 * in the application. It displays a list of workouts based on the user's preferences and allows
 * navigation to other activities such as adding new workouts or viewing the user's health data.
 * <p>
 * This activity fetches the user's data, displays filtered workout routines based on the user's
 * intensity level and weight goals, and provides navigation options through buttons.
 */
public class MyFitnessActivity extends AppCompatActivity {

    private RecyclerView workoutRecyclerView;
    private ExerciseAdapter workoutAdapter;
    private Button backButton; // Declare the back button
    private Button addBtn;

    /**
     * Called when the activity is created. Initializes UI components, sets up the RecyclerView,
     * and defines the behavior of the back and add workout buttons.
     *
     * @param savedInstanceState a Bundle containing the activity's previous state, or null if it's the first time the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_fitness);

        // Initialize RecyclerView
        workoutRecyclerView = findViewById(R.id.workoutRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use a LinearLayoutManager
        workoutAdapter = new ExerciseAdapter(MyFitnessActivity.this, new ArrayList<>()); // Initialize the adapter with an empty list
        workoutRecyclerView.setAdapter(workoutAdapter);

        // Initialize back button
        backButton = findViewById(R.id.backButton);
        addBtn = findViewById(R.id.addWorkoutButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFitnessActivity.this, MyHealthActivity.class));
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFitnessActivity.this, AddWorkoutActivity.class));
                finish();
            }
        });

        setUserDataOnScreen();
    }

    /**
     * Called when the activity is resumed. This ensures the user's data is fetched and displayed
     * whenever the activity is brought back into focus.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUserDataOnScreen();
    }

    /**
     * Fetches the user's data and displays it on the screen. If successful, the workouts
     * are fetched and filtered based on the user's preferences.
     */
    public void setUserDataOnScreen() {
        UserManager.getInstance(this).fetchUserData()
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            fetchAndDisplayWorkouts(user);
                        } else {
                            Toast.makeText(MyFitnessActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MyFitnessActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Fetches the workouts based on the user's preferences and updates the RecyclerView with
     * the filtered workout list.
     *
     * @param user the current user whose data and preferences are used to filter workouts
     */
    private void fetchAndDisplayWorkouts(User user) {
        // Call the WorkoutParser to fetch workouts
        WorkoutParser.fetchWorkouts(new WorkoutParser.WorkoutCallback() {
            @Override
            public void onSuccess(ArrayList<Exercise> exercises) {
                // Filter exercises by the user's intensity level and weight goals
                ArrayList<Exercise> filteredExercises = WorkoutParser.filterByIntensity(exercises, user);

                // Update the RecyclerView with the filtered exercises on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the adapter with the filtered list of exercises
                        workoutAdapter.updateExerciseList(filteredExercises); // Use the new method to update data
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFitnessActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
