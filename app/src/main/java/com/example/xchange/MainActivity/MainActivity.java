package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.Upload.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        uploadFab = findViewById(R.id.uploadFab);

        User currentUser = intent.getParcelableExtra("USER");
        uploadFab.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
            uploadIntent.putExtra("USER", currentUser);
            startActivity(uploadIntent);
        });

        // Initialize Views
        usernameTextView = findViewById(R.id.usernameTextView);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setNestedScrollingEnabled(true);

        // Set up RecyclerView with Adapter
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser); // Περνάμε τον currentUser στον Adapter
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", currentUser); // Χρήση του currentUser
            startActivity(detailIntent);
        });

        assert currentUser != null;
        usernameTextView.setText("Welcome "+currentUser.getUsername().toUpperCase()+" !");

        itemsRecyclerView.setAdapter(itemsAdapter);
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
                searchIntent.putExtra("USER", currentUser); // Pass the current User object
                startActivity(searchIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER", currentUser); // Pass the current User object
                startActivity(profileIntent);
                return true;
            } else {
                return false;
            }
        });
    }
}
