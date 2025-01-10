// File: com/example/xchange/AcceptRequest/AcceptRequestActivity.java

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
import com.example.xchange.Counteroffer;
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
    private Counteroffer counteroffer;
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
        counteroffer = getIntent().getParcelableExtra("COUNTEROFFER");
        currentUser = getIntent().getParcelableExtra("USER");

        // Validate received data
        if ((request == null && counteroffer == null) || currentUser == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        AcceptRequestViewModelFactory factory = new AcceptRequestViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(AcceptRequestViewModel.class);

        // Determine whether it's a Request or Counteroffer and populate UI accordingly
        if (request != null) {
            populateRequestDetails();
        } else {
            populateCounterofferDetails();
        }

        // Set button click listeners
        acceptButton.setOnClickListener(v -> showAcceptConfirmationDialog());
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Populates the UI with Request details.
     */
    private void populateRequestDetails() {
        // Check if request has a requester and requestee
        if (request.getRequester() != null) {
            requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        } else {
            requesterTextView.setText("Requester: Unknown");
            Log.e("AcceptRequestActivity", "Request has null requester.");
        }

        if (request.getRequestee() != null) {
            requesteeTextView.setText("Requestee: " + request.getRequestee().getUsername());
        } else {
            requesteeTextView.setText("Requestee: Unknown");
            Log.e("AcceptRequestActivity", "Request has null requestee.");
        }

        requestStatusTextView.setText("Request Status: " + request.getStatus());
        offeredItemTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());
        requestedItemTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());

        // Load images for offered and requested items
        loadItemImage(request.getOfferedItem(), offeredItemImageView);
        loadItemImage(request.getRequestedItem(), requestedItemImageView);
    }

    /**
     * Populates the UI with Counteroffer details.
     */
    private void populateCounterofferDetails() {
        // Check if counteroffer has a counterofferer and counterofferee
        if (counteroffer.getCounterofferer() != null) {
            requesterTextView.setText("Counterofferer: " + counteroffer.getCounterofferer().getUsername());
        } else {
            requesterTextView.setText("Counterofferer: Unknown");
            Log.e("AcceptRequestActivity", "Counteroffer has null counterofferer.");
        }

        if (counteroffer.getCounterofferee() != null) {
            requesteeTextView.setText("Counterofferee: " + counteroffer.getCounterofferee().getUsername());
        } else {
            requesteeTextView.setText("Counterofferee: Unknown");
            Log.e("AcceptRequestActivity", "Counteroffer has null counterofferee.");
        }

        requestStatusTextView.setText("Counteroffer Status: Pending");
        offeredItemTextView.setText("Offered Item: " + counteroffer.getOfferedItem().getItemName());
        requestedItemTextView.setText("Requested Item: " + counteroffer.getRequestedItem().getItemName());

        // Load images for offered and requested items
        loadItemImage(counteroffer.getOfferedItem(), offeredItemImageView);
        loadItemImage(counteroffer.getRequestedItem(), requestedItemImageView);
    }

    /**
     * Loads an item's image into the provided ImageView using Glide.
     *
     * @param item      The item whose image is to be loaded.
     * @param imageView The ImageView where the image will be displayed.
     */
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

    /**
     * Displays a confirmation dialog before accepting the request/counteroffer.
     */
    private void showAcceptConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Acceptance")
                .setMessage("Are you sure you want to accept this " + (request != null ? "request" : "counteroffer") + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    float rating = requestRatingBar != null ? requestRatingBar.getRating() : 5.0f;
                    handleAcceptRequest(rating);
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Handles the acceptance of a Request or Counteroffer.
     */
    private void handleAcceptRequest(float rating) {
        xChanger xchanger = new xChanger(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getJoin_Date(),
                currentUser.getPassword(),
                currentUser.getLocation()
        );

        if (request != null) {
            // Handle acceptance of a regular Request
            viewModel.acceptRequest(request, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
                @Override
                public void onSuccess(long xChangeId) {
                    sendNotification(request.getRequester().getUsername(), "Your request has been accepted by " + currentUser.getUsername(), xChangeId);
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> Toast.makeText(AcceptRequestActivity.this, "Failed to accept request: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        } else if (counteroffer != null) {
            // Handle acceptance of a Counteroffer
            viewModel.acceptCounteroffer(xchanger, counteroffer, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
                @Override
                public void onSuccess(long xChangeId) {
                    sendNotification(counteroffer.getCounterofferer().getUsername(), "Your counteroffer has been accepted by " + currentUser.getUsername(), xChangeId);
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> Toast.makeText(AcceptRequestActivity.this, "Failed to accept counteroffer: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    /**
     * Sends a notification to the specified user.
     *
     * @param username    The username of the recipient.
     * @param message     The notification message.
     * @param xChangeId   The ID of the xChange for reference.
     */
    private void sendNotification(String username, String message, long xChangeId) {
        Notification notification = new Notification(
                username,
                message,
                SimpleCalendar.today(),
                xChangeId
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

        runOnUiThread(() -> new AlertDialog.Builder(AcceptRequestActivity.this)
                .setTitle("Acceptance Successful")
                .setMessage("The requester has been notified of the acceptance.")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show());
    }
}