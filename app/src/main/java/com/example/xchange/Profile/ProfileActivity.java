package com.example.xchange.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.ProfileData.AllItemsActivity;
import com.example.xchange.ProfileData.RequestsActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private TextView usernameTextView, emailTextView, userTypeTextView, locationTextView, statsTextView, requestsSentCountTextView, requestsReceivedCountTextView, counterOffersSentCountTextView, counterOffersReceivedCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        usernameTextView = findViewById(R.id.profileUsernameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        userTypeTextView = findViewById(R.id.profileUserTypeTextView);
        locationTextView = findViewById(R.id.profileLocationTextView);
        statsTextView = findViewById(R.id.profileStatsTextView);
        requestsSentCountTextView = findViewById(R.id.requestsSentCountTextView);
        requestsReceivedCountTextView = findViewById(R.id.requestsReceivedCountTextView);
        counterOffersSentCountTextView = findViewById(R.id.counterOffersSentCountTextView);
        counterOffersReceivedCountTextView = findViewById(R.id.counterOffersReceivedCountTextView);

        // Initialize Logout Button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(ProfileActivity.this, com.example.xchange.Login.LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(loginIntent);
            finish(); // Close the current activity
        });

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");

        if (user == null) {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
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
                userTypeTextView.setText("User Type: " + userData.getUser_type());
                locationTextView.setText("Location: " + userData.getLocation());
            }
        });

        viewModel.getUserStatistics().observe(this, stats -> {
            if (stats != null) {
                statsTextView.setText(stats);
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe LiveData for requests and counters
        viewModel.getSentRequestsCount().observe(this, count -> {
            requestsSentCountTextView.setText(count + " Requests Sent");
        });

        viewModel.getReceivedRequestsCount().observe(this, count -> {
            requestsReceivedCountTextView.setText(count + " Requests Received");
        });

        viewModel.getCounterOffersSentCount().observe(this, count -> {
            counterOffersSentCountTextView.setText(count + " Counter Offers Sent");
        });

        viewModel.getCounterOffersReceivedCount().observe(this, count -> {
            counterOffersReceivedCountTextView.setText(count + " Counter Offers Received");
        });

        // Navigate to AllItemsActivity
        Button viewAllItemsButton = findViewById(R.id.viewAllItemsButton);
        viewAllItemsButton.setOnClickListener(v -> {
            Intent allItemsIntent = new Intent(ProfileActivity.this, AllItemsActivity.class);
            allItemsIntent.putExtra("USER", user);
            allItemsIntent.putParcelableArrayListExtra("ITEMS", new ArrayList<>(viewModel.getUserItems().getValue()));
            startActivity(allItemsIntent);
        });

        ArrayList<Request> sentRequests = new ArrayList<>();
        ArrayList<Request> receivedRequests = new ArrayList<>();
        viewModel.loadRequests();

        viewModel.getRequestsSent().observe(this, sent -> {
            if (sent != null) {
                sentRequests.clear();
                sentRequests.addAll(sent);
            }
        });

        viewModel.getRequestsReceived().observe(this, received -> {
            if (received != null) {
                receivedRequests.clear();
                receivedRequests.addAll(received);
            }
        });

        Button requestsSent = findViewById(R.id.requestsSentButton);
        Button requestsReceived = findViewById(R.id.requestsReceivedButton);

        requestsSent.setOnClickListener(v -> {
            Intent showRequestsSent = new Intent(ProfileActivity.this, RequestsActivity.class);
            showRequestsSent.putExtra("REQUEST_TYPE", "SENT");
            showRequestsSent.putExtra("USER", user);
            showRequestsSent.putParcelableArrayListExtra("REQUESTS", sentRequests);
            startActivity(showRequestsSent);
        });

        requestsReceived.setOnClickListener(v -> {
            Intent showRequestsReceived = new Intent(ProfileActivity.this, RequestsActivity.class);
            showRequestsReceived.putExtra("REQUEST_TYPE", "RECEIVED");
            showRequestsReceived.putParcelableArrayListExtra("REQUESTS", receivedRequests);
            showRequestsReceived.putExtra("USER", user);
            startActivity(showRequestsReceived);
        });

        viewModel.loadProfileData();
        viewModel.loadUserItems();
        viewModel.loadRequestsCount();
        viewModel.loadCounterOffersCount();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                Intent browseIntent = new Intent(ProfileActivity.this, MainActivity.class);
                browseIntent.putExtra("USER", user);
                startActivity(browseIntent);
                return true;

            } else if (itemId == R.id.menu_search) {
                Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
                searchIntent.putExtra("USER", user);
                startActivity(searchIntent);
                return true;

            } else if (itemId == R.id.menu_profile) {
                return true;
            } else {
                return false;
            }
        });
    }
}
