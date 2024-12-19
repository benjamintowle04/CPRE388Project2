package com.example.getfit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfit.models.User;
import com.example.getfit.viewmodel.MainActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * SignupActivity is the activity that allows users to create a new account in the application.
 * It takes the user's name, email (as username), and password and uses Firebase Authentication
 * to create the user. After successful account creation, it stores the user data in Firestore.
 * The user is then redirected to the BioDataActivity to complete their profile.
 *
 * If any error occurs during registration or data storage, the user is shown an appropriate
 * error message.
 */
public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button signupButton;
    private Button goToLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;  // Firestore instance
    private MainActivityViewModel mViewModel;

    /**
     * Called when the activity is created. It initializes the UI elements,
     * sets up listeners for the signup button, and handles user registration logic.
     *
     * @param savedInstanceState A bundle containing any previously saved state of the activity, if applicable.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();  // Initialize Firestore

        // Initialize the views
        nameEditText = findViewById(R.id.signup_name_edt);
        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        signupButton = findViewById(R.id.signup_signup_btn);
        goToLogin = findViewById(R.id.go_to_login);

        // Handle signup button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String netID = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                // Validate inputs
                if (password.equals(confirm) && !netID.isEmpty()) {
                    // Create the new user in Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(netID, password)
                            .addOnCompleteListener(SignupActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Successfully created user, update profile
                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(
                                            new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name) // Set user's display name
                                                    .build()
                                    ).addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            // Store user data in Firestore
                                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            User user = new User(name, netID, 0, 0, "", 0); // Create a User object

                                            db.collection("users") // Reference to the 'users' collection
                                                    .document(userId)
                                                    .set(user)
                                                    .addOnCompleteListener(storeTask -> {
                                                        if (storeTask.isSuccessful()) {
                                                            // Navigate to BioDataActivity on successful signup and data save
                                                            Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(SignupActivity.this, BioDataActivity.class));
                                                            finish(); // Close SignupActivity
                                                        } else {
                                                            // If Firestore save fails, show an error
                                                            Toast.makeText(SignupActivity.this, "Failed to store user data", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                        } else {
                                            // If profile update fails, show an error
                                            Toast.makeText(SignupActivity.this, "Profile update failed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    // If registration fails, show an error
                                    Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    // Show error if password doesn't match or fields are empty
                    Toast.makeText(SignupActivity.this, "Passwords don't match or fields are empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Navigate to the login screen
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
