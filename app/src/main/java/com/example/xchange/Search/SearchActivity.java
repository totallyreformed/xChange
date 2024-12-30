// File: SearchActivity.java
package com.example.xchange.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.Profile.ProfileActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private Button searchButton, clearButton;
    private RecyclerView searchResultsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private User user; // Assuming you have a User class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search); // Ensure activity_search.xml exists

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        if (user == null) {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to LoginActivity if user data is missing
            Intent loginIntent = new Intent(SearchActivity.this, com.example.xchange.Login.LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Set up Spinner for category filter using the enum
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set up RecyclerView with Adapter
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>(),user);
        searchResultsRecyclerView.setAdapter(itemsAdapter);

        // Initialize ViewModel with Factory
        SearchViewModelFactory factory = new SearchViewModelFactory(getApplication(), user);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        // Observe LiveData for search results
        viewModel.getSearchResults().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                Toast.makeText(this, "No items found.", Toast.LENGTH_SHORT).show();
                itemsAdapter.setItems(new ArrayList<>());
            }
        });

        // Observe LiveData for errors
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up search button click listener
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            String selectedCategoryName = categorySpinner.getSelectedItem().toString();
            Category selectedCategory = Category.fromDisplayName(selectedCategoryName);

            if (query.isEmpty() && selectedCategory == Category.ALL) {
                Toast.makeText(this, "Please enter a search query or select a category.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass null if Category.ALL is selected, else pass the selected Category
            Category categoryFilter = (selectedCategory == Category.ALL) ? null : selectedCategory;

            viewModel.searchItems(query, categoryFilter);
        });

        // Set up clear button click listener
        clearButton.setOnClickListener(v -> {
            // Clear search input
            searchEditText.setText("");
            // Reset category spinner to "All"
            categorySpinner.setSelection(0);
            // Clear search results
            itemsAdapter.setItems(new ArrayList<>());
            Toast.makeText(this, "Search cleared.", Toast.LENGTH_SHORT).show();
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set the selected item to Search to highlight it in the navigation bar
        bottomNavigationView.setSelectedItemId(R.id.menu_search);

        // Set up OnItemSelectedListener for navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                // Navigate to MainActivity (Browse section)
                Intent browseIntent = new Intent(SearchActivity.this, MainActivity.class);
                browseIntent.putExtra("USER", user); // Pass the current User object
                startActivity(browseIntent);
                return true;

            } else if (itemId == R.id.menu_search) {
                // Currently in SearchActivity, no action needed
                return true;

            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER", user); // Pass the current User object
                startActivity(profileIntent);
                return true;
            } else {
                return false;
            }
        });
    }
}