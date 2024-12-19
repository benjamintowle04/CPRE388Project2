package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.adapter.MenuAdapter;
import com.example.getfit.models.MenuItem;
import com.example.getfit.models.UserStats;
import com.example.getfit.util.MenuParser;

import java.util.ArrayList;

/**
 * The MenuActivity class displays a list of menu items to the user. It allows the user to view menu items
 * and their associated calorie values, and adds the calories of selected menu items to the user's total calorie count.
 * <p>
 * The activity fetches the menu data from a server based on a "slug" passed via the Intent. The data is displayed
 * in a RecyclerView. When a user selects a menu item, the corresponding calories are added to the user's total.
 * A back button is provided to return to the previous screen.
 * </p>
 */
public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private Button backButton;
    private UserStats userStats;  // UserStats object to track total calories

    /**
     * Called when the activity is created. Initializes the RecyclerView, back button, and user stats.
     * It also fetches menu data in a background thread and updates the RecyclerView on the main thread.
     *
     * @param savedInstanceState The saved instance state, if any, for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize back button and set its click listener
        backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> {
            // Navigate back to MyDiningActivity when back button is clicked
            Intent intent = new Intent(MenuActivity.this, MyDiningActivity.class);
            startActivity(intent);
        });

        // Initialize the userStats object using SharedPreferences
        userStats = new UserStats(MenuActivity.this);

        // Retrieve the slug passed from the previous activity
        String slug = getIntent().getStringExtra("slug");
        Log.d("MenuActivity", "Slug received: " + slug);

        // Fetch the menu data in a background thread
        new Thread(() -> {
            ArrayList<MenuItem> menuItems = MenuParser.fetchMenuData(slug);

            // Update the RecyclerView with the fetched data on the main thread
            runOnUiThread(() -> {
                if (menuItems != null && !menuItems.isEmpty()) {
                    if (menuAdapter == null) {
                        // Initialize the adapter if it hasn't been initialized yet
                        menuAdapter = new MenuAdapter(MenuActivity.this, menuItems, new MenuAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MenuItem menuItem) {
                                // Add the calories of the clicked menu item to the user's total
                                userStats.addCalories(menuItem.getTotalCal());
                                // Display a Toast message with the updated total calories
                                Toast.makeText(MenuActivity.this, "Total Calories: " + userStats.getTotalCalories(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(menuAdapter);
                    } else {
                        // Update the existing adapter with the new menu items
                        menuAdapter.updateMenuItems(menuItems);
                        recyclerView.setAdapter(menuAdapter);
                    }
                }
            });
        }).start();
    }
}
