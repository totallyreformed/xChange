package com.example.xchange.RejectRequest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class RejectRequestActivity extends AppCompatActivity {

    private RejectRequestViewModel viewModel;
    private ImageView offeredItemImageView, requestedItemImageView;
    private Request request;
    private Counteroffer counteroffer;
    private User currentUser;

    // UI Components
    private TextView requesterTextView;
    private TextView offeredItemHeaderTextView;
    private TextView requestedItemHeaderTextView;
    private TextView requestStatusTextView;
    private Button rejectButton;
    private Button backButton;
    private RatingBar requestRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_request);

        // Initialize views
        requesterTextView = findViewById(R.id.requesterTextView);
        offeredItemHeaderTextView = findViewById(R.id.offeredItemHeaderTextView);
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemHeaderTextView = findViewById(R.id.requestedItemHeaderTextView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        requestStatusTextView = findViewById(R.id.requestStatusTextView);
        rejectButton = findViewById(R.id.rejectButton);
        backButton = findViewById(R.id.backButton);
        requestRatingBar = findViewById(R.id.requestRatingBar);


        // Retrieve data from intent
        request = getIntent().getParcelableExtra("REQUEST");
        currentUser = getIntent().getParcelableExtra("USER");
        counteroffer = getIntent().getParcelableExtra("COUNTEROFFER");

        if ((request == null && counteroffer == null) || currentUser == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Initialize ViewModel
        RejectRequestViewModelFactory factory = new RejectRequestViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RejectRequestViewModel.class);

        // Determine whether it's a Request or Counteroffer and populate UI accordingly
        if (request != null) {
            populateRequestDetails();
        } else {
            populateCounterofferDetails();
        }

        // Reject button functionality
        rejectButton.setOnClickListener(v -> showRejectConfirmationDialog());

        // Back button functionality
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Populates the UI with Request details.
     */
    private void populateRequestDetails() {
        // Check if request has a requester
        if (request.getRequester() != null) {
            requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        } else {
            requesterTextView.setText("Requester: Unknown");
            Log.e("RejectRequestActivity", "Request has null requester.");
        }

        offeredItemHeaderTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());
        requestedItemHeaderTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());
        requestStatusTextView.setText("Request Status: Pending");

        // Load images for offered and requested items
        loadItemImage(request.getOfferedItem(), offeredItemImageView);
        loadItemImage(request.getRequestedItem(), requestedItemImageView);
    }

    /**
     * Populates the UI with Counteroffer details.
     */
    private void populateCounterofferDetails() {
        // Check if counteroffer has a requester
        if (counteroffer.getCounterofferer() != null) {
            requesterTextView.setText("Requester: " + counteroffer.getCounterofferer().getUsername());
        } else {
            requesterTextView.setText("Requester: Unknown");
            Log.e("RejectRequestActivity", "Counteroffer has null requester.");
        }

        offeredItemHeaderTextView.setText("Offered Item: " + counteroffer.getOfferedItem().getItemName());
        requestedItemHeaderTextView.setText("Requested Item: " + counteroffer.getRequestedItem().getItemName());
        requestStatusTextView.setText("Counteroffer Status: Pending");

        // Load images for offered and requested items
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

    /**
     * Displays a confirmation dialog before rejecting the request/counteroffer.
     */
    private void showRejectConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Rejection")
                .setMessage("Are you sure you want to reject this " + (request != null ? "request" : "counteroffer") + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    float rating = requestRatingBar != null ? requestRatingBar.getRating() : 5.0f;
                    handleRejectRequest(rating);
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Handles the rejection of a Request or Counteroffer.
     */
    private void handleRejectRequest(float rating) {
        xChanger xchanger = new xChanger(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getJoin_Date(),
                currentUser.getPassword(),
                currentUser.getLocation()
        );

        if (request != null) {
            // Handle rejection of a regular Request
            viewModel.rejectRequest(xchanger, request, new RejectRequestViewModel.RejectRequestCallback() {
                @Override
                public void onSuccess() {
                    sendNotification(request.getRequester().getUsername(), "Your request has been rejected by " + currentUser.getUsername());
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> Toast.makeText(RejectRequestActivity.this, "Failed to reject request: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        } else if (counteroffer != null) {
            // Handle rejection of a Counteroffer
            viewModel.rejectCounteroffer(xchanger, counteroffer, new RejectRequestViewModel.RejectRequestCallback() {
                @Override
                public void onSuccess() {
                    sendNotification(counteroffer.getCounterofferer().getUsername(), "Your counteroffer has been rejected by " + currentUser.getUsername());
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> Toast.makeText(RejectRequestActivity.this, "Failed to reject counteroffer: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    /**
     * Sends a notification to the specified user.
     *
     * @param username The username of the recipient.
     * @param message  The notification message.
     */
    private void sendNotification(String username, String message) {
        Notification notification = new Notification(
                username,
                message,
                SimpleCalendar.today(),
                (long) -1
        );

        UserRepository userRepository = new UserRepository(getApplication());
        userRepository.addNotification(notification, new UserRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.d("RejectRequestActivity", "Notification added successfully");
            }

            @Override
            public void onFailure(String message) {
                Log.e("RejectRequestActivity", "Failed to add notification: " + message);
            }
        });

        runOnUiThread(() -> new AlertDialog.Builder(RejectRequestActivity.this)
                .setTitle("Rejection Successful")
                .setMessage("The requester has been notified of the rejection.")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show());
    }

}
