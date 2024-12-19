package com.example.getfit.util;

import android.content.Context;

import com.example.getfit.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Utility class responsible for managing user data in Firebase.
 * <p>
 * This class provides methods to fetch, update, and manage user data stored in Firestore.
 * It also handles user authentication using Firebase Auth. The class follows the singleton pattern
 * to ensure only one instance exists and to cache the current user.
 * </p>
 */
public class UserManager {
    private static UserManager instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;  // Cache the user data

    /**
     * Private constructor for initializing Firebase Auth and Firestore instances.
     *
     * @param context The context used to initialize Firebase services.
     */
    private UserManager(Context context) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Singleton pattern to ensure only one instance of UserManager exists.
     *
     * @param context The context used to initialize Firebase services.
     * @return The singleton instance of UserManager.
     */
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Fetches user data from Firestore for the currently authenticated user.
     * <p>
     * This method retrieves the user's name, email, height, weight, intensity level, and target weight
     * from Firestore and caches the data in the `currentUser` field.
     * </p>
     *
     * @return A Task that will resolve with the `User` object containing the user data, or an exception if there is no user signed in.
     */
    public Task<User> fetchUserData() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            return db.collection("users").document(userId).get()
                    .continueWith(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve fields individually
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String intensityLevel = document.getString("intensityLevel");

                                int height = document.getLong("height") != null
                                        ? document.getLong("height").intValue() : 0;

                                int weight = document.getLong("weight") != null
                                        ? document.getLong("weight").intValue() : 0;

                                int targetWeight = document.getLong("targetWeight") != null
                                        ? document.getLong("targetWeight").intValue() : 0;

                                User user = new User(name, email, height, weight, intensityLevel,
                                        targetWeight);

                                currentUser = user;
                                return currentUser;
                            } else {
                                throw new Exception("No such user document");
                            }
                        } else {
                            throw task.getException();
                        }
                    });
        } else {
            return Tasks.forException(new Exception("No user is signed in"));
        }
    }

    /**
     * Returns the cached user data.
     * <p>
     * This method returns the current user stored in the `currentUser` field. If the user data has not been fetched yet, it may return null.
     * </p>
     *
     * @return The cached `User` object, or null if no user data has been fetched yet.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Updates the user's bio data (name, height, weight, target weight, and intensity level) in Firestore.
     * <p>
     * This method creates an updated `User` object with the new values and stores it in the Firestore
     * database under the current user's document.
     * </p>
     *
     * @param name        The user's name.
     * @param height      The user's height.
     * @param weight      The user's weight.
     * @param targetWeight The user's target weight.
     * @param intensityLevel The user's intensity level.
     * @return A Task that will complete when the update is finished. It resolves to `null` on success.
     */
    public Task<Void> updateUserBioData(String name, int height, int weight, int targetWeight, String intensityLevel) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // Create a map of the new data to update
            User updatedUser = new User(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), height, weight, intensityLevel, targetWeight);

            // Create a Map to set the fields
            return db.collection("users").document(userId).set(updatedUser)
                    .continueWith(task -> {
                        if (task.isSuccessful()) {
                            currentUser = updatedUser;  // Update the currentUser cache
                            return null;
                        } else {
                            throw task.getException();  // Return any exceptions if the update fails
                        }
                    });
        } else {
            return Tasks.forException(new Exception("No user is signed in"));
        }
    }

    /**
     * Updates the user's settings data (name, height, weight, target weight, and intensity level) in Firestore.
     * <p>
     * This method allows for updating the settings-related user information (excluding the display name).
     * </p>
     *
     * @param name        The user's name.
     * @param height      The user's height.
     * @param weight      The user's weight.
     * @param targetWeight The user's target weight.
     * @param intensityLevel The user's intensity level.
     * @return A Task that will complete when the update is finished. It resolves to `null` on success.
     */
    public Task<Void> updateUserSettingsData(String name, int height, int weight, int targetWeight, String intensityLevel) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // Create a map of the new data to update
            User updatedUser = new User(name, mAuth.getCurrentUser().getEmail(), currentUser.getHeight(), weight, intensityLevel, targetWeight);

            // Create a Map to set the fields
            return db.collection("users").document(userId).set(updatedUser)
                    .continueWith(task -> {
                        if (task.isSuccessful()) {
                            currentUser = updatedUser;  // Update the currentUser cache
                            return null;
                        } else {
                            throw task.getException();  // Return any exceptions if the update fails
                        }
                    });
        } else {
            return Tasks.forException(new Exception("No user is signed in"));
        }
    }
}
