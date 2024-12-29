package com.example.xchange.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        RecyclerView allItemsRecyclerView = findViewById(R.id.allItemsRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);

        // Set up RecyclerView
        allItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter itemsAdapter = new ItemsAdapter(new ArrayList<>()); // Create adapter with empty list
        allItemsRecyclerView.setAdapter(itemsAdapter);

        // Retrieve items and user from intent
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");
        ArrayList<Item> items = intent.getParcelableArrayListExtra("ITEMS");

        if (items == null || items.isEmpty()) {
            Toast.makeText(this, "No items available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        itemsAdapter.setItems(items);
        itemsAdapter.notifyDataSetChanged();

        // Handle back button
        backButton.setOnClickListener(v -> finish());

        // Handle item clicks
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", user);
            startActivity(detailIntent);
        });
    }
}
