package com.example.xchange.ItemDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.CounterOffer.CounterofferActivity;
import com.example.xchange.EditItem.EditItemActivity;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.request.RequestActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailViewModel viewModel;
    private TextView itemNameTextView, itemDescriptionTextView, itemCategoryTextView, itemConditionTextView, itemXchangerTextView;
    private ImageView itemImageView;
    private Request requestToSend; // Request to pass to the new activity

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialize Views
        itemNameTextView = findViewById(R.id.detailItemNameTextView);
        itemDescriptionTextView = findViewById(R.id.detailItemDescriptionTextView);
        itemCategoryTextView = findViewById(R.id.detailItemCategoryTextView);
        itemConditionTextView = findViewById(R.id.detailItemConditionTextView);
        itemXchangerTextView = findViewById(R.id.detailItemXchangerTextView);
        itemImageView = findViewById(R.id.detailItemImageView);

        Button backButton = findViewById(R.id.backToMainButton);
        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);
        Button seeRequestCounterofferButton = findViewById(R.id.seeRequestCounterofferButton);

        // Handle "Back to Main" Button
        backButton.setOnClickListener(v -> finish());

        // Get item ID and user data from Intent
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

        // Fetch item details and display
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
            Intent editIntent = new Intent(this, EditItemActivity.class);
            editIntent.putExtra("ITEM_ID", itemId);
            startActivity(editIntent);
        });

        // Check if Accept/Reject should be displayed
        viewModel.checkToDisplayAcceptReject(itemId, user.getUsername(), (success, request) -> {
            runOnUiThread(() -> {
                Button acceptButton = findViewById(R.id.acceptButton);
                Button rejectButton = findViewById(R.id.rejectButton);
                Button counterofferButton = findViewById(R.id.counterofferButton);
                Button seeextra=findViewById(R.id.seeRequestCounterofferButton);
                Button edit=findViewById(R.id.editItemButton);

                if (success && request != null) {
                    requestToSend = request; // Store the request to send
                    acceptButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    counterofferButton.setVisibility(View.VISIBLE);
                    seeextra.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);

                }
            });
        });

        seeRequestCounterofferButton.setOnClickListener(view -> {
            if (requestToSend != null) {
                Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                intent.putExtra("REQUEST", requestToSend);
                startActivity(intent);

            } else {
                Toast.makeText(this, "No request available to view.", Toast.LENGTH_SHORT).show();
            }
        });


        // Handle Counteroffer Button
        Button counterButton = findViewById(R.id.counterofferButton);
        counterButton.setOnClickListener(v -> {
            viewModel.findRequest(itemId, user.getUsername(), (success, request) -> {
                if (success && request != null) {
                    Intent intent = new Intent(this, CounterofferActivity.class);
                    intent.putExtra("REQUEST", request);

                    // Fetch all items of the xChanger
                    viewModel.findItemsByXChanger(request.getRequester().getUsername(), new UserRepository.UserItemsCallback() {
                        @Override
                        public void onSuccess(List<Item> items) {
                            if (items != null && !items.isEmpty()) {
                                intent.putParcelableArrayListExtra("XCHANGER_ITEMS", new ArrayList<>(items));
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String message) {
                            startActivity(intent);
                        }
                    });
                }
            });
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayItemDetails(Item item, User user) {
        // Display item details
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        // Display item image using Glide
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

        // Show/hide buttons based on user
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

        // Handle Request Button
        requestButton.setOnClickListener(v -> {
            LiveData<User> ownerLiveData = viewModel.getUserByUsername(item.getXchanger());
            ownerLiveData.observe(this, owner -> {
                if (owner != null) {
                    Intent intent = new Intent(this, RequestActivity.class);
                    intent.putExtra("REQUESTED_ITEM", item);
                    intent.putExtra("USER", user);
                    intent.putExtra("ITEM_OWNER", owner);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Owner not found!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
