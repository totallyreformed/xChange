package com.example.xchange.ProfileData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Item;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.R;
import com.example.xchange.User;

import java.util.ArrayList;

/**
 * Activity class for displaying all items associated with a user in the xChange application.
 * <p>
 * This activity shows a list of items in a RecyclerView and allows navigation to item details.
 * It also provides a back button to return to the previous screen.
 * </p>
 */
public class AllItemsActivity extends AppCompatActivity {

    private RecyclerView allItemsRecyclerView;
    private Button backButton;
    private ItemsAdapter itemsAdapter;

    /**
     * Initializes the activity, sets up UI components, and handles intent data.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        initializeUI();
        setupRecyclerView();
        handleIntentData();
        setupBackButton();
    }

    /**
     * Initializes UI components such as the RecyclerView and the back button.
     */
    private void initializeUI() {
        allItemsRecyclerView = findViewById(R.id.allItemsRecyclerView);
        backButton = findViewById(R.id.backToProfileButton);
    }

    /**
     * Configures the RecyclerView with a layout manager and an adapter.
     */
    private void setupRecyclerView() {
        allItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), null);
        allItemsRecyclerView.setAdapter(itemsAdapter);
    }

    /**
     * Handles intent data to populate the RecyclerView with items.
     * If no items are provided, displays a message and finishes the activity.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void handleIntentData() {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");
        ArrayList<Item> items = intent.getParcelableArrayListExtra("ITEMS");

        if (items == null || items.isEmpty()) {
            showToast("No items available");
            finish();
            return;
        }

        itemsAdapter.setItems(items);
        itemsAdapter.notifyDataSetChanged();

        setupItemClickListener(user);
    }

    /**
     * Sets up an item click listener for navigating to the item detail screen.
     *
     * @param user The user associated with the items.
     */
    private void setupItemClickListener(User user) {
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", user);
            startActivity(detailIntent);
        });
    }

    /**
     * Configures the back button to finish the activity and return to the previous screen.
     */
    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Displays a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}