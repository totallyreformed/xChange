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

public class AllItemsActivity extends AppCompatActivity {

    private RecyclerView allItemsRecyclerView;
    private Button backButton;
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        initializeUI();
        setupRecyclerView();
        handleIntentData();
        setupBackButton();
    }

    private void initializeUI() {
        allItemsRecyclerView = findViewById(R.id.allItemsRecyclerView);
        backButton = findViewById(R.id.backToProfileButton);
    }

    private void setupRecyclerView() {
        allItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), null);
        allItemsRecyclerView.setAdapter(itemsAdapter);
    }

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

    private void setupItemClickListener(User user) {
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", user);
            startActivity(detailIntent);
        });
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}