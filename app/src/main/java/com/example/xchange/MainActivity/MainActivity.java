package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Item;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.R;
import com.example.xchange.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private TextView usernameTextView;
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");


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
                // Handle Browse action
                return true;
            } else if (itemId == R.id.menu_search) {
                // Handle Search action
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Handle Profile action
                return true;
            } else {
                return false;
            }
        });
    }
}
