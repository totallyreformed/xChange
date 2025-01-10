package com.example.xchange.ItemDetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;

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

        // Retrieve the HAS_COUNTEROFFER flag
        boolean hasCounteroffer = getIntent().getBooleanExtra("HAS_COUNTEROFFER", false);
        User user = getIntent().getParcelableExtra("USER");
        if (hasCounteroffer) {
            // Handle Counteroffer
            Counteroffer counteroffer = getIntent().getParcelableExtra("COUNTEROFFER");
            if (counteroffer == null) {
                Toast.makeText(this, "No counteroffer data available.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            displayCounterofferDetails(counteroffer);
        } else {
            // Handle Request
            Request request = getIntent().getParcelableExtra("REQUEST");
            if (request == null) {
                Toast.makeText(this, "No request data available.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            displayRequestDetails(request);
        }

        // Handle Back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("USER",user);
            startActivity(intent);
            finish();
        });

    }

    private void displayRequestDetails(Request request) {
        // Display the requester and requestee names
        requesterNameTextView.setText("Requester: " + request.getRequester().getUsername());
        requesteeNameTextView.setText("Requestee: " + request.getRequestee().getUsername());

        // Display the offered item
        Item offeredItem = request.getOfferedItem();
        if (offeredItem != null) {
            offeredItemNameTextView.setText("Offered Item: " + offeredItem.getItemName());
            loadImage(offeredItem.getFirstImage() != null ? offeredItem.getFirstImage().getFilePath() : null, offeredItemImageView);
        } else {
            offeredItemNameTextView.setText("Offered Item: Not available");
            offeredItemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Display the requested item
        Item requestedItem = request.getRequestedItem();
        if (requestedItem != null) {
            requestedItemNameTextView.setText("Requested Item: " + requestedItem.getItemName());
            loadImage(requestedItem.getFirstImage() != null ? requestedItem.getFirstImage().getFilePath() : null, requestedItemImageView);
        } else {
            requestedItemNameTextView.setText("Requested Item: Not available");
            requestedItemImageView.setImageResource(R.drawable.image_placeholder);
        }
    }

    private void displayCounterofferDetails(Counteroffer counteroffer) {
        // Display the counterofferer and counterofferee names
        requesterNameTextView.setText("Counterofferer: " + counteroffer.getCounterofferer().getUsername());
        requesteeNameTextView.setText("Counterofferee: " + counteroffer.getCounterofferee().getUsername());

        // Display the offered item for the counteroffer
        Item offeredItem = counteroffer.getOfferedItem();
        if (offeredItem != null) {
            offeredItemNameTextView.setText("Counteroffer Item: " + offeredItem.getItemName());
            loadImage(offeredItem.getFirstImage() != null ? offeredItem.getFirstImage().getFilePath() : null, offeredItemImageView);
        } else {
            offeredItemNameTextView.setText("Counteroffer Item: Not available");
            offeredItemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Display the requested item for the counteroffer
        Item requestedItem = counteroffer.getRequestedItem();
        if (requestedItem != null) {
            requestedItemNameTextView.setText("Requested Item: " + requestedItem.getItemName());
            loadImage(requestedItem.getFirstImage() != null ? requestedItem.getFirstImage().getFilePath() : null, requestedItemImageView);
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