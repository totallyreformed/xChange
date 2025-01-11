package com.example.xchange.request;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for handling the creation of a request in the xChange application.
 * <p>
 * This class displays the details of the requested item, allows the user to select one of their items to offer,
 * and sends the request to the item's owner. It uses a ViewModel to fetch the user's items and manage the request.
 * </p>
 */
public class RequestActivity extends AppCompatActivity {

    private RequestViewModel viewModel;
    private Item requestedItem;
    private User user, itemOwner;
    private Spinner userItemsSpinner;

    private xChanger Requester;
    private xChanger Requestee;

    /**
     * Initializes the activity, sets up the UI components, and handles user interactions.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        initializeUIComponents();
        if (!loadIntentData()) {
            showErrorAndExit();
            return;
        }
        setupRequestedItemDetails();
        setupRequesterDetails();
        initializeViewModel();
        setupSendRequestButton();
    }

    /**
     * Initializes UI components such as the item spinner.
     */
    private void initializeUIComponents() {
        userItemsSpinner = findViewById(R.id.userItemsSpinner);
    }

    /**
     * Loads data from the intent and initializes the {@link xChanger} objects for the requester and requestee.
     *
     * @return True if the data is successfully loaded; false otherwise.
     */
    private boolean loadIntentData() {
        requestedItem = getIntent().getParcelableExtra("REQUESTED_ITEM");
        user = getIntent().getParcelableExtra("USER");
        itemOwner = getIntent().getParcelableExtra("ITEM_OWNER");

        if (requestedItem != null && user != null && itemOwner != null) {
            Requester = new xChanger(user.getUsername(), user.getEmail(), user.getJoin_Date(), user.getPassword(), user.getLocation());
            Requestee = new xChanger(itemOwner.getUsername(), itemOwner.getEmail(), itemOwner.getJoin_Date(), itemOwner.getPassword(), itemOwner.getLocation());
            return true;
        }
        return false;
    }

    /**
     * Displays an error message and finishes the activity if the intent data is invalid.
     */
    private void showErrorAndExit() {
        Toast.makeText(this, "Error loading request details.", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Sets up the details of the requested item in the UI.
     */
    private void setupRequestedItemDetails() {
        TextView requestedItemNameTextView = findViewById(R.id.requestedItemNameTextView);
        TextView requestedItemDescriptionTextView = findViewById(R.id.requestedItemDescriptionTextView);
        ImageView requestedItemImageView = findViewById(R.id.requestedItemImageView);

        requestedItemNameTextView.setText(requestedItem.getItemName());
        requestedItemDescriptionTextView.setText(requestedItem.getItemDescription());

        if (requestedItem.getFirstImage() != null) {
            loadImage(requestedItem.getFirstImage().getFilePath(), requestedItemImageView);
        } else {
            requestedItemImageView.setImageResource(R.drawable.image_placeholder);
        }
    }

    /**
     * Loads an image into the specified {@link ImageView}.
     *
     * @param filePath The file path or resource ID of the image.
     * @param imageView The ImageView where the image will be loaded.
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
     * Sets up the details of the requester in the UI.
     */
    private void setupRequesterDetails() {
        TextView requesterNameTextView = findViewById(R.id.requesterNameTextView);
        requesterNameTextView.setText("Requester: " + user.getUsername());
    }

    /**
     * Initializes the ViewModel and fetches the user's items.
     */
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this, new RequestViewModelFactory(getApplicationContext())).get(RequestViewModel.class);
        viewModel.fetchUserItems(user.getUsername());
        viewModel.getUserItems().observe(this, this::populateUserItemsSpinner);
    }

    /**
     * Populates the spinner with the user's items.
     *
     * @param items The list of items owned by the user.
     */
    private void populateUserItemsSpinner(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(items));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            userItemsSpinner.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No items found for the requester.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Sets up the send request button to handle the request submission.
     */
    private void setupSendRequestButton() {
        Button sendRequestButton = findViewById(R.id.sendRequestButton);
        sendRequestButton.setOnClickListener(v -> handleSendRequest());
    }

    /**
     * Handles the logic for sending a request.
     */
    private void handleSendRequest() {
        Item offeredItem = (Item) userItemsSpinner.getSelectedItem();

        if (offeredItem == null) {
            Toast.makeText(this, "Please select an item to offer.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.sendRequest(Requester, Requestee, offeredItem, requestedItem);
        Toast.makeText(this, "Request sent successfully!", Toast.LENGTH_SHORT).show();

        navigateToMainActivity();
    }

    /**
     * Navigates back to the MainActivity after successfully sending the request.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
        finish();
    }
}