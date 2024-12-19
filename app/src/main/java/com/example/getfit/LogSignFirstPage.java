package com.example.getfit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The LogSignFirstPage activity serves as the main entry point for the user.
 * It provides two buttons: one to navigate to the login page and another to navigate to the signup page.
 * This activity also displays a welcome message or other initial information for the user.
 * <p>
 * This page is the first page shown to the user after launching the app, and it provides options
 * for the user to either log in or sign up for the app.
 * </p>
 */
public class LogSignFirstPage extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private TextView messageText;  // Displays a message to the user

    /**
     * Called when the activity is created. It sets up the layout, initializes the buttons,
     * and sets their click listeners to navigate to the appropriate activities.
     *
     * @param savedInstanceState The saved instance state, if any, for the activity.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        // Initialize UI elements
        messageText = findViewById(R.id.main_msg_txt);
        loginButton = findViewById(R.id.main_login_btn);
        signupButton = findViewById(R.id.main_signup_btn);

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the LoginActivity when login button is clicked
                startActivity(new Intent(LogSignFirstPage.this, LoginActivity.class));
            }
        });

        // Set up the signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the SignupActivity when signup button is clicked
                startActivity(new Intent(LogSignFirstPage.this, SignupActivity.class));
            }
        });

    }
}
