package com.example.xchange.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Category;

import com.example.xchange.ItemsAdapter;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.Profile.ProfileActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * Activity class for performing item searches in the xChange application.
 * <p>
 * This class allows users to search for items by query and category, view search results,
 * and navigate between different sections of the application.
 * </p>
 */
public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private Button searchButton, clearButton;
    private RecyclerView searchResultsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private User user;

    /**
     * Initializes the activity, sets up the UI components, and observes the ViewModel.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeUI();
        initializeUser();
        initializeViewModel();
        setupCategorySpinner();
        setupRecyclerView();
        observeViewModel();
        setupButtonListeners();
        setupBottomNavigation();
    }

    /**
     * Initializes the UI components of the activity.
     */
    private void initializeUI() {
        searchEditText = findViewById(R.id.searchEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
    }

    /**
     * Initializes the user data from the intent.
     * If user data is not found, navigates to the login activity.
     */
    private void initializeUser() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        if (user == null) {
            showToast("User data not found");
            navigateToLogin();
        }
    }

    /**
     * Initializes the ViewModel for managing search-related data and logic.
     */
    private void initializeViewModel() {
        SearchViewModelFactory factory = new SearchViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
    }

    /**
     * Sets up the category spinner with the list of categories.
     */
    private void setupCategorySpinner() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Category.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    /**
     * Sets up the RecyclerView for displaying search results.
     */
    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), user);
        searchResultsRecyclerView.setAdapter(itemsAdapter);
    }

    /**
     * Observes the LiveData objects in the ViewModel for search results and errors.
     */
    private void observeViewModel() {
        viewModel.getSearchResults().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                showToast("No items found.");
                itemsAdapter.setItems(new ArrayList<>());
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                showToast(errorMessage);
            }
        });
    }

    /**
     * Sets up the click listeners for the search and clear buttons.
     */
    private void setupButtonListeners() {
        searchButton.setOnClickListener(v -> performSearch());
        clearButton.setOnClickListener(v -> clearSearch());
    }

    /**
     * Performs the search based on the query and selected category.
     */
    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        String selectedCategoryName = categorySpinner.getSelectedItem().toString();
        Category selectedCategory = Category.fromDisplayName(selectedCategoryName);

        if (query.isEmpty() && selectedCategory == Category.ALL) {
            showToast("Please enter a search query or select a category.");
            return;
        }

        Category categoryFilter = (selectedCategory == Category.ALL) ? null : selectedCategory;
        viewModel.searchItems(query, categoryFilter);
    }

    /**
     * Clears the search inputs and results.
     */
    private void clearSearch() {
        searchEditText.setText("");
        categorySpinner.setSelection(0);
        itemsAdapter.setItems(new ArrayList<>());
        showToast("Search cleared.");
    }

    /**
     * Sets up the bottom navigation view and its item selection behavior.
     */
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNavigationSelection);
    }

    /**
     * Handles navigation item selection in the bottom navigation view.
     *
     * @param item The selected menu item.
     * @return True if the selection was handled, false otherwise.
     */
    private boolean handleBottomNavigationSelection(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_browse) {
            navigateToActivity(MainActivity.class);
            return true;
        } else if (itemId == R.id.menu_search) {
            return true;
        } else if (itemId == R.id.menu_profile) {
            navigateToActivity(ProfileActivity.class);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Navigates to the specified activity, passing the current user data.
     *
     * @param activityClass The activity to navigate to.
     */
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    /**
     * Navigates to the login activity and clears the current task stack.
     */
    private void navigateToLogin() {
        Intent loginIntent = new Intent(this, com.example.xchange.Login.LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    /**
     * Displays a toast message to the user.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}