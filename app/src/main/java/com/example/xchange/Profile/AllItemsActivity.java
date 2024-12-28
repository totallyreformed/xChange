package com.example.xchange.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Item;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        RecyclerView allItemsRecyclerView = findViewById(R.id.allItemsRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);

        allItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter itemsAdapter = new ItemsAdapter(new ArrayList<>());
        allItemsRecyclerView.setAdapter(itemsAdapter);

        // Retrieve items and user from intent
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");
        ArrayList<?> items = intent.getParcelableArrayListExtra("ITEMS");

        if (items != null) {
            itemsAdapter.setItems((List<Item>) items);
        }

        // Back to Profile
        backButton.setOnClickListener(v -> finish());
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            startActivity(detailIntent);
        });
    }
}
