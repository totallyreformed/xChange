package com.example.xchange.ItemDetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemDetailViewModel extends AndroidViewModel {
    private final ItemDao itemDao;
    private final Executor executor;
    private final UserRepository repository;
    private final RequestDao requestDao;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        requestDao = AppDatabase.getRequestDao();
        repository = new UserRepository(application);
    }

    public LiveData<Item> getItemById(long itemId) {
        MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
        executor.execute(() -> {
            Item item = itemDao.getItemByIdSync(itemId); // Assuming getItemByIdSync exists for synchronous call
            if (item != null) {
                itemLiveData.postValue(item);
            } else {
                Log.e("ItemDetailViewModel", "Item with ID " + itemId + " not found.");
                itemLiveData.postValue(null); // Ensure observer is notified with null if no item is found
            }
        });
        return itemLiveData;
    }


    public void deleteItemById(long itemId) {
        executor.execute(() -> {
            try {
                // Fetch related requests and counteroffers
                List<Request> requests = requestDao.getAllRequests();
                List<Counteroffer> counteroffers = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                Item item = itemDao.getItemByIdSync(itemId);

                for (Request request : requests) {
                    if (item != null && (item.equals(request.getOfferedItem()) || item.equals(request.getRequestedItem()))) {
                        AppDatabase.getRequestDao().deleteRequest(request);
                    }
                }
                for (Counteroffer counter : counteroffers) {
                    if (item != null && (item.equals(counter.getOfferedItem()) || item.equals(counter.getRequestedItem()))) {
                        AppDatabase.getCounterofferDao().deleteCounteroffer(counter);
                    }
                }
                itemDao.deleteItemById(itemId);
            } catch (Exception e) {
            }
        });
    }


    public LiveData<User> getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    public void checkRequestToDisplay(long itemId, String username, FetchResultCallback callback) {
        getItemById(itemId).observeForever(item -> {
            if (item == null) {
                callback.onResult(false);
                return;
            }
            executor.execute(() -> {
                boolean found = requestDao.getAllRequests()
                        .stream()
                        .anyMatch(req -> req.getRequestedItem() != null
                                && req.getRequestedItem().equals(item)
                                && username.equals(req.getRequester().getUsername()));
                callback.onResult(found);
            });
        });
    }

    public void checkToDisplayAcceptReject(long itemId, String username, FetchRequestCallback callback) {
        getItemById(itemId).observeForever(item -> {
            if (item == null) {
                callback.onResult(false, null);
                return;
            }
            executor.execute(() -> {
                Request matchingRequest = requestDao.getAllRequests()
                        .stream()
                        .filter(req -> req.getRequestedItem() != null
                                && req.getRequestedItem().equals(item)
                                && username.equals(req.getRequestee().getUsername()))
                        .findFirst()
                        .orElse(null);
                callback.onResult(matchingRequest != null, matchingRequest);
            });
        });
    }

    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        repository.findRequest(itemId, username, callback);
    }

    public void cancelRequest(long itemId, String username) {
        if (itemId > 0 && username != null && !username.isEmpty()) {
            repository.cancelItemRequest(itemId, username);
        } else {
            Log.e("ItemDetailViewModel", "Invalid itemId or username for cancelRequest.");
        }
    }

    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        repository.findItemsByXChanger(xChangerUsername, callback);
    }

    public void checkIfRequesteeWithCounteroffer(long itemId, String username, CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesteeWithCounteroffer(itemId, username);
            callback.onResult(counteroffer);
        });
    }

    public void checkIfRequesterWithCounterofferee(long itemid,String username, CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesterWithCounterofferee(itemid,username); // Retrieve Counteroffer
            callback.onResult(counteroffer); // Pass Counteroffer object to the callback
        });
    }


    public void getOfferedItemForCounteroffer(long itemId, String username, ItemCallback callback) {
        executor.execute(() -> {
            Item offeredItem = repository.getOfferedItemForCounteroffer(itemId, username);
            if (offeredItem != null) {
                callback.onItemFetched(offeredItem);
            } else {
                callback.onFailure("No offered item found for counteroffer.");
            }
        });
    }

    // Callback Interfaces
    public interface FetchResultCallback {
        void onResult(boolean result);
    }

    public interface FetchRequestCallback {
        void onResult(boolean success, @Nullable Request request);
    }

    public interface ItemCallback {
        void onItemFetched(Item item);

        void onFailure(String message);
    }

    public interface CheckCounterofferCallback {
        void onResult(@Nullable Counteroffer counteroffer);
    }

}
