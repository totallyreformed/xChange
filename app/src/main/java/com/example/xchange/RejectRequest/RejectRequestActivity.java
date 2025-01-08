package com.example.xchange.RejectRequest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RejectRequestActivity extends AppCompatActivity {

    private RejectRequestViewModel viewModel;
    private ImageView offeredItemImageView, requestedItemImageView;
    private Request requestToReject;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_request);

        // Initialize views
        TextView requesterTextView = findViewById(R.id.requesterTextView);
        TextView offeredItemHeaderTextView = findViewById(R.id.offeredItemHeaderTextView);
        ImageView offeredItemImageView = findViewById(R.id.offeredItemImageView);
        TextView requestedItemHeaderTextView = findViewById(R.id.requestedItemHeaderTextView);
        ImageView requestedItemImageView = findViewById(R.id.requestedItemImageView);
        TextView requestStatusTextView = findViewById(R.id.requestStatusTextView);
        Button rejectButton = findViewById(R.id.rejectButton);
        Button backButton = findViewById(R.id.backButton);

        // Retrieve data from intent
        requestToReject = getIntent().getParcelableExtra("REQUEST");
        currentUser = getIntent().getParcelableExtra("USER");

        if (requestToReject == null || currentUser == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        xChanger xchanger = new xChanger(currentUser.getUsername(),currentUser.getEmail(),currentUser.getJoin_Date(),currentUser.getPassword(),currentUser.getLocation());


        // Initialize ViewModel
        RejectRequestViewModelFactory factory = new RejectRequestViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RejectRequestViewModel.class);

        // Populate views with request details
        requesterTextView.setText("Requester: " + requestToReject.getRequester().getUsername());
        offeredItemHeaderTextView.setText("Offered Item: " + requestToReject.getOfferedItem().getItemName());
        requestedItemHeaderTextView.setText("Requested Item: " + requestToReject.getRequestedItem().getItemName());
        requestStatusTextView.setText("Request Status: Pending");

        // Load images for offered and requested items
        loadItemImage(requestToReject.getOfferedItem(), offeredItemImageView);
        loadItemImage(requestToReject.getRequestedItem(), requestedItemImageView);

        // Reject button functionality
        rejectButton.setOnClickListener(v -> showRejectConfirmationDialog());

        // Back button functionality
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

    private void showRejectConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Rejection")
                .setMessage("Are you sure you want to reject this request?")
                .setPositiveButton("Yes", (dialog, which) -> handleRejectRequest())
                .setNegativeButton("No", null)
                .show();
    }

    private void handleRejectRequest() {
        xChanger xchanger = new xChanger(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getJoin_Date(),
                currentUser.getPassword(),
                currentUser.getLocation()
        );

        viewModel.rejectRequest(xchanger, requestToReject, new RejectRequestViewModel.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                Notification notification = new Notification(
                        requestToReject.getRequester().getUsername(),
                        "Your request has been rejected by " + currentUser.getUsername(),
                        SimpleCalendar.today()
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
                        .setTitle("Request Rejected")
                        .setMessage("The requester has been notified of the rejection.")
                        .setPositiveButton("Proceed", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        })
                        .show());
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> Toast.makeText(RejectRequestActivity.this, "Failed to reject request: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Store the rejection notification for the other user
    private void storeRejectionNotification(Request request) {
        String notificationMessage = "Your request for \"" + request.getRequestedItem().getItemName() +
                "\" has been rejected.";
        viewModel.storeNotificationForUser(request.getRequester().getUsername(), notificationMessage);
    }
}
