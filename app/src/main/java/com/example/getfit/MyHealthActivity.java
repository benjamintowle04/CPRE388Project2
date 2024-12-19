package com.example.getfit;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfit.models.User;
import com.example.getfit.models.UserStats;
import com.example.getfit.util.UserManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

/**
 * The MyHealthActivity displays the user's health dashboard, including navigation buttons
 * for fitness and dining sections, a check-in button to earn points, and the ability to sign out.
 * It also shows a personalized greeting, a random motivational quote, and the user's accumulated points.
 *
 * This activity interacts with the UserStats class to manage points and check-ins,
 * and uses FirebaseAuth to handle user sign-out.
 */
public class MyHealthActivity extends AppCompatActivity {

    private Button myFitnessButton;
    private Button myDiningButton;
    private Button signOutButton;  // Button to sign out the user
    private TextView userGreeting;
    private TextView motivationalQuote;
    private TextView pointsTextView;  // Displays the total points of the user
    private ImageButton checkInButton;
    private UserStats userStats;  // Manages user's points and check-in data

    /**
     * Called when the activity is created. It sets up the user interface, including buttons,
     * and initializes the user's data, motivational quote, and check-in functionality.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhealth);

        // Initialize UserStats and UI components
        userStats = new UserStats(this);
        myFitnessButton = findViewById(R.id.myhealth_myfitness_btn);
        myDiningButton = findViewById(R.id.myhealth_mydining_btn);
        signOutButton = findViewById(R.id.signout_button);
        userGreeting = findViewById(R.id.greeting_text);
        motivationalQuote = findViewById(R.id.motivational_quote_text);
        pointsTextView = findViewById(R.id.points_text);
        checkInButton = findViewById(R.id.check_in_button);

        // Display the points and set up the check-in functionality
        updatePointsText();

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userStats.hasCheckedInToday()) {
                    // Add 20 points for a successful check-in
                    userStats.addPoints(20);
                    userStats.updateCheckInDate();

                    // Update the points display
                    updatePointsText();

                    // Optionally save the points in Firestore or other persistent storage
                    Toast.makeText(MyHealthActivity.this, "You've earned 20 points for today!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyHealthActivity.this, "You've already checked in today!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navigate to the User Settings page when the user icon is clicked
        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHealthActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to the MyFitnessActivity
        myFitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHealthActivity.this, MyFitnessActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to the MyDiningActivity
        myDiningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHealthActivity.this, MyDiningActivity.class);
                startActivity(intent);
            }
        });

        // Sign out the user when the sign-out button is clicked
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });

        // Set a random motivational quote
        setRandomMotivationalQuote();

        // Fetch and display user data (greeting)
        setUserDataOnScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data each time the activity is resumed
        setUserDataOnScreen();
    }

    /**
     * Fetches the user's data using UserManager and displays it on the screen.
     * The user's name is displayed in the greeting text.
     */
    public void setUserDataOnScreen() {
        UserManager.getInstance(this).fetchUserData()
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            userGreeting.setText("Hello " + user.getName());
                        } else {
                            Toast.makeText(MyHealthActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MyHealthActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Signs the user out from Firebase and navigates to the login page.
     */
    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();  // Signs out the user
        Toast.makeText(MyHealthActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to the login screen
        Intent intent = new Intent(MyHealthActivity.this, LogSignFirstPage.class);
        startActivity(intent);
        finish();  // Close the current activity
    }

    /**
     * Sets a random motivational quote on the screen.
     */
    private void setRandomMotivationalQuote() {
        Resources res = getResources();
        String[] quotes = res.getStringArray(R.array.motivational_quotes);

        Random random = new Random();
        int randomIndex = random.nextInt(quotes.length);

        motivationalQuote.setText(quotes[randomIndex]);
    }

    /**
     * Updates the points display on the screen.
     */
    private void updatePointsText() {
        pointsTextView.setText("Points: " + userStats.getTotalPoints());
    }
}
