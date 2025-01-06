package com.example.xchange.ItemDetail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.Request;

public class SeerequestsCounteroffersActivity extends AppCompatActivity {

    private ImageView offeredItemImageView, requestedItemImageView;
    private TextView offeredItemNameTextView, requestedItemNameTextView;
    private TextView requesterNameTextView, requesteeNameTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seerequest_counteroffer);

        // Initialize Views
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        offeredItemNameTextView = findViewById(R.id.offeredItemNameTextView);
        requestedItemNameTextView = findViewById(R.id.requestedItemNameTextView);
        requesterNameTextView = findViewById(R.id.requesterNameTextView);
        requesteeNameTextView = findViewById(R.id.requesteeNameTextView);
        backButton = findViewById(R.id.backButton);

        // Retrieve the Request object from Intent
        Request request = getIntent().getParcelableExtra("REQUEST");
        if (request == null) {
            Toast.makeText(this, "No request data available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display request details
        displayRequestDetails(request);

        // Handle Back button
        backButton.setOnClickListener(v -> finish());
    }

    private void displayRequestDetails(Request request) {
        // Display the requester and requestee names
        requesterNameTextView.setText("Requester: " + request.getRequester().getUsername());
        requesteeNameTextView.setText("Requestee: " + request.getRequestee().getUsername());

        // Display the offered item details
        Item offeredItem = request.getOfferedItem();
        if (offeredItem != null) {
            offeredItemNameTextView.setText("Offered Item: " + offeredItem.getItemName());
            if (offeredItem.getFirstImage() != null && offeredItem.getFirstImage().getFilePath() != null) {
                String filePath = offeredItem.getFirstImage().getFilePath();
                loadImage(filePath, offeredItemImageView);
            } else {
                offeredItemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            offeredItemNameTextView.setText("Offered Item: Not available");
            offeredItemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Display the requested item details
        Item requestedItem = request.getRequestedItem();
        if (requestedItem != null) {
            requestedItemNameTextView.setText("Requested Item: " + requestedItem.getItemName());
            if (requestedItem.getFirstImage() != null && requestedItem.getFirstImage().getFilePath() != null) {
                String filePath = requestedItem.getFirstImage().getFilePath();
                loadImage(filePath, requestedItemImageView);
            } else {
                requestedItemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            requestedItemNameTextView.setText("Requested Item: Not available");
            requestedItemImageView.setImageResource(R.drawable.image_placeholder);
        }
    }


    private void loadImage(String filePath, ImageView imageView) {
        if (filePath != null) {
            try {
                // Try parsing as a resource ID
                int resourceId = Integer.parseInt(filePath);
                Glide.with(this)
                        .load(resourceId)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .into(imageView);
            } catch (NumberFormatException e) {
                // If parsing fails, treat as a file path or URL
                Glide.with(this)
                        .load(filePath)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .into(imageView);
            }
        } else {
            // If filePath is null, use a placeholder
            imageView.setImageResource(R.drawable.image_placeholder);
        }
    }

}
