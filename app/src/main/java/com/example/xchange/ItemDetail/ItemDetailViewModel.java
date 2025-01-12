package com.example.xchange.ItemDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

/**
 * The {@code ItemDetailViewModel} class provides a bridge between the UI layer
 * and the data/business logic layer in the MVVM architecture.
 * It communicates with the {@link ItemDetailPresenter} and {@link UserRepository}
 * to perform item-related operations and exposes LiveData to the UI for reactive updates.
 */
public class ItemDetailViewModel extends AndroidViewModel {

    private final ItemDetailPresenter presenter;
    private final UserRepository repository;

    /**
     * Constructs an {@code ItemDetailViewModel} instance.
     *
     * @param application The application context used for initializing the presenter and repository.
     */
    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        presenter = new ItemDetailPresenter(application);
        repository = new UserRepository(application);
    }

    /**
     * Fetches an item by its ID as LiveData.
     *
     * @param itemId The ID of the item to fetch.
     * @return A {@link LiveData} object containing the {@link Item}.
     */
    public LiveData<Item> getItemById(long itemId) {
        MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
        presenter.getItemById(itemId, itemLiveData::setValue);
        return itemLiveData;
    }

    /**
     * Deletes an item by its ID.
     *
     * @param itemId The ID of the item to delete.
     */
    public void deleteItemById(long itemId) {
        presenter.deleteItemById(itemId, () -> {
            // You can add post-deletion actions here if needed.
        });
    }

    /**
     * Fetches a user by their username as LiveData.
     *
     * @param username The username of the user to fetch.
     * @return A {@link LiveData} object containing the {@link User}.
     */
    public LiveData<User> getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    /**
     * Checks if a request for the specified item exists for the given user.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the requester.
     * @param callback The callback to handle the result.
     */
    public void checkRequestToDisplay(long itemId, String username, ItemDetailPresenter.FetchResultCallback callback) {
        presenter.checkRequestToDisplay(itemId, username, callback);
    }

    /**
     * Checks whether the accept/reject UI should be displayed for a request.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to handle the result.
     */
    public void checkToDisplayAcceptReject(long itemId, String username, ItemDetailPresenter.FetchRequestCallback callback) {
        presenter.checkToDisplayAcceptReject(itemId, username, callback);
    }

    /**
     * Finds a request for the specified item and user.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the requester.
     * @param callback The callback to handle the result.
     */
    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        presenter.findRequest(itemId, username, callback);
    }

    /**
     * Cancels a request for the specified item by the given user.
     *
     * @param itemId   The ID of the item associated with the request.
     * @param username The username of the user cancelling the request.
     */
    public void cancelRequest(long itemId, String username) {
        presenter.cancelRequest(itemId, username);
    }

    /**
     * Finds items owned by a specific XChanger.
     *
     * @param xChangerUsername The username of the XChanger.
     * @param callback         The callback to handle the result.
     */
    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        presenter.findItemsByXChanger(xChangerUsername, callback);
    }

    /**
     * Checks if the user is the requestee with a counteroffer for a specific item.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to handle the counteroffer.
     */
    public void checkIfRequesteeWithCounteroffer(long itemId, String username, CheckCounterofferCallback callback) {
        presenter.checkIfRequesteeWithCounteroffer(itemId, username, callback);
    }

    /**
     * Checks if the user is the requester with a counterofferee for a specific item.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to handle the counteroffer.
     */
    public void checkIfRequesterWithCounterofferee(long itemId, String username, CheckCounterofferCallback callback) {
        presenter.checkIfRequesterWithCounterofferee(itemId, username, callback);
    }

    /**
     * Callback interface for checking counteroffers.
     */
    public interface CheckCounterofferCallback {
        void onResult(@Nullable Counteroffer counteroffer);
    }
}
