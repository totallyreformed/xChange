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

import com.example.xchange.Item;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private Button searchButton;
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

        // Set up Spinner for category filter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set up RecyclerView with Adapter
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>());
        searchResultsRecyclerView.setAdapter(itemsAdapter);

        // Initialize ViewModel with Factory
        SearchViewModelFactory factory = new SearchViewModelFactory(getApplication());
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
            String category = categorySpinner.getSelectedItem().toString();

            if (query.isEmpty() && category.equals("All")) {
                Toast.makeText(this, "Please enter a search query or select a category.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category.equals("All")) {
                category = ""; // No category filter
            }

            viewModel.searchItems(query, category);
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
                overridePendingTransition(0, 0); // Optional: Remove transition animation
                return true;

            } else if (itemId == R.id.menu_search) {
                // Currently in SearchActivity, no action needed
                return true;

            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(SearchActivity.this, com.example.xchange.Profile.ProfileActivity.class);
                profileIntent.putExtra("USER", user); // Pass the current User object
                startActivity(profileIntent);
                overridePendingTransition(0, 0); // Optional: Remove transition animation
                return true;
            } else {
                return false;
            }
        });
    }
}
