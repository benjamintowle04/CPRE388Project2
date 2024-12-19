package com.example.getfit.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.BuildConfig;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.auth.AuthUI;

/**
 * Utility class for initializing Firebase services and connecting them to the Firebase Emulator
 * Suite if necessary.
 * <p>
 * This class ensures that Firebase services are properly initialized and configured to connect
 * either to the Firebase Cloud services or to local Firebase Emulator instances (for development and testing).
 * </p>
 */
public class FirebaseUtil {

    /** Flag to use emulators only in debug builds. */
    private static final boolean sUseEmulators = false;

    // Firebase service instances
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private static AuthUI AUTH_UI;

    /**
     * Returns the instance of FirebaseFirestore. If not initialized, it creates a new instance.
     * It also connects to the Firestore emulator if the flag sUseEmulators is true.
     *
     * @return the FirebaseFirestore instance
     */
    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();

            // Connect to the Cloud Firestore emulator when appropriate
            if (sUseEmulators) {
                FIRESTORE.useEmulator("10.0.2.2", 8080); // The IP 10.0.2.2 connects to localhost on Android Emulator
            }
        }

        return FIRESTORE;
    }

    /**
     * Returns the instance of FirebaseAuth. If not initialized, it creates a new instance.
     * It also connects to the Firebase Auth emulator if the flag sUseEmulators is true.
     *
     * @return the FirebaseAuth instance
     */
    public static FirebaseAuth getAuth() {
        if (AUTH == null) {
            AUTH = FirebaseAuth.getInstance();

            // Connect to the Firebase Auth emulator when appropriate
            if (sUseEmulators) {
                AUTH.useEmulator("10.0.2.2", 9099); // The IP 10.0.2.2 connects to localhost on Android Emulator
            }
        }

        return AUTH;
    }

    /**
     * Returns the instance of AuthUI. If not initialized, it creates a new instance.
     * It also connects to the Firebase Auth emulator if the flag sUseEmulators is true.
     *
     * @return the AuthUI instance
     */
    public static AuthUI getAuthUI() {
        if (AUTH_UI == null) {
            AUTH_UI = AuthUI.getInstance();

            // Connect to the Firebase Auth emulator when appropriate
            if (sUseEmulators) {
                AUTH_UI.useEmulator("10.0.2.2", 9099); // The IP 10.0.2.2 connects to localhost on Android Emulator
            }
        }

        return AUTH_UI;
    }

}
