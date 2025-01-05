package com.example.xchange.CounterOffer;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import java.util.List;

public class CounterofferPresenter {

    private MutableLiveData<String> requesterText;
    private MutableLiveData<String> requesteeText;
    private MutableLiveData<String> requestedItemText;
    private MutableLiveData<List<Item>> spinnerItems;
    private MutableLiveData<String> selectedItemText;
    private MutableLiveData<String> errorMessage;
    private UserRepository userRepository;

    public CounterofferPresenter(MutableLiveData<String> requesterText, MutableLiveData<String> requesteeText, MutableLiveData<String> requestedItemText, MutableLiveData<List<Item>> spinnerItems, MutableLiveData<String> selectedItemText, MutableLiveData<String> errorMessage, Context context) {
        this.requesterText = requesterText;
        this.requesteeText = requesteeText;
        this.requestedItemText = requestedItemText;
        this.spinnerItems = spinnerItems;
        this.selectedItemText = selectedItemText;
        this.errorMessage = errorMessage;
        this.userRepository=new UserRepository(context);
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
    public void createCounterOffer(Request request, Item counterItem, xChanger xchanger) {
        if (request != null && counterItem != null) {
            try {
                xchanger.counterOffer(counterItem, request);
            } catch (Exception e) {
                Log.e("CounterofferPresenter", "Error creating counteroffer", e);
            }
        } else {
            Log.e("CounterofferPresenter", "Invalid data for creating counteroffer");
        }
    }



}
