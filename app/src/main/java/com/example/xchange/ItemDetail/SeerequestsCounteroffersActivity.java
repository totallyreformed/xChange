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

/**
 * The {@code SeerequestsCounteroffersActivity} class is responsible for displaying the details
 * of a {@link Request} or {@link Counteroffer} to the user. It handles the visualization
 * of offered and requested items along with the associated requester and requestee details.
 */
public class SeerequestsCounteroffersActivity extends AppCompatActivity {

    private ImageView offeredItemImageView, requestedItemImageView;
    private TextView offeredItemNameTextView, requestedItemNameTextView;
    private TextView requesterNameTextView, requesteeNameTextView;
    private Button backButton;

    /**
     * Called when the activity is first created. Initializes the UI and handles intent data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data most recently supplied in {@code onSaveInstanceState}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seerequest_counteroffer);

        initializeUI();
        handleIntentData();
        setupBackButton();
    }

    /**
     * Initializes the UI components for the activity.
     */
    private void initializeUI() {
        offeredItemImageView = findViewById(R.id.offeredItemImageView);
        requestedItemImageView = findViewById(R.id.requestedItemImageView);
        offeredItemNameTextView = findViewById(R.id.offeredItemNameTextView);
        requestedItemNameTextView = findViewById(R.id.requestedItemNameTextView);
        requesterNameTextView = findViewById(R.id.requesterNameTextView);
        requesteeNameTextView = findViewById(R.id.requesteeNameTextView);
        backButton = findViewById(R.id.backButton);
    }

    /**
     * Handles the intent data passed to the activity, determining whether a
     * {@link Counteroffer} or {@link Request} is being displayed.
     */
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

    /**
     * Sets up the back button functionality.
     */
    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Displays the details of a {@link Request}.
     *
     * @param request The {@link Request} object containing the details to display.
     */
    private void displayRequestDetails(Request request) {
        setRequesterAndRequesteeNames(request.getRequester().getUsername(), request.getRequestee().getUsername());
        displayItemDetails(request.getOfferedItem(), offeredItemNameTextView, offeredItemImageView, "Offered Item");
        displayItemDetails(request.getRequestedItem(), requestedItemNameTextView, requestedItemImageView, "Requested Item");
    }

    /**
     * Displays the details of a {@link Counteroffer}.
     *
     * @param counteroffer The {@link Counteroffer} object containing the details to display.
     */
    private void displayCounterofferDetails(Counteroffer counteroffer) {
        setRequesterAndRequesteeNames(counteroffer.getCounterofferer().getUsername(), counteroffer.getCounterofferee().getUsername());
        displayItemDetails(counteroffer.getOfferedItem(), offeredItemNameTextView, offeredItemImageView, "Counteroffer Item");
        displayItemDetails(counteroffer.getRequestedItem(), requestedItemNameTextView, requestedItemImageView, "Requested Item");
    }

    /**
     * Sets the names of the requester and requestee in the corresponding UI elements.
     *
     * @param requester The username of the requester.
     * @param requestee The username of the requestee.
     */
    @SuppressLint("SetTextI18n")
    private void setRequesterAndRequesteeNames(String requester, String requestee) {
        requesterNameTextView.setText("Requester: " + requester);
        requesteeNameTextView.setText("Requestee: " + requestee);
    }

    /**
     * Displays the details of an item in the UI.
     *
     * @param item         The {@link Item} object containing the details to display.
     * @param nameTextView The {@link TextView} for the item's name.
     * @param imageView    The {@link ImageView} for the item's image.
     * @param label        A label describing the item's role (e.g., "Offered Item").
     */
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

    /**
     * Loads an image into an {@link ImageView} using Glide.
     *
     * @param filePath The file path of the image.
     * @param imageView The {@link ImageView} in which to load the image.
     */
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

    /**
     * Displays a toast message and closes the activity.
     *
     * @param message The message to display in the toast.
     */
    private void showToastAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}