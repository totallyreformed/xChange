package com.example.xchange.ItemDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xchange.Counteroffer;
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

        initializeUI();
        handleIntentData();
        setupBackButton();
    }

    private void initializeUI() {
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        offeredItemNameTextView = findViewById(R.id.offeredItemNameTextView);
        requestedItemNameTextView = findViewById(R.id.requestedItemNameTextView);
        requesterNameTextView = findViewById(R.id.requesterNameTextView);
        requesteeNameTextView = findViewById(R.id.requesteeNameTextView);
        backButton = findViewById(R.id.backButton);
    }

    private void handleIntentData() {
        boolean hasCounteroffer = getIntent().getBooleanExtra("HAS_COUNTEROFFER", false);

        if (hasCounteroffer) {
            Counteroffer counteroffer = getIntent().getParcelableExtra("COUNTEROFFER");
            if (counteroffer == null) {
                showToastAndFinish("No counteroffer data available.");
                return;
            }
            displayCounterofferDetails(counteroffer);
        } else {
            Request request = getIntent().getParcelableExtra("REQUEST");
            if (request == null) {
                showToastAndFinish("No request data available.");
                return;
            }
            displayRequestDetails(request);
        }
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    private void displayRequestDetails(Request request) {
        setRequesterAndRequesteeNames(request.getRequester().getUsername(), request.getRequestee().getUsername());
        displayItemDetails(request.getOfferedItem(), offeredItemNameTextView, offeredItemImageView, "Offered Item");
        displayItemDetails(request.getRequestedItem(), requestedItemNameTextView, requestedItemImageView, "Requested Item");
    }

    private void displayCounterofferDetails(Counteroffer counteroffer) {
        setRequesterAndRequesteeNames(counteroffer.getCounterofferer().getUsername(), counteroffer.getCounterofferee().getUsername());
        displayItemDetails(counteroffer.getOfferedItem(), offeredItemNameTextView, offeredItemImageView, "Counteroffer Item");
        displayItemDetails(counteroffer.getRequestedItem(), requestedItemNameTextView, requestedItemImageView, "Requested Item");
    }

    @SuppressLint("SetTextI18n")
    private void setRequesterAndRequesteeNames(String requester, String requestee) {
        requesterNameTextView.setText("Requester: " + requester);
        requesteeNameTextView.setText("Requestee: " + requestee);
    }

    @SuppressLint("SetTextI18n")
    private void displayItemDetails(Item item, TextView nameTextView, ImageView imageView, String label) {
        if (item != null) {
            nameTextView.setText(label + ": " + item.getItemName());
            loadImage(item.getFirstImage() != null ? item.getFirstImage().getFilePath() : null, imageView);
        } else {
            nameTextView.setText(label + ": Not available");
            imageView.setImageResource(R.drawable.image_placeholder);
        }
    }

    private void loadImage(String filePath, ImageView imageView) {
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
    }

    private void showToastAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}