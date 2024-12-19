package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.adapter.DiningCenterAdapter;
import com.example.getfit.models.DiningCenter;
import com.example.getfit.models.UserStats;
import com.example.getfit.util.DiningCenterFetcher;

import java.util.List;

/**
 * MyDiningActivity displays information about dining centers where the user can view meal options.
 * The activity includes a list of dining centers and updates the user's calorie count and goal range.
 * It also offers a button to navigate back to the previous activity.
 */
public class MyDiningActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiningCenterAdapter diningCenterAdapter;
    private Button backBtn;
    private TextView calorieCountTxt;
    private TextView goalRangeTxt; // Single TextView for the goal range
    private UserStats userStats;

    /**
     * Initializes the activity, sets up the RecyclerView to display dining centers,
     * and updates the user's calorie count and goal range.
     *
     * @param savedInstanceState the saved instance state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dining);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewDiningCenters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backBtn = findViewById(R.id.back_btn);
        calorieCountTxt = findViewById(R.id.calorieCountTextView);
        goalRangeTxt = findViewById(R.id.goalRangeTextView); // Initialize the single goal range TextView
        userStats = new UserStats(MyDiningActivity.this);

        // Set back button listener to navigate to MyHealthActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyDiningActivity.this, MyHealthActivity.class));
                finish();
            }
        });

        // Fetch dining center data in the background (using a new thread)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<DiningCenter> diningCenters = DiningCenterFetcher.fetchDiningCenters();
                    // Update the RecyclerView on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            diningCenterAdapter = new DiningCenterAdapter(MyDiningActivity.this, diningCenters);
                            recyclerView.setAdapter(diningCenterAdapter);
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyDiningActivity.this, "Failed to fetch dining centers", Toast.LENGTH_SHORT).show();
                            Log.d("MyDiningActivity", "Error Fetching Dining Centers");
                        }
                    });
                }
            }
        }).start();

        // Update calorie count and goal range display
        updateCalorieCount();
        updateGoalRange();

        // Add points if the current calories are within the goal range
        userStats.addPointsForCalorieRange(userStats.getTotalCalories(), 30);
    }

    /**
     * Updates the TextView displaying the total calories consumed by the user.
     */
    private void updateCalorieCount() {
        int totalCalories = userStats.getTotalCalories();  // Get the current total calories
        calorieCountTxt.setText("Total Calories: " + totalCalories);  // Update the TextView with the total calorie count
    }

    /**
     * Updates the TextView displaying the user's daily calorie goal range.
     */
    private void updateGoalRange() {
        int[] calorieRange = userStats.getDailyCalorieGoalRange();  // Get the calorie goal range (lower and upper bounds)
        int lowerBound = calorieRange[0];
        int upperBound = calorieRange[1];

        // Set the goal range as a single string in the TextView
        goalRangeTxt.setText("Goal Range: " + lowerBound + " - " + upperBound);
    }
}
