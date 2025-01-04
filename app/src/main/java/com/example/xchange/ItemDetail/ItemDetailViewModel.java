package com.example.xchange.ItemDetail;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;

import java.util.concurrent.Executor;
public class ItemDetailViewModel extends AndroidViewModel {
    private final ItemDao itemDao;
    private final Executor executor;
    private UserRepository repository;
    private final RequestDao requestDao;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        requestDao=AppDatabase.getRequestDao();
        this.repository=new UserRepository(application);
    }

    public LiveData<Item> getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }

    public void deleteItemById(long itemId) {
        executor.execute(() -> itemDao.deleteItemById(itemId));
    }
    public LiveData<User> getUserByUsername(String name){
        return this.repository.getUserByUsername(name);
    }

    public void checkRequestToDisplay(long itemId, String username, FetchResultCallback callback) {
        LiveData<Item> itemLiveData = getItemById(itemId);
        itemLiveData.observeForever(item -> {
            if (item == null) {
                callback.onResult(false);
                return;
            }
            executor.execute(() -> {
                List<Request> requests = requestDao.getAllRequests();
                for (Request req : requests) {
                    if (req.getRequestedItem() != null && req.getRequestedItem().equals(item) &&
                            username.equals(req.getRequester().getUsername())) {
                        callback.onResult(true);
                        return;
                    }
                }
                callback.onResult(false);
            });
        });
    }
    public void checkToDisplayAcceptReject(long itemId, String username, FetchRequestCallback callback) {
        LiveData<Item> itemLiveData = getItemById(itemId);
        itemLiveData.observeForever(item -> {
            if (item == null) {
                callback.onResult(false, null);
                return;
            }
            executor.execute(() -> {
                List<Request> requests = requestDao.getAllRequests();
                for (Request req : requests) {
                    if (req.getRequestedItem() != null && req.getRequestedItem().equals(item) &&
                            username.equals(req.getRequestee().getUsername())) {
                        callback.onResult(true, req);
                        return;
                    }
                }
                callback.onResult(false, null);
            });
        });
    }

    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        repository.findRequest(itemId, username, new UserRepository.FindRequestCallback() {
            @Override
            public void onResult(boolean success, @Nullable Request request) {
                if (success && request != null) {
                    callback.onResult(true, request);
                } else {
                    callback.onResult(false, null);
                }
            }
        });
    }


    public void cancelRequest(long itemId, String username) {
        if (itemId <= 0 || username == null || username.isEmpty()) {
            Log.e("ItemDetailViewModel", "Invalid itemId or username for cancelRequest.");
            return;
        }
        repository.cancelItemRequest(itemId, username);
    }
    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        repository.findItemsByXChanger(xChangerUsername, new UserRepository.UserItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                callback.onSuccess(items);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }




    // Callback interface
    public interface FetchResultCallback {
        void onResult(boolean result);
    }
    public interface FetchRequestCallback {
        void onResult(boolean success, @Nullable Request request);
    }

}

