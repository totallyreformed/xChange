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

public class CounterofferViewModel extends ViewModel {

    private MutableLiveData<String> requesterText = new MutableLiveData<>();
    private MutableLiveData<String> requesteeText = new MutableLiveData<>();
    private MutableLiveData<String> requestedItemText = new MutableLiveData<>();
    private MutableLiveData<List<Item>> spinnerItems = new MutableLiveData<>();
    private MutableLiveData<String> selectedItemText = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private CounterofferPresenter presenter;

    public CounterofferViewModel(Context context) {
        // Initialize presenter and pass LiveData references
        presenter = new CounterofferPresenter(requesterText, requesteeText, requestedItemText, spinnerItems, selectedItemText, errorMessage,context);
    }

    // LiveData Getters
    public LiveData<String> getRequesterText() {
        return requesterText;
    }

    public LiveData<String> getRequesteeText() {
        return requesteeText;
    }

    public LiveData<String> getRequestedItemText() {
        return requestedItemText;
    }

    public LiveData<List<Item>> getSpinnerItems() {
        return spinnerItems;
    }

    public LiveData<String> getSelectedItemText() {
        return selectedItemText;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Delegate request details to presenter
    public void setRequestDetails(Request request) {
        presenter.processRequestDetails(request);
    }

    // Delegate spinner population to presenter
    public void populateSpinner(List<Item> items) {
        presenter.populateSpinnerWithItems(items);
    }

    // Delegate item selection to presenter
    public void handleItemSelection(Item selectedItem) {
        presenter.handleSelectedItem(selectedItem);
    }

    // Delegate no selection case to presenter
    public void handleNoSelection() {
        presenter.handleNoSelection();
    }

    // Delegate error handling to presenter
    public void setError(String message) {
        presenter.displayError(message);
    }
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
    public void initializeCounterRequest(Request request, Item counterItem, xChanger xchanger) {
        presenter.createCounterOffer(request, counterItem,xchanger);
    }
}
