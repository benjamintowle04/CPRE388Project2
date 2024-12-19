package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The LoginActivity class is responsible for handling the user login process.
 * It allows the user to log in with their email and password, checks if the user is already signed in,
 * and redirects to the appropriate activity based on the login status.
 * <p>
 * If the user is already logged in, the activity redirects to the main activity. If the user is not logged in,
 * they can enter their credentials and attempt to sign in. If the sign-in is successful, the user is redirected
 * to the main activity. If the sign-in fails, an error message is displayed.
 * </p>
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button goToSignUp;

    /**
     * Called when the activity is created. Initializes the UI components and sets up click listeners
     * for the login and sign-up buttons. It also checks if the user is already signed in.
     *
     * @param savedInstanceState The saved instance state, if any, for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        goToSignUp = findViewById(R.id.login_forgotPassword_btn);

        // Check if the user is already signed in and redirect if needed
        checkUserAlreadySignedIn();

        // Set up the login button's click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the login button is clicked, start the sign-in process
                if (shouldStartSignIn()) {
                    startSignIn();
                }
            }
        });

        // Set up the sign-up button's click listener
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Redirect to the Sign-Up activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Attempts to sign in the user with the entered email and password.
     * If the sign-in is successful, the user is redirected to the main activity.
     */
    private void startSignIn() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            // Optionally show an error message if fields are empty
            return;
        }

        // Attempt to sign in the user with email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful, go to MainActivity
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            startMainActivity();
                        }
                    } else {
                        // Sign-in failed, show an error message (optional)
                        // You could use Toast, Snackbar, etc.
                    }
                });
    }

    /**
     * Checks if the user is already signed in. If so, redirects to the main activity.
     * This method is called when the activity is created.
     */
    private void checkUserAlreadySignedIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, go to MainActivity
            startMainActivity();
        }
    }

    /**
     * Starts the MainActivity when the user is successfully signed in.
     */
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MyHealthActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user can't navigate back to it
    }

    /**
     * Determines if the sign-in process should begin. This check ensures that the user is not already signed in.
     *
     * @return True if the user is not already signed in, false otherwise.
     */
    private boolean shouldStartSignIn() {
        // Only start sign-in if not already signed in
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }
}
