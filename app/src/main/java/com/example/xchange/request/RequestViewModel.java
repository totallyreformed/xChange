package com.example.xchange.request;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.xChanger;

import java.util.List;

/**
 * ViewModel class for managing user items and requests in the xChange application.
 * <p>
 * This class provides LiveData for observing user items and methods to interact with the {@link RequestPresenter}
 * for loading user items and creating requests.
 * </p>
 */
public class RequestViewModel extends ViewModel {

    private final RequestPresenter presenter;
    private final MutableLiveData<List<Item>> userItemsLiveData = new MutableLiveData<>();

    /**
     * Constructor for initializing the RequestViewModel.
     *
     * @param context The application context.
     */
    public RequestViewModel(Context context) {
        presenter = new RequestPresenter(context, this);
    }

    /**
     * Retrieves the LiveData object for observing user items.
     *
     * @return LiveData containing the list of user items.
     */
    public LiveData<List<Item>> getUserItems() {
        return userItemsLiveData;
    }

    /**
     * Fetches the list of items belonging to the specified user.
     *
     * @param username The username of the user whose items are to be fetched.
     */
    public void fetchUserItems(String username) {
        presenter.loadUserItems(username);
    }

    /**
     * Sends a request from one user to another for exchanging items.
     *
     * @param requester     The {@link xChanger} creating the request.
     * @param requestee     The {@link xChanger} receiving the request.
     * @param offeredItem   The item being offered by the requester.
     * @param requestedItem The item being requested from the requestee.
     */
    public void sendRequest(xChanger requester, xChanger requestee, Item offeredItem, Item requestedItem) {
        presenter.createRequest(requester, requestee, offeredItem, requestedItem);
    }

    /**
     * Updates the LiveData object with the list of items belonging to the user.
     *
     * @param items The list of items to update.
     */
    void updateUserItems(List<Item> items) {
        userItemsLiveData.setValue(items);
    }
}