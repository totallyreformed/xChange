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

    // UI Components
    private TextView requesterTextView;
    private TextView offeredItemHeaderTextView;
    private TextView requestedItemHeaderTextView;
    private TextView requestStatusTextView;
    private ImageView offeredItemImageView, requestedItemImageView;
    private Button acceptButton, backButton;
    private RatingBar requestRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        // Initialize views
        requesterTextView = findViewById(R.id.requesterTextView);
        offeredItemHeaderTextView = findViewById(R.id.offeredItemHeaderTextView);
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemHeaderTextView = findViewById(R.id.requestedItemHeaderTextView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        requestStatusTextView = findViewById(R.id.requestStatusTextView);
        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        requestRatingBar = findViewById(R.id.requestRatingBar);

        // Retrieve data from Intent
        request = getIntent().getParcelableExtra("REQUEST");
        counteroffer = getIntent().getParcelableExtra("COUNTEROFFER");
        currentUser = getIntent().getParcelableExtra("USER");

        if ((request == null && counteroffer == null) || currentUser == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new AcceptRequestViewModelFactory(getApplicationContext()))
                .get(AcceptRequestViewModel.class);

        // Populate UI based on whether it's a Request or Counteroffer
        if (request != null) {
            populateRequestDetails();
        } else {
            populateCounterofferDetails();
        }

        // Set button click listeners
        acceptButton.setOnClickListener(v -> showAcceptConfirmationDialog());
        backButton.setOnClickListener(v -> finish());
    }

    private void populateRequestDetails() {
        requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        offeredItemHeaderTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());
        requestedItemHeaderTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());
        requestStatusTextView.setText("Request Status: Pending");

        loadItemImage(request.getOfferedItem(), offeredItemImageView);
        loadItemImage(request.getRequestedItem(), requestedItemImageView);
    }

    private void populateCounterofferDetails() {
        requesterTextView.setText("Requester: " + counteroffer.getCounterofferer().getUsername());
        offeredItemHeaderTextView.setText("Offered Item: " + counteroffer.getOfferedItem().getItemName());
        requestedItemHeaderTextView.setText("Requested Item: " + counteroffer.getRequestedItem().getItemName());
        requestStatusTextView.setText("Counteroffer Status: Pending");

        loadItemImage(counteroffer.getOfferedItem(), offeredItemImageView);
        loadItemImage(counteroffer.getRequestedItem(), requestedItemImageView);
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

        if (request != null) {
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
            viewModel.acceptCounteroffer(counteroffer, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
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
                .setTitle("Request Accepted")
                .setMessage("The requester has been notified. Proceed to view the xChange confirmation.")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    Intent intent = new Intent(AcceptRequestActivity.this, xChangeConfirmationActivity.class);
                    intent.putExtra("REQUEST", request);
                    intent.putExtra("COUNTEROFFER", counteroffer);
                    intent.putExtra("USER", currentUser);
                    intent.putExtra("XCHANGE_ID", xChangeId); // Pass the xChangeId
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show());

    }
}
