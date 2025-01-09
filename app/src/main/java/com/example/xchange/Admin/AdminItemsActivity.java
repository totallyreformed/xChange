package com.example.xchange.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Item;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import androidx.lifecycle.Observer;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class AdminItemsActivity extends AppCompatActivity {

    private RecyclerView adminItemsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private UserRepository userRepository;
    private User currentUser; // Admin user

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_items);

        // Initialize Toolbar (Optional)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.adminItemsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hides the title text
        }

        // Initialize RecyclerView
        adminItemsRecyclerView = findViewById(R.id.adminItemsRecyclerView);
        adminItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminItemsRecyclerView.setHasFixedSize(true);

        // Initialize Adapter with empty list and currentUser
        // Assuming Admin is a type of User; replace with actual admin user retrieval if necessary
        currentUser = getAdminUser(); // Implement this method based on your app's logic
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser);
        adminItemsRecyclerView.setAdapter(itemsAdapter);

        // Set OnItemClickListener
        itemsAdapter.setOnItemClickListener(itemId -> {
            // Navigate to ItemDetailActivity
            Intent intent = new Intent(AdminItemsActivity.this, ItemDetailActivity.class);
            intent.putExtra("ITEM_ID", itemId);
            intent.putExtra("USER", currentUser); // Pass Admin user if needed
            startActivity(intent);
        });

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Observe LiveData from getAllItems()
        LiveData<List<Item>> itemsLiveData = userRepository.getAllItems();
        itemsLiveData.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null && !items.isEmpty()) {
                    itemsAdapter.setItems(items);
                } else {
                    Toast.makeText(AdminItemsActivity.this, "No items found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Optional: Implement method to retrieve Admin user
    private User getAdminUser() {
        // Retrieve the Admin user from SharedPreferences, Intent, or any other method you use
        // For example:
        // return getIntent().getParcelableExtra("USER");
        return null; // Replace with actual retrieval logic
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userRepository.shutdownExecutor(); // Ensure to shutdown the executor to prevent leaks
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

