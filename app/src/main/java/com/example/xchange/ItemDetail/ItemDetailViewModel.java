package com.example.xchange.ItemDetail;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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

    public void fetchItemAndRequests(long itemId, String username, FetchResultCallback callback) {
        // Observe LiveData properly
        LiveData<Item> itemLiveData = getItemById(itemId);
        itemLiveData.observeForever(item -> {
            if (item == null) {
                Log.e("Debug", "Item is null for ID: " + itemId);
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

    // Callback interface
    public interface FetchResultCallback {
        void onResult(boolean result);
    }





}

