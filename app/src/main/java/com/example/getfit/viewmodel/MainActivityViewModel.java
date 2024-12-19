package com.example.getfit.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.drive.query.Filters;

/**
 * ViewModel class for managing UI-related data in the MainActivity.
 * <p>
 * This class is used to hold and manage the data for the MainActivity. It is responsible for
 * storing the state of whether the user is signing in and any other relevant filters.
 * </p>
 */
public class MainActivityViewModel extends ViewModel {

    // Flag indicating whether the user is signing in
    private boolean mIsSigningIn;

    // Placeholder for filters (currently not used in the code)
    private Filters mFilters;

    /**
     * Constructor initializing the ViewModel.
     * <p>
     * Initializes the `mIsSigningIn` flag to `false` to represent the initial state where
     * the user is not signing in.
     * </p>
     */
    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    /**
     * Getter for the `mIsSigningIn` flag.
     * <p>
     * This method retrieves the current value of the flag indicating whether the user
     * is in the process of signing in.
     * </p>
     *
     * @return A boolean value indicating if the user is signing in (`true`) or not (`false`).
     */
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    /**
     * Setter for the `mIsSigningIn` flag.
     * <p>
     * This method allows updating the state of the signing-in process, i.e., whether the user is
     * currently signing in or not.
     * </p>
     *
     * @param mIsSigningIn A boolean value to set the state of the signing-in process.
     */
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
