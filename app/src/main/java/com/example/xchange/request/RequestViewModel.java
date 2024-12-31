// RequestViewModel.java
package com.example.xchange.request;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Item;
import com.example.xchange.User;

import java.util.List;

public class RequestViewModel extends ViewModel {

    private final RequestPresenter presenter;
    private final MutableLiveData<List<Item>> userItemsLiveData = new MutableLiveData<>();

    public RequestViewModel(Context context) {
        presenter = new RequestPresenter(context, this);
    }

    public LiveData<List<Item>> getUserItems() {
        return userItemsLiveData;
    }

    public void fetchUserItems(String username) {
        presenter.loadUserItems(username);
    }

    public void sendRequest(User requester, User requestee, Item offeredItem, Item requestedItem) {
        presenter.createRequest(requester, requestee, offeredItem, requestedItem);
    }

    void updateUserItems(List<Item> items) {
        userItemsLiveData.setValue(items);
    }
}