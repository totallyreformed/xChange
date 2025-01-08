package com.example.xchange.AcceptRequest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.Notification;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;
import com.example.xchange.Item;

public class AcceptRequestActivity extends AppCompatActivity {

    private AcceptRequestViewModel viewModel;
    private Request request;
    private User currentUser;
    private TextView requesterTextView, requesteeTextView, requestStatusTextView;
    private TextView offeredItemTextView, requestedItemTextView;
    private ImageView offeredItemImageView, requestedItemImageView;
    private Button acceptButton, backButton;
    private RatingBar requestRatingBar;

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
        backButton = findViewById(R.id.backButton);
        requestRatingBar = findViewById(R.id.requestRatingBar);

        // Retrieve data from Intent
        request = getIntent().getParcelableExtra("REQUEST");
        currentUser = getIntent().getParcelableExtra("USER");

        if (request == null || currentUser == null) {
            Toast.makeText(this, "Error loading request details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Convert currentUser to xChanger
        xChanger xchanger = new xChanger(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getJoin_Date(),
                currentUser.getPassword(),
                currentUser.getLocation()
        );

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
        viewModel = new ViewModelProvider(this, new AcceptRequestViewModelFactory(getApplicationContext()))
                .get(AcceptRequestViewModel.class);

        // Set button click listeners
        acceptButton.setOnClickListener(v -> showAcceptConfirmationDialog());
        backButton.setOnClickListener(v -> finish());
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
                    float rating = requestRatingBar != null ? requestRatingBar.getRating() : 5.0f;
                    handleAcceptRequest(rating);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void handleAcceptRequest(float rating) {
        xChanger xchanger = new xChanger(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getJoin_Date(),
                currentUser.getPassword(),
                currentUser.getLocation()
        );

        viewModel.acceptRequest(request, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess() {
                // Create a notification for the requester
                Notification notification = new Notification(
                        request.getRequester().getUsername(),
                        "Your request has been accepted by " + currentUser.getUsername(),
                        SimpleCalendar.today()
                );

                UserRepository userRepository = new UserRepository(getApplication());
                userRepository.addNotification(notification, new UserRepository.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("AcceptRequestActivity", "Notification added successfully");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("AcceptRequestActivity", "Failed to add notification: " + message);
                    }
                });

                // Show the acceptance dialog
                runOnUiThread(() -> new AlertDialog.Builder(AcceptRequestActivity.this)
                        .setTitle("Request Accepted")
                        .setMessage("The requester has been notified. Proceed to view the xChange confirmation.")
                        .setPositiveButton("Proceed", (dialog, which) -> {
                            Intent intent = new Intent(AcceptRequestActivity.this, xChangeConfirmationActivity.class);
                            intent.putExtra("REQUEST", request);
                            intent.putExtra("USER", currentUser);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show());
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                        Toast.makeText(AcceptRequestActivity.this, "Failed to accept request: " + message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void notifyOtherUserOfAcceptance() {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Request Accepted")
                .setMessage("The requester has been notified, and they will be redirected to the xChange confirmation.")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    Intent intent = new Intent(AcceptRequestActivity.this, xChangeConfirmationActivity.class);
                    intent.putExtra("REQUEST", request);
                    intent.putExtra("USER", currentUser);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show());
    }
}
