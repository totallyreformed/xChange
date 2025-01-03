package com.example.xchange.ItemDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.EditItem.EditItemActivity;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.request.RequestActivity;
import com.example.xchange.User;
import com.example.xchange.xChanger;

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

        User user = getIntent().getParcelableExtra("USER");
        if (user == null) {
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new ItemDetailViewModelFactory(getApplication())).get(ItemDetailViewModel.class);

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


        viewModel.getItemById(itemId).observe(this, item -> {
            if (item == null) {
                Log.e("ItemDetail", "Item is null for ID: " + itemId);
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            viewModel.checkRequestToDisplay(itemId, user.getUsername(), result -> {
                runOnUiThread(() -> {
                    TextView requestStatusTextView = findViewById(R.id.requestStatusTextView);
                    Button requestItemButton = findViewById(R.id.requestItemButton);

                    if (user.getUsername().trim().equals(item.getXchanger().trim())) {
                        // User owns the item
                        requestStatusTextView.setVisibility(View.GONE);
                        requestItemButton.setVisibility(View.GONE);
                    } else if (result) {
                        // Item already requested
                        requestStatusTextView.setVisibility(View.VISIBLE);
                        requestItemButton.setVisibility(View.GONE);
                    } else {
                        // Item not requested
                        requestStatusTextView.setVisibility(View.GONE);
                        requestItemButton.setVisibility(View.VISIBLE);
                    }
                });
            });
        });

        viewModel.getItemById(itemId).observe(this, item -> {
            if (item == null) {
                Log.e("ItemDetail", "Item is null for ID: " + itemId);
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            viewModel.checkToDisplayAcceptReject(itemId, user.getUsername(), result -> {
                runOnUiThread(() -> {
                    Button requestItemButton = findViewById(R.id.requestItemButton);
                    Button acceptButton = findViewById(R.id.acceptButton);
                    Button rejectButton = findViewById(R.id.rejectButton);
                    if (!item.getXchanger().equals(user.getUsername())) {
                        // Ο χρήστης ΔΕΝ είναι ο κάτοχος
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);
                        requestItemButton.setVisibility(View.VISIBLE);
                    } else {
                        // Ο χρήστης είναι ο κάτοχος
                        if (result) {
                            acceptButton.setVisibility(View.VISIBLE);
                            rejectButton.setVisibility(View.VISIBLE);
                            requestItemButton.setVisibility(View.GONE);
                        } else {
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            requestItemButton.setVisibility(View.GONE);
                        }
                    }
                });
            });
        });



        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                displayItemDetails(item, user);
            } else {
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
        @SuppressLint("SetTextI18n")
    private void displayItemDetails(Item item, User user) {
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        if (item.getFirstImage() != null) {
            String filePath = item.getFirstImage().getFilePath();
            if (filePath != null) {
                try {
                    int resourceId = Integer.parseInt(filePath);
                    Glide.with(this)
                            .load(resourceId)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(itemImageView);
                } catch (NumberFormatException e) {
                    Glide.with(this)
                            .load(filePath)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(itemImageView);
                }
            } else {
                itemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            itemImageView.setImageResource(R.drawable.image_placeholder);
        }

        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);
        Button requestButton = findViewById(R.id.requestItemButton);

        if (user.getUsername().trim().equals(item.getXchanger().trim())) {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            requestButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            requestButton.setVisibility(View.VISIBLE);
        }

        // Handle request button click
        requestButton.setOnClickListener(v -> {
            LiveData<User> ownerLiveData = viewModel.getUserByUsername(item.getXchanger());

            ownerLiveData.observe(ItemDetailActivity.this, owner -> {
                if (owner != null) {
                    Intent intent = new Intent(ItemDetailActivity.this, RequestActivity.class);
                    intent.putExtra("REQUESTED_ITEM", item);
                    intent.putExtra("USER", user);
                    intent.putExtra("ITEM_OWNER", owner);
                    startActivity(intent);

                    // Σταματάμε την παρατήρηση για να αποφύγουμε περιττά callbacks
                    ownerLiveData.removeObservers(ItemDetailActivity.this);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Owner not found!", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

}
