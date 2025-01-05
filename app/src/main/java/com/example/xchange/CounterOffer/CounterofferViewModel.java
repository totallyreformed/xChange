package com.example.xchange.CounterOffer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Item;
import com.example.xchange.Request;

import java.util.List;

public class CounterofferViewModel extends ViewModel {

    private MutableLiveData<String> requesterText = new MutableLiveData<>();
    private MutableLiveData<String> requesteeText = new MutableLiveData<>();
    private MutableLiveData<String> requestedItemText = new MutableLiveData<>();
    private MutableLiveData<List<Item>> spinnerItems = new MutableLiveData<>();
    private MutableLiveData<String> selectedItemText = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private CounterofferPresenter presenter;

    public CounterofferViewModel() {
        // Initialize presenter and pass LiveData references
        presenter = new CounterofferPresenter(requesterText, requesteeText, requestedItemText, spinnerItems, selectedItemText, errorMessage);
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
}
