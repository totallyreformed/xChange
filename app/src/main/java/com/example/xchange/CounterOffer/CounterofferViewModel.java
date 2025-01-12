package com.example.xchange.CounterOffer;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import java.util.List;

/**
 * ViewModel class for managing the state and logic of the Counteroffer feature in the xChange app.
 * Acts as a bridge between the UI and the CounterofferPresenter.
 */
public class CounterofferViewModel extends ViewModel {

    private MutableLiveData<String> requesterText = new MutableLiveData<>();
    private MutableLiveData<String> requesteeText = new MutableLiveData<>();
    private MutableLiveData<String> requestedItemText = new MutableLiveData<>();
    private MutableLiveData<List<Item>> spinnerItems = new MutableLiveData<>();
    private MutableLiveData<String> selectedItemText = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private CounterofferPresenter presenter;

    /**
     * Constructs a CounterofferViewModel.
     *
     * @param context The application context used to initialize the presenter and repository.
     */
    public CounterofferViewModel(Context context) {
        // Initialize presenter and pass LiveData references
        presenter = new CounterofferPresenter(requesterText, requesteeText, requestedItemText, spinnerItems, selectedItemText, errorMessage,context);
    }

    /**
     * Gets the LiveData for the requester's text.
     *
     * @return The LiveData object holding the requester's text.
     */
    public LiveData<String> getRequesterText() {
        return requesterText;
    }

    /**
     * Gets the LiveData for the requestee's text.
     *
     * @return The LiveData object holding the requestee's text.
     */
    public LiveData<String> getRequesteeText() {
        return requesteeText;
    }

    /**
     * Gets the LiveData for the requested item's text.
     *
     * @return The LiveData object holding the requested item's text.
     */
    public LiveData<String> getRequestedItemText() {
        return requestedItemText;
    }

    /**
     * Gets the LiveData for the spinner items.
     *
     * @return The LiveData object holding the list of spinner items.
     */
    public LiveData<List<Item>> getSpinnerItems() {
        return spinnerItems;
    }

    /**
     * Gets the LiveData for the selected item's text.
     *
     * @return The LiveData object holding the selected item's text.
     */
    public LiveData<String> getSelectedItemText() {
        return selectedItemText;
    }

    /**
     * Gets the LiveData for error messages.
     *
     * @return The LiveData object holding error messages.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the details of the request by delegating to the presenter.
     *
     * @param request The request to process.
     */
    public void setRequestDetails(Request request) {
        presenter.processRequestDetails(request);
    }

    /**
     * Populates the spinner with a list of items by delegating to the presenter.
     *
     * @param items The list of items to populate in the spinner.
     */
    public void populateSpinner(List<Item> items) {
        presenter.populateSpinnerWithItems(items);
    }

    /**
     * Handles the selection of an item by delegating to the presenter.
     *
     * @param selectedItem The selected item.
     */
    public void handleItemSelection(Item selectedItem) {
        presenter.handleSelectedItem(selectedItem);
    }

    /**
     * Handles cases where no item is selected by delegating to the presenter.
     */
    public void handleNoSelection() {
        presenter.handleNoSelection();
    }

    /**
     * Sets an error message by delegating to the presenter.
     *
     * @param message The error message to display.
     */
    public void setError(String message) {
        presenter.displayError(message);
    }

    /**
     * Finds a request based on the item ID and username by delegating to the presenter.
     *
     * @param itemId   The ID of the requested item.
     * @param username The username of the requester.
     * @param callback The callback to handle the result.
     */
    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        // Delegate the logic to the presenter
        presenter.findRequest(itemId, username, new UserRepository.FindRequestCallback() {
            @Override
            public void onResult(boolean found, Request request) {
                if (found) {
                    // You can handle additional logic here if needed before passing to the callback
                    callback.onResult(true, request);
                } else {
                    callback.onResult(false, null);
                }
            }
        });
    }

    /**
     * Initializes a counteroffer request by delegating to the presenter.
     *
     * @param request     The original request.
     * @param counterItem The item offered as a counteroffer.
     * @param xchanger    The xChanger instance performing the counteroffer.
     */
    public void initializeCounterRequest(Request request, Item counterItem, xChanger xchanger) {
        presenter.createCounterOffer(request, counterItem,xchanger);
    }
}
