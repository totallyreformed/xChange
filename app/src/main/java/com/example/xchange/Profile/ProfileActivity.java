package com.example.xchange.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Counteroffer;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.ProfileData.AllItemsActivity;
import com.example.xchange.ProfileData.CounteroffersActivity;
import com.example.xchange.ProfileData.RequestsActivity;
import com.example.xchange.ProfileData.xChangesActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.User;
import com.example.xchange.xChange;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private TextView usernameTextView, emailTextView, userTypeTextView, locationTextView, statsTextView,
            requestsSentCountTextView, requestsReceivedCountTextView,
            counterOffersSentCountTextView, counterOffersReceivedCountTextView, totalExchangesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeUI();
        User user = retrieveUserFromIntent();
        if (user == null) {
            navigateToLogin();
            return;
        }
        initializeViewModel(user);
        observeViewModelData(user);
        setupNavigation(user);
        setupButtonListeners(user);
        loadInitialData();
    }

    private void initializeUI() {
        usernameTextView = findViewById(R.id.profileUsernameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        userTypeTextView = findViewById(R.id.profileUserTypeTextView);
        locationTextView = findViewById(R.id.profileLocationTextView);
        statsTextView = findViewById(R.id.profileStatsTextView);
        requestsSentCountTextView = findViewById(R.id.requestsSentCountTextView);
        requestsReceivedCountTextView = findViewById(R.id.requestsReceivedCountTextView);
        counterOffersSentCountTextView = findViewById(R.id.counterOffersSentCountTextView);
        counterOffersReceivedCountTextView = findViewById(R.id.counterOffersReceivedCountTextView);
        totalExchangesTextView = findViewById(R.id.totalExchangesTextView);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> navigateToLogin());
    }

    private User retrieveUserFromIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra("USER");
    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(ProfileActivity.this, com.example.xchange.Login.LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void initializeViewModel(User user) {
        ProfileViewModelFactory factory = new ProfileViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    private void observeViewModelData(User user) {
        viewModel.getUser().observe(this, userData -> {
            if (userData != null) {
                updateUserInfo(userData);
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

        viewModel.getSentRequestsCount().observe(this, count -> requestsSentCountTextView.setText(count + " Requests Sent"));

        viewModel.getReceivedRequestsCount().observe(this, count -> requestsReceivedCountTextView.setText(count + " Requests Received"));

        viewModel.getCounterOffersSentCount().observe(this, count -> counterOffersSentCountTextView.setText(count + " Counter Offers Sent"));

        viewModel.getCounterOffersReceivedCount().observe(this, count -> counterOffersReceivedCountTextView.setText(count + " Counter Offers Received"));

        viewModel.getUserXChanges().observe(this, xChanges -> totalExchangesTextView.setText(xChanges.size() + " xChanges"));
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInfo(User user) {
        usernameTextView.setText("Username: " + user.getUsername());
        emailTextView.setText("Email: " + user.getEmail());
        userTypeTextView.setText("User Type: " + user.getUser_type());
        locationTextView.setText("Location: " + user.getLocation());
    }

    private void setupNavigation(User user) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                navigateToActivity(MainActivity.class, user);
                return true;
            } else if (itemId == R.id.menu_search) {
                navigateToActivity(SearchActivity.class, user);
                return true;
            } else return itemId == R.id.menu_profile;
        });
    }

    private void navigateToActivity(Class<?> targetActivity, User user) {
        Intent intent = new Intent(ProfileActivity.this, targetActivity);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void setupButtonListeners(User user) {
        setupRequestsButtons(user);
        setupCounterOffersButtons(user);
        setupViewAllItemsButton(user);
        setupXChangesButton(user);
    }

    private void setupRequestsButtons(User user) {
        Button requestsSentButton = findViewById(R.id.requestsSentButton);
        Button requestsReceivedButton = findViewById(R.id.requestsReceivedButton);

        requestsSentButton.setOnClickListener(v -> navigateToRequestsActivity("SENT", user, viewModel.getRequestsSent().getValue()));
        requestsReceivedButton.setOnClickListener(v -> navigateToRequestsActivity("RECEIVED", user, viewModel.getRequestsReceived().getValue()));
    }

    private void navigateToRequestsActivity(String requestType, User user, List<Request> requests) {
        Intent intent = new Intent(ProfileActivity.this, RequestsActivity.class);
        intent.putExtra("REQUEST_TYPE", requestType);
        intent.putExtra("USER", user);
        intent.putParcelableArrayListExtra("REQUESTS", new ArrayList<>(requests));
        startActivity(intent);
    }

    private void setupCounterOffersButtons(User user) {
        Button counterOffersSentButton = findViewById(R.id.counterOffersSentButton);
        Button counterOffersReceivedButton = findViewById(R.id.counterOffersReceivedButton);

        counterOffersSentButton.setOnClickListener(v -> navigateToCounterOffersActivity("COUNTER_OFFERS_SENT", user, viewModel.getCounterOffersSent().getValue()));
        counterOffersReceivedButton.setOnClickListener(v -> navigateToCounterOffersActivity("COUNTER_OFFERS_RECEIVED", user, viewModel.getCounterOffersReceived().getValue()));
    }

    private void navigateToCounterOffersActivity(String requestType, User user, List<Counteroffer> counterOffers) {
        Intent intent = new Intent(ProfileActivity.this, CounteroffersActivity.class);
        intent.putExtra("REQUEST_TYPE", requestType);
        intent.putExtra("USER", user);
        intent.putParcelableArrayListExtra("COUNTEROFFERS", new ArrayList<>(counterOffers));
        startActivity(intent);
    }

    private void setupViewAllItemsButton(User user) {
        Button viewAllItemsButton = findViewById(R.id.viewAllItemsButton);
        viewAllItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AllItemsActivity.class);
            intent.putExtra("USER", user);
            intent.putParcelableArrayListExtra("ITEMS", new ArrayList<>(Objects.requireNonNull(viewModel.getUserItems().getValue())));
            startActivity(intent);
        });
    }

    private void setupXChangesButton(User user) {
        Button xChangesButton = findViewById(R.id.xChangesButton);
        xChangesButton.setOnClickListener(v -> {
            List<xChange> xChanges = viewModel.getUserXChanges().getValue();
            if (xChanges != null) {
                Intent intent = new Intent(ProfileActivity.this, xChangesActivity.class);
                intent.putExtra("USER", user);
                intent.putParcelableArrayListExtra("XCHANGES", new ArrayList<>(xChanges));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No xChanges available.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInitialData() {
        viewModel.loadProfileData();
        viewModel.loadUserItems();
        viewModel.loadRequests();
        viewModel.loadCounterOffers();
        viewModel.loadUserXChanges();
        viewModel.loadRequestsCount();
        viewModel.loadCounterOffersCount();
    }
}
