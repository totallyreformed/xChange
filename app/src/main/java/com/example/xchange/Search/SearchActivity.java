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

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private Button searchButton, clearButton;
    private RecyclerView searchResultsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private User user;

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

    private void initializeUI() {
        searchEditText = findViewById(R.id.searchEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
    }

    private void initializeUser() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        if (user == null) {
            showToast("User data not found");
            navigateToLogin();
        }
    }

    private void initializeViewModel() {
        SearchViewModelFactory factory = new SearchViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Category.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), user);
        searchResultsRecyclerView.setAdapter(itemsAdapter);
    }

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

    private void setupButtonListeners() {
        searchButton.setOnClickListener(v -> performSearch());
        clearButton.setOnClickListener(v -> clearSearch());
    }

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

    private void clearSearch() {
        searchEditText.setText("");
        categorySpinner.setSelection(0);
        itemsAdapter.setItems(new ArrayList<>());
        showToast("Search cleared.");
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNavigationSelection);
    }

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

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(this, com.example.xchange.Login.LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
