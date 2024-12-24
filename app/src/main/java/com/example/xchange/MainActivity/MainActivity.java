package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.xchange.Upload.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Item;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.Profile.ProfileActivity;
import com.example.xchange.R;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private TextView usernameTextView;
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton uploadFab;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");

        // Initialize FAB
        uploadFab = findViewById(R.id.uploadFab);

        // Set click listener for upload fab
        uploadFab.setOnClickListener(v -> {
            // Retrieve current user from intent or session
            User currentUser = intent.getParcelableExtra("USER");
            if (currentUser == null) {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to UploadActivity, passing the current user
            Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
            uploadIntent.putExtra("USER", currentUser);
            startActivity(uploadIntent);
        });

        // Initialize Views
        usernameTextView = findViewById(R.id.usernameTextView);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);

        // Set up RecyclerView with Adapter
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>()); // Initialize with an empty list
        itemsRecyclerView.setAdapter(itemsAdapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        assert user != null;
        usernameTextView.setText("Welcome "+user.getUsername().toUpperCase()+" !");

        viewModel.getItemsList().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
        });


        // BottomNavigationView setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                // Currently in MainActivity, no action needed
                return true;
            } else if (itemId == R.id.menu_search) {
                // Navigate to SearchActivity
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra("USER", user); // Pass the current User object
                startActivity(searchIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER", user); // Pass the current User object
                startActivity(profileIntent);
                return true;
            } else {
                return false;
            }
        });
    }
}
