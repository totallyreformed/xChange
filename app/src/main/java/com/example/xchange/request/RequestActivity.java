package com.example.xchange.request;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private RequestViewModel viewModel;
    private Item requestedItem;
    private User user, itemOwner;
    private Spinner userItemsSpinner;

    private xChanger Requester;
    private xChanger Requestee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        TextView requestedItemNameTextView = findViewById(R.id.requestedItemNameTextView);
        TextView requestedItemDescriptionTextView = findViewById(R.id.requestedItemDescriptionTextView);
        TextView requesterNameTextView = findViewById(R.id.requesterNameTextView);
        ImageView requestedItemImageView = findViewById(R.id.requestedItemImageView);
        userItemsSpinner = findViewById(R.id.userItemsSpinner);
        Button sendRequestButton = findViewById(R.id.sendRequestButton);

        // Get Data from Intent
        requestedItem = getIntent().getParcelableExtra("REQUESTED_ITEM");
        user = getIntent().getParcelableExtra("USER");
        itemOwner = getIntent().getParcelableExtra("ITEM_OWNER");

        // Δημιουργούμε τα αντικείμενα Requester και Requestee
        Requester = new xChanger(user.getUsername(), user.getEmail(), user.getJoin_Date(), user.getPassword(), user.getLocation());
        Requestee = new xChanger(itemOwner.getUsername(), itemOwner.getEmail(), itemOwner.getJoin_Date(), itemOwner.getPassword(), itemOwner.getLocation());

        if (requestedItem == null || user == null || itemOwner == null) {
            Toast.makeText(this, "Error loading request details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display requested item details
        requestedItemNameTextView.setText(requestedItem.getItemName());
        requestedItemDescriptionTextView.setText(requestedItem.getItemDescription());
        requesterNameTextView.setText("Requester: " + user.getUsername());

        if (requestedItem.getFirstImage() != null) {
            String filePath = requestedItem.getFirstImage().getFilePath();
            if (filePath != null) {
                try {
                    int resourceId = Integer.parseInt(filePath);
                    Glide.with(this)
                            .load(resourceId)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(requestedItemImageView);
                } catch (NumberFormatException e) {
                    Glide.with(this)
                            .load(filePath)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(requestedItemImageView);
                }
            } else {
                requestedItemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            requestedItemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new RequestViewModelFactory(getApplicationContext())).get(RequestViewModel.class);
        viewModel.fetchUserItems(user.getUsername());
        viewModel.getUserItems().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(items));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userItemsSpinner.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No items found for the requester.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set button click listener
        sendRequestButton.setOnClickListener(v -> handleSendRequest());
    }

    private void handleSendRequest() {
        Item offeredItem = (Item) userItemsSpinner.getSelectedItem();

        if (offeredItem == null) {
            Toast.makeText(this, "Please select an item to offer.", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.sendRequest(Requester, Requestee, offeredItem, requestedItem);
        Toast.makeText(this, "Request sent successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Finish the current activity
    }
}