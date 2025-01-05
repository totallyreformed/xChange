package com.example.xchange.CounterOffer;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.Request;

import java.util.List;

public class CounterofferPresenter {

    private MutableLiveData<String> requesterText;
    private MutableLiveData<String> requesteeText;
    private MutableLiveData<String> requestedItemText;
    private MutableLiveData<List<Item>> spinnerItems;
    private MutableLiveData<String> selectedItemText;
    private MutableLiveData<String> errorMessage;

    public CounterofferPresenter(
            MutableLiveData<String> requesterText,
            MutableLiveData<String> requesteeText,
            MutableLiveData<String> requestedItemText,
            MutableLiveData<List<Item>> spinnerItems,
            MutableLiveData<String> selectedItemText,
            MutableLiveData<String> errorMessage
    ) {
        this.requesterText = requesterText;
        this.requesteeText = requesteeText;
        this.requestedItemText = requestedItemText;
        this.spinnerItems = spinnerItems;
        this.selectedItemText = selectedItemText;
        this.errorMessage = errorMessage;
    }

    public void processRequestDetails(Request request) {
        if (request != null) {
            requesterText.setValue("Requester: " + request.getRequester().getUsername());
            requesteeText.setValue("Requestee: " + request.getRequestee().getUsername());
            requestedItemText.setValue("Requested Item: " + request.getRequestedItem().getItemName());
        } else {
            displayError("Request is null!");
        }
    }

    public void populateSpinnerWithItems(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            spinnerItems.setValue(items);
        } else {
            displayError("No items available to populate the spinner.");
        }
    }

    public void handleSelectedItem(Item selectedItem) {
        if (selectedItem != null) {
            selectedItemText.setValue("Selected Item: " + selectedItem.getItemName());
        } else {
            displayError("No item selected!");
        }
    }

    public void handleNoSelection() {
        displayError("No item selected in spinner.");
    }

    public void displayError(String message) {
        errorMessage.setValue(message);
    }
}
