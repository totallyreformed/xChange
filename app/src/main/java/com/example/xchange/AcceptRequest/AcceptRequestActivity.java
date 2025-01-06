// File: com/example/xchange/acceptrequest/AcceptRequestActivity.java

package com.example.xchange.AcceptRequest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.xChanger;
import com.example.xchange.Item;

public class AcceptRequestActivity extends AppCompatActivity {

    private AcceptRequestViewModel viewModel;
    private Request request;
    private User currentUser;
    private TextView requesterTextView, requesteeTextView, requestStatusTextView;
    private TextView offeredItemTextView, requestedItemTextView;
    private ImageView offeredItemImageView, requestedItemImageView;
    private Button acceptButton, rejectButton;
    private RatingBar requestRatingBar;
    private float userRating = 5.0f; // Default rating or retrieve from UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        // Initialize UI components
        requesterTextView = findViewById(R.id.requesterTextView);
        requesteeTextView = findViewById(R.id.requesteeTextView);
        requestStatusTextView = findViewById(R.id.requestStatusTextView);
        offeredItemTextView = findViewById(R.id.offeredItemTextView);
        requestedItemTextView = findViewById(R.id.requestedItemTextView);
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);
        requestRatingBar = findViewById(R.id.requestRatingBar);


        // Retrieve data from Intent
        request = getIntent().getParcelableExtra("REQUEST");
        currentUser = getIntent().getParcelableExtra("USER");

        if (request == null || currentUser == null) {
            Toast.makeText(this, "Error loading request details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display request details
        requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        requesteeTextView.setText("Requestee: " + request.getRequestee().getUsername());
        requestStatusTextView.setText("Status: " + request.getStatus());
        offeredItemTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());
        requestedItemTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());

        // Load images using Glide
        loadItemImage(request.getOfferedItem(), offeredItemImageView);
        loadItemImage(request.getRequestedItem(), requestedItemImageView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new AcceptRequestViewModelFactory(getApplicationContext())).get(AcceptRequestViewModel.class);

        // Set button click listeners
        acceptButton.setOnClickListener(v -> showAcceptConfirmationDialog());
        rejectButton.setOnClickListener(v -> handleRejectRequest());
    }

    private void loadItemImage(Item item, ImageView imageView) {
        if (item.getFirstImage() != null) {
            String filePath = item.getFirstImage().getFilePath();
            if (filePath != null) {
                try {
                    int resourceId = Integer.parseInt(filePath);
                    Glide.with(this)
                            .load(resourceId)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(imageView);
                } catch (NumberFormatException e) {
                    Glide.with(this)
                            .load(filePath)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(imageView);
                }
            } else {
                imageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            imageView.setImageResource(R.drawable.image_placeholder);
        }
    }

    private void showAcceptConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Acceptance")
                .setMessage("Are you sure you want to accept this xChange?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    float rating = requestRatingBar != null ? requestRatingBar.getRating() : 5.0f; // Default rating if not using RatingBar
                    handleAcceptRequest(rating);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void handleAcceptRequest(float rating) {
        viewModel.acceptRequest(request, rating).observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(AcceptRequestActivity.this, "Request accepted successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AcceptRequestActivity.this, xChangeConfirmationActivity.class);
                intent.putExtra("REQUEST", request);
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AcceptRequestActivity.this, "Failed to accept request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRejectRequest() {
        // Optionally implement reject functionality
        // For now, simply delete or mark the request as inactive
        // You can create a similar method in ViewModel for rejection

        // Example implementation:
        request.make_unactive();
        viewModel.rejectRequest(request).observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(AcceptRequestActivity.this, "Request rejected.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AcceptRequestActivity.this, MainActivity.class);
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AcceptRequestActivity.this, "Failed to reject request.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
