package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.xchange.MainActivity.AdminHomeFragment;
import com.example.xchange.MainActivity.XChangerHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton uploadFab;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        uploadFab = findViewById(R.id.uploadFab);

        currentUser = intent.getParcelableExtra("USER");

        if (currentUser != null) {
            String userType = currentUser.getUser_type();
            if ("admin".equalsIgnoreCase(userType)) {
                // Load AdminHomeFragment into the fragment container
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AdminHomeFragment())
                        .commit();
            } else if ("xChanger".equalsIgnoreCase(userType)) {
                // Load XChangerHomeFragment into the fragment container
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new XChangerHomeFragment())
                        .commit();
            } else {
                Toast.makeText(this, "Unknown user type", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            finish();
        }

        uploadFab.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
            uploadIntent.putExtra("USER", currentUser);
            startActivity(uploadIntent);
        });



        // Use the custom factory to instantiate the ViewModel
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);

        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser);
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", currentUser);
            startActivity(detailIntent);
        });

        assert currentUser != null;

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
                searchIntent.putExtra("USER", currentUser);
                startActivity(searchIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER", currentUser);
                startActivity(profileIntent);
                return true;
            } else {
                return false;
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }
}