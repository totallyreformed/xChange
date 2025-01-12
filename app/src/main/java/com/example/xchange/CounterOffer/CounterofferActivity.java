package com.example.xchange.CounterOffer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.Notification;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for creating a counteroffer in the xChange app.
 * Displays request details and allows the user to select an item for the counteroffer.
 */
public class CounterofferActivity extends AppCompatActivity {

    private TextView requesterTextView, requesteeTextView, requestedItemTextView;
    private Spinner offeredItemSpinner;
    private CounterofferViewModel viewModel;

    /**
     * Called when the activity is created.
     * Initializes the UI, ViewModel, and handles intent data.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_offer);

        initializeUI();
        initializeViewModel();
        handleIntentData();
        observeViewModelData();
        setupCounterofferButton();
    }

    /**
     * Initializes the UI components of the activity.
     */
    private void initializeUI() {
        requesterTextView = findViewById(R.id.requesterTextView);
        requesteeTextView = findViewById(R.id.requesteeTextView);
        requestedItemTextView = findViewById(R.id.requestedItemTextView);
        offeredItemSpinner = findViewById(R.id.offeredItemSpinner);
    }

    /**
     * Initializes the ViewModel used for managing data in the activity.
     */
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CounterofferViewModel(CounterofferActivity.this);
            }
        }).get(CounterofferViewModel.class);
    }

    /**
     * Handles the intent data passed to the activity and updates the ViewModel.
     */
    private void handleIntentData() {
        Request request = getIntent().getParcelableExtra("REQUEST");
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra("XCHANGER_ITEMS");

        if (request != null) {
            viewModel.setRequestDetails(request);
        }

        if (items != null) {
            viewModel.populateSpinner(items);
        }
    }

    /**
     * Observes data in the ViewModel and updates the UI accordingly.
     */
    private void observeViewModelData() {
        viewModel.getRequesterText().observe(this, text -> requesterTextView.setText(text));
        viewModel.getRequesteeText().observe(this, text -> requesteeTextView.setText(text));
        viewModel.getRequestedItemText().observe(this, text -> requestedItemTextView.setText(text));

        viewModel.getSpinnerItems().observe(this, this::populateSpinner);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Populates the spinner with items and handles item selection events.
     *
     * @param items The list of items to display in the spinner.
     */
    private void populateSpinner(List<Item> items) {
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeredItemSpinner.setAdapter(adapter);

        offeredItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Item selectedItem = (Item) offeredItemSpinner.getSelectedItem();
                viewModel.handleItemSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.handleNoSelection();
            }
        });
    }

    /**
     * Sets up the counteroffer button and its click event.
     */
    private void setupCounterofferButton() {
        Button initializeCounterofferButton = findViewById(R.id.initializeCounterofferButton);
        initializeCounterofferButton.setOnClickListener(view -> initializeCounteroffer());
    }

    /**
     * Initializes a counteroffer using the selected item and request details.
     */
    private void initializeCounteroffer() {
        Item selectedItem = (Item) offeredItemSpinner.getSelectedItem();

        if (selectedItem == null) {
            Toast.makeText(this, "Please select an item to counteroffer.", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = getIntent().getParcelableExtra("REQUEST");
        User user = request != null ? request.getRequestee() : null;

        if (user == null || request == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.findRequest(request.getRequestedItem().getItemId(), user.getUsername(), (found, foundRequest) -> {
            runOnUiThread(() -> {
                if (found && foundRequest != null) {
                    xChanger xchanger = new xChanger(user.getUsername(), user.getEmail(), user.getJoin_Date(), user.getPassword(), user.getLocation());
                    viewModel.initializeCounterRequest(foundRequest, selectedItem, xchanger);

                    // Send notification to the original requester that a counteroffer has been made.
                    sendCounterofferNotification(
                            request.getRequester().getUsername(),
                            "Your request has received a counteroffer from " + user.getUsername(),
                            foundRequest.getRequestId(),  // Using the request id as the xChangeId for context.
                            selectedItem.getItemId()
                    );

                    Toast.makeText(this, "Counter offer initialized", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity(user, selectedItem);
                } else {
                    Toast.makeText(this, "No matching request found!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * Sends a counteroffer notification to the given recipient.
     *
     * @param recipientUsername The username of the recipient.
     * @param message           The notification message.
     * @param xChangeId         The ID representing the associated xChange (or request).
     * @param itemId            The ID of the item offered as a counteroffer.
     */
    private void sendCounterofferNotification(String recipientUsername, String message, long xChangeId, long itemId) {
        Notification notification = new Notification(
                recipientUsername,
                message,
                SimpleCalendar.today(),
                xChangeId,
                itemId
        );
        UserRepository userRepository = new UserRepository(getApplication());
        userRepository.addNotification(notification, new UserRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                // Log success (UI updates such as Toasts should not be called here because this may not be the main thread)
                runOnUiThread(() ->
                        Toast.makeText(CounterofferActivity.this, "Counteroffer notification sent.", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                        Toast.makeText(CounterofferActivity.this, "Failed to send notification: " + message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    /**
     * Navigates to the main activity with the updated user and selected item data.
     *
     * @param user         The user involved in the counteroffer.
     * @param selectedItem The item selected for the counteroffer.
     */
    private void navigateToMainActivity(User user, Item selectedItem) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", user);
        intent.putExtra("REQUESTED_ITEM_ID", selectedItem.getItemId());
        startActivity(intent);
    }
}