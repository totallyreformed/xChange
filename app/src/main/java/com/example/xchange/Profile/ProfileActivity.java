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
import com.example.xchange.xChanger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity class for displaying and managing the user's profile in the xChange application.
 * <p>
 * Displays user information, statistics, and allows navigation to other activities such as requests,
 * counter-offers, and exchanged items. Users can also log out or navigate through the app using the
 * bottom navigation bar.
 * </p>
 */
public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private TextView usernameTextView, emailTextView, userTypeTextView, ratingTextView, locationTextView, statsTextView,
            requestsSentCountTextView, requestsReceivedCountTextView,
            counterOffersSentCountTextView, counterOffersReceivedCountTextView, totalExchangesTextView;

    /**
     * Initializes the activity and its components.
     *
     * @param savedInstanceState The saved state of the activity.
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadUpdatedUser();
        viewModel.loadUserRating();
    }

    /**
     * Initializes UI components such as TextViews and buttons.
     */
    private void initializeUI() {
        usernameTextView = findViewById(R.id.profileUsernameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        userTypeTextView = findViewById(R.id.profileUserTypeTextView);
        ratingTextView = findViewById(R.id.profileRatingTextView);
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

    /**
     * Retrieves the user object passed through the intent.
     *
     * @return The User object, or null if not found.
     */
    private User retrieveUserFromIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra("USER");
    }

    /**
     * Navigates to the login activity and clears the current activity stack.
     */
    private void navigateToLogin() {
        Intent loginIntent = new Intent(ProfileActivity.this, com.example.xchange.Login.LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    /**
     * Initializes the ViewModel for managing profile data.
     *
     * @param user The user for whom the profile data is managed.
     */
    private void initializeViewModel(User user) {
        ProfileViewModelFactory factory = new ProfileViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
    }

    /**
     * Observes LiveData from the ViewModel to update the UI based on changes.
     *
     * @param user The current user.
     */
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

        viewModel.getRating().observe(this, rating -> ratingTextView.setText("Rating: " + rating));
    }

    /**
     * Updates the user information displayed in the profile.
     *
     * @param user The current user.
     */
    @SuppressLint("SetTextI18n")
    private void updateUserInfo(User user) {
        usernameTextView.setText("Username: " + user.getUsername());
        emailTextView.setText("Email: " + user.getEmail());
        userTypeTextView.setText("User Type: " + user.getUser_type());


        locationTextView.setText("Location: " + user.getLocation());
    }

    /**
     * Sets up the bottom navigation bar for navigation between activities.
     *
     * @param user The current user.
     */
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

    /**
     * Navigates to the specified activity.
     *
     * @param targetActivity The target activity class.
     * @param user           The current user.
     */
    private void navigateToActivity(Class<?> targetActivity, User user) {
        Intent intent = new Intent(ProfileActivity.this, targetActivity);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    /**
     * Sets up listeners for buttons related to requests, counter-offers, and items.
     *
     * @param user The current user.
     */
    private void setupButtonListeners(User user) {
        setupRequestsButtons(user);
        setupCounterOffersButtons(user);
        setupViewAllItemsButton(user);
        setupXChangesButton(user);
    }

    /**
     * Sets up listeners for requests-related buttons.
     *
     * @param user The current user.
     */
    private void setupRequestsButtons(User user) {
        Button requestsSentButton = findViewById(R.id.requestsSentButton);
        Button requestsReceivedButton = findViewById(R.id.requestsReceivedButton);

        requestsSentButton.setOnClickListener(v -> navigateToRequestsActivity("SENT", user, viewModel.getRequestsSent().getValue()));
        requestsReceivedButton.setOnClickListener(v -> navigateToRequestsActivity("RECEIVED", user, viewModel.getRequestsReceived().getValue()));
    }

    /**
     * Navigates to the RequestsActivity with the specified type of requests.
     *
     * @param requestType The type of requests (e.g., "SENT", "RECEIVED").
     * @param user        The current user.
     * @param requests    The list of requests.
     */
    private void navigateToRequestsActivity(String requestType, User user, List<Request> requests) {
        Intent intent = new Intent(ProfileActivity.this, RequestsActivity.class);
        intent.putExtra("REQUEST_TYPE", requestType);
        intent.putExtra("USER", user);
        intent.putParcelableArrayListExtra("REQUESTS", new ArrayList<>(requests));
        startActivity(intent);
    }

    /**
     * Sets up listeners for counter-offers-related buttons.
     *
     * @param user The current user.
     */
    private void setupCounterOffersButtons(User user) {
        Button counterOffersSentButton = findViewById(R.id.counterOffersSentButton);
        Button counterOffersReceivedButton = findViewById(R.id.counterOffersReceivedButton);

        counterOffersSentButton.setOnClickListener(v -> navigateToCounterOffersActivity("COUNTER_OFFERS_SENT", user, viewModel.getCounterOffersSent().getValue()));
        counterOffersReceivedButton.setOnClickListener(v -> navigateToCounterOffersActivity("COUNTER_OFFERS_RECEIVED", user, viewModel.getCounterOffersReceived().getValue()));
    }

    /**
     * Navigates to the CounteroffersActivity with the specified type of counter-offers.
     *
     * @param requestType   The type of counter-offers (e.g., "COUNTER_OFFERS_SENT", "COUNTER_OFFERS_RECEIVED").
     * @param user          The current user.
     * @param counterOffers The list of counter-offers.
     */
    private void navigateToCounterOffersActivity(String requestType, User user, List<Counteroffer> counterOffers) {
        Intent intent = new Intent(ProfileActivity.this, CounteroffersActivity.class);
        intent.putExtra("REQUEST_TYPE", requestType);
        intent.putExtra("USER", user);
        intent.putParcelableArrayListExtra("COUNTEROFFERS", new ArrayList<>(counterOffers));
        startActivity(intent);
    }

    /**
     * Sets up the listener for the button to view all items.
     *
     * @param user The current user.
     */
    private void setupViewAllItemsButton(User user) {
        Button viewAllItemsButton = findViewById(R.id.viewAllItemsButton);
        viewAllItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AllItemsActivity.class);
            intent.putExtra("USER", user);
            intent.putParcelableArrayListExtra("ITEMS", new ArrayList<>(Objects.requireNonNull(viewModel.getUserItems().getValue())));
            startActivity(intent);
        });
    }

    /**
     * Sets up the listener for the button to view xChanges.
     *
     * @param user The current user.
     */
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

    /**
     * Loads initial data for the profile such as statistics, items, and requests.
     */
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