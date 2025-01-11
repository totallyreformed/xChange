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

public class ItemDetailViewModel extends AndroidViewModel {

    private final ItemDetailPresenter presenter;
    private final UserRepository repository;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        presenter = new ItemDetailPresenter(application);
        repository = new UserRepository(application);
    }

    public LiveData<Item> getItemById(long itemId) {
        MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
        presenter.getItemById(itemId, itemLiveData::setValue);
        return itemLiveData;
    }

    public void deleteItemById(long itemId) {
        presenter.deleteItemById(itemId, () -> {
            // You can add post-deletion actions here if needed.
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    public void checkRequestToDisplay(long itemId, String username, ItemDetailPresenter.FetchResultCallback callback) {
        presenter.checkRequestToDisplay(itemId, username, callback);
    }

    public void checkToDisplayAcceptReject(long itemId, String username, ItemDetailPresenter.FetchRequestCallback callback) {
        presenter.checkToDisplayAcceptReject(itemId, username, callback);
    }

    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        presenter.findRequest(itemId, username, callback);
    }

    public void cancelRequest(long itemId, String username) {
        presenter.cancelRequest(itemId, username);
    }

    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        presenter.findItemsByXChanger(xChangerUsername, callback);
    }

    public void checkIfRequesteeWithCounteroffer(long itemId, String username, CheckCounterofferCallback callback) {
        presenter.checkIfRequesteeWithCounteroffer(itemId, username, callback);
    }

    public void checkIfRequesterWithCounterofferee(long itemId, String username, CheckCounterofferCallback callback) {
        presenter.checkIfRequesterWithCounterofferee(itemId, username, callback);
    }

    // -------------------- Callback Interface --------------------
    public interface CheckCounterofferCallback {
        void onResult(@Nullable Counteroffer counteroffer);
    }
}
