// File: ProfileActivity.java
package com.example.xchange.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemsAdapter;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
//import com.example.xchange.Search.SearchActivity;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private TextView usernameTextView, emailTextView, userTypeTextView, locationTextView, statsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure this layout file exists

        // Add this after the BottomNavigationView setup in onCreate
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(ProfileActivity.this, com.example.xchange.Login.LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(loginIntent);
            finish(); // Close the current activity
        });

        // Initialize UI elements
        usernameTextView = findViewById(R.id.profileUsernameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        userTypeTextView = findViewById(R.id.profileUserTypeTextView);
        locationTextView = findViewById(R.id.profileLocationTextView);
        statsTextView = findViewById(R.id.profileStatsTextView);
        RecyclerView userItemsRecyclerView = findViewById(R.id.userItemsRecyclerView);
        userItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter userItemsAdapter = new ItemsAdapter(new ArrayList<>());
        userItemsRecyclerView.setAdapter(userItemsAdapter);

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");

        if (user == null) {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to LoginActivity if user data is missing
            Intent loginIntent = new Intent(ProfileActivity.this, com.example.xchange.Login.LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize ViewModel with Factory
        ProfileViewModelFactory factory = new ProfileViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        // Observe LiveData for profile data
        viewModel.getUser().observe(this, userData -> {
            if (userData != null) {
                usernameTextView.setText("Username: " + userData.getUsername());
                emailTextView.setText("Email: " + userData.getEmail());
                userTypeTextView.setText("User Type: " + userData.getUser_type()); // Adjusted method name
                locationTextView.setText("Location: " + userData.getLocation());
            }
        });

        viewModel.getUserStatistics().observe(this, stats -> {
            if (stats != null) {
                statsTextView.setText(stats);
            }
        });

        // Observe LiveData for user items
        viewModel.getUserItems().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                userItemsAdapter.setItems(items);
            } else {
                // Optionally handle empty state
                Toast.makeText(this, "No items uploaded.", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Request Profile data
        viewModel.loadProfileData();
        viewModel.loadProfileData();
        viewModel.loadUserItems();

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set the selected item to Profile to highlight it in the navigation bar
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        // Set up OnItemSelectedListener for navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                // Navigate to MainActivity (Browse section)
                Intent browseIntent = new Intent(ProfileActivity.this, MainActivity.class);
                browseIntent.putExtra("USER", user); // Pass the current User object
                startActivity(browseIntent);
                return true;

            } else if (itemId == R.id.menu_search) {
                // Navigate to SearchActivity (Assuming you have a SearchActivity)
                Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
                searchIntent.putExtra("USER", user); // Pass the current User object
                startActivity(searchIntent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                // Currently in ProfileActivity, no action needed
                return true;
            } else {
                return false;
            }
        });
    }


}
