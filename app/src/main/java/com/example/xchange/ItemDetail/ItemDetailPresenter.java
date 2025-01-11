package com.example.xchange.ItemDetail;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemDetailPresenter {

    private final Executor executor;
    private final Handler mainThreadHandler;
    private final ItemDao itemDao;
    private final RequestDao requestDao;
    private final UserRepository repository;

    public ItemDetailPresenter(Context context) {
        executor = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());

        AppDatabase db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
        requestDao = AppDatabase.getRequestDao();
        repository = new UserRepository(context);
    }

    // -------------------- Fetch an Item --------------------
    public interface ItemCallback {
        void onItemFetched(Item item);
    }

    public void getItemById(long itemId, ItemCallback callback) {
        executor.execute(() -> {
            Item item = itemDao.getItemByIdSync(itemId); // Assuming synchronous call exists
            mainThreadHandler.post(() -> callback.onItemFetched(item));
        });
    }

    // -------------------- Delete an Item --------------------
    public interface DeleteCallback {
        void onDeleted();
    }

    public void deleteItemById(long itemId, DeleteCallback callback) {
        executor.execute(() -> {
            try {
                // Retrieve all requests and counteroffers associated with the item.
                List<Request> requests = requestDao.getAllRequests();
                List<Counteroffer> counteroffers = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                Item item = itemDao.getItemByIdSync(itemId);
                if (item != null) {
                    for (Request request : requests) {
                        if (item.equals(request.getOfferedItem()) || item.equals(request.getRequestedItem())) {
                            AppDatabase.getRequestDao().deleteRequest(request);
                        }
                    }
                    for (Counteroffer counter : counteroffers) {
                        if (item.equals(counter.getOfferedItem()) || item.equals(counter.getRequestedItem())) {
                            AppDatabase.getCounterofferDao().deleteCounteroffer(counter);
                        }
                    }
                    itemDao.deleteItemById(itemId);
                }
            } catch (Exception e) {
                // Optionally log the exception
            }
            mainThreadHandler.post(callback::onDeleted);
        });
    }

    // -------------------- Cancel a Request --------------------
    public void cancelRequest(long itemId, String username) {
        executor.execute(() -> {
            if (itemId > 0 && username != null && !username.isEmpty()) {
                repository.cancelItemRequest(itemId, username);
            }
        });
    }

    // -------------------- Check to Display Accept/Reject --------------------
    public interface FetchRequestCallback {
        void onResult(boolean success, Request request);
    }

    public void checkToDisplayAcceptReject(long itemId, String username, FetchRequestCallback callback) {
        executor.execute(() -> {
            Item item = itemDao.getItemByIdSync(itemId);
            if (item == null) {
                mainThreadHandler.post(() -> callback.onResult(false, null));
            } else {
                Request matchingRequest = requestDao.getAllRequests()
                        .stream()
                        .filter(req -> req.getRequestedItem() != null &&
                                req.getRequestedItem().equals(item) &&
                                username.equals(req.getRequestee().getUsername()))
                        .findFirst()
                        .orElse(null);
                mainThreadHandler.post(() -> callback.onResult(matchingRequest != null, matchingRequest));
            }
        });
    }

    // -------------------- Check if Request Exists --------------------
    public interface FetchResultCallback {
        void onResult(boolean result);
    }

    public void checkRequestToDisplay(long itemId, String username, FetchResultCallback callback) {
        executor.execute(() -> {
            Item item = itemDao.getItemByIdSync(itemId);
            if (item == null) {
                mainThreadHandler.post(() -> callback.onResult(false));
                return;
            }
            boolean found = requestDao.getAllRequests()
                    .stream()
                    .anyMatch(req -> req.getRequestedItem() != null &&
                            req.getRequestedItem().equals(item) &&
                            username.equals(req.getRequester().getUsername()));
            mainThreadHandler.post(() -> callback.onResult(found));
        });
    }

    // -------------------- Find a Request --------------------
    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        repository.findRequest(itemId, username, callback);
    }

    // -------------------- Find Items by XChanger --------------------
    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        repository.findItemsByXChanger(xChangerUsername, callback);
    }

    // -------------------- Check Counteroffer Related --------------------
    public void checkIfRequesteeWithCounteroffer(long itemId, String username, ItemDetailViewModel.CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesteeWithCounteroffer(itemId, username);
            mainThreadHandler.post(() -> callback.onResult(counteroffer));
        });
    }

    public void checkIfRequesterWithCounterofferee(long itemId, String username, ItemDetailViewModel.CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesterWithCounterofferee(itemId, username);
            mainThreadHandler.post(() -> callback.onResult(counteroffer));
        });
    }
}
