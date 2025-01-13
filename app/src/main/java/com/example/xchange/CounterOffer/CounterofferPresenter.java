package com.example.xchange.CounterOffer;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;
import java.util.List;

/**
 * Presenter class for managing the logic and data flow of the Counteroffer feature in the xChange app.
 * Handles interactions between the ViewModel, Repository, and UI components.
 */
public class CounterofferPresenter {

    private MutableLiveData<String> requesterText;
    private MutableLiveData<String> requesteeText;
    private MutableLiveData<String> requestedItemText;
    private MutableLiveData<List<Item>> spinnerItems;
    private MutableLiveData<String> selectedItemText;
    private MutableLiveData<String> errorMessage;
    private UserRepository userRepository;

    /**
     * Constructs a CounterofferPresenter.
     *
     * @param requesterText     LiveData to hold the requester's text.
     * @param requesteeText     LiveData to hold the requestee's text.
     * @param requestedItemText LiveData to hold the requested item's text.
     * @param spinnerItems      LiveData to hold the spinner's items.
     * @param selectedItemText  LiveData to hold the selected item's text.
     * @param errorMessage      LiveData to hold error messages.
     * @param context           The context to initialize the UserRepository.
     */
    public CounterofferPresenter(MutableLiveData<String> requesterText, MutableLiveData<String> requesteeText, MutableLiveData<String> requestedItemText, MutableLiveData<List<Item>> spinnerItems, MutableLiveData<String> selectedItemText, MutableLiveData<String> errorMessage, Context context) {
        this.requesterText = requesterText;
        this.requesteeText = requesteeText;
        this.requestedItemText = requestedItemText;
        this.spinnerItems = spinnerItems;
        this.selectedItemText = selectedItemText;
        this.errorMessage = errorMessage;
        this.userRepository = new UserRepository(context);
    }

    /**
     * Processes the details of a request and updates the LiveData objects.
     *
     * @param request The request to process.
     */
    public void processRequestDetails(Request request) {
        if (request != null) {
            requesterText.setValue("Requester: " + request.getRequester().getUsername());
            requesteeText.setValue("Requestee: " + request.getRequestee().getUsername());
            requestedItemText.setValue("Requested Item: " + request.getRequestedItem().getItemName());
        } else {
            displayError("Request is null!");
        }
    }

    /**
     * Populates the spinner with a list of items.
     *
     * @param items The list of items to display in the spinner.
     */
    public void populateSpinnerWithItems(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            spinnerItems.setValue(items);
        } else {
            displayError("No items available to populate the spinner.");
        }
    }

    /**
     * Handles the selection of an item and updates the selected item text.
     *
     * @param selectedItem The selected item.
     */
    public void handleSelectedItem(Item selectedItem) {
        if (selectedItem != null) {
            selectedItemText.setValue("Selected Item: " + selectedItem.getItemName());
        } else {
            displayError("No item selected!");
        }
    }

    /**
     * Handles cases where no item is selected in the spinner.
     */
    public void handleNoSelection() {
        displayError("No item selected in spinner.");
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void displayError(String message) {
        errorMessage.setValue(message);
    }

    /**
     * Finds a specific request based on the item ID and username.
     *
     * @param itemId   The ID of the requested item.
     * @param username The username of the requester.
     * @param callback The callback to handle the result.
     */
    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        // Delegate to UserRepository
        userRepository.findRequest(itemId, username, new UserRepository.FindRequestCallback() {
            @Override
            public void onResult(boolean found, Request request) {
                if (found) {
                    callback.onResult(true, request);
                } else {
                    callback.onResult(false, null);
                }
            }
        });
    }

    /**
     * Creates a counteroffer for a specific request using a given item and xChanger.
     * <p>
     * Note: The actual sending of the notification for a counteroffer is handled in the UI (Activity)
     * after successfully calling this method.
     *
     * @param request     The original request.
     * @param counterItem The item offered as a counteroffer.
     * @param xchanger    The xChanger instance performing the counteroffer.
     */
    public void createCounterOffer(Request request, Item counterItem, xChanger xchanger) {
        if (request == null || counterItem == null || xchanger == null) {
            throw new IllegalArgumentException("Item, message, or request cannot be null.");
        }
        if (xchanger.getCounterOffers().stream().anyMatch(co -> co.getRequest().equals(request) && co.getOfferedItem().equals(counterItem))) {
            throw new IllegalArgumentException("Duplicate counteroffer detected.");
        }

        try {
            xchanger.counterOffer(counterItem, request);
        } catch (Exception e) {
            Log.e("CounterofferPresenter", "Error creating counteroffer", e);
        }
    }
}
