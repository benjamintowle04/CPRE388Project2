package com.example.getfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.getfit.databinding.ActivityMainBinding;
import com.example.getfit.models.User;
import com.example.getfit.util.UserManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView userNameTextView;  // Example TextView to display the user's name

    private TextView netID_disp;  // Declare the TextView to update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and set the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get a reference to the NavigationView after the layout is set
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Access the header view in the NavigationView
        View headerView = navigationView.getHeaderView(0);  // 0 refers to the first header in the NavigationView
        netID_disp = headerView.findViewById(R.id.netid_disp);  // Initialize the TextView

        // Set user data on screen
        setUserDataOnScreen();

        // Setup toolbar and floating action button
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        // Set up the navigation drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    // Method to fetch user data and display it
    public void setUserDataOnScreen() {
        UserManager.getInstance(this).fetchUserData()
                .addOnSuccessListener(new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            // Update the TextView with the user's email
                            netID_disp.setText(user.getEmail() + "@iastate.edu");
                            Log.d("MainActivity", "Fetched email: " + user.getEmail());  // Log the fetched email
                        } else {
                            Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_sign_out) {
            signOutUser();
            return true;
        } else if (id == R.id.nav_my_health) {
            // Start the new MyHealthActivity
            Intent intent = new Intent(MainActivity.this, MyHealthActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Sign out the user from Firebase Auth and navigate back to the login screen
    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();  // Sign out the user
        Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Navigate back to the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);  // Replace with your login activity
        startActivity(intent);
        finish();  // Close the current activity
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
