package com.example.xchange.ItemDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.EditItem.EditItemActivity;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.User;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailViewModel viewModel;
    private TextView itemNameTextView, itemDescriptionTextView, itemCategoryTextView, itemConditionTextView, itemXchangerTextView;
    private ImageView itemImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Button backButton = findViewById(R.id.backToMainButton);
        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);

        backButton.setOnClickListener(v -> finish());

        // Initialize Views
        itemNameTextView = findViewById(R.id.detailItemNameTextView);
        itemDescriptionTextView = findViewById(R.id.detailItemDescriptionTextView);
        itemCategoryTextView = findViewById(R.id.detailItemCategoryTextView);
        itemConditionTextView = findViewById(R.id.detailItemConditionTextView);
        itemXchangerTextView = findViewById(R.id.detailItemXchangerTextView);
        itemImageView = findViewById(R.id.detailItemImageView);

        // Get item ID from Intent
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get User object from Intent
        User user = getIntent().getParcelableExtra("USER");
        if (user == null) {
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new ItemDetailViewModelFactory(getApplication())).get(ItemDetailViewModel.class);

        // Observe LiveData for the Item
        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                displayItemDetails(item, user);
            } else {
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Delete button functionality
        deleteButton.setOnClickListener(v -> {
            viewModel.deleteItemById(itemId);
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Edit button functionality
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(ItemDetailActivity.this, EditItemActivity.class);
            editIntent.putExtra("ITEM_ID", itemId);
            startActivity(editIntent);
        });
    }

    private void displayItemDetails(Item item, User user) {
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        // Load the first image using Glide
        if (item.getFirstImage() != null && item.getFirstImage().getFilePath() != null) {
            Glide.with(this)
                    .load(item.getFirstImage().getFilePath())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(itemImageView);
        } else {
            itemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Show or hide buttons based on user ownership
        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);

        if (user.getUsername().equals(item.getXchanger())) {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }
    }
}
