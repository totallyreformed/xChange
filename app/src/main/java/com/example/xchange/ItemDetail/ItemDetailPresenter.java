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

/**
 * The {@code ItemDetailPresenter} serves as the presenter in the MVP architecture.
 * It interacts with the database and business logic to fetch, modify, and manage
 * item-related data. The class also ensures UI updates are performed on the main thread.
 */
public class ItemDetailPresenter {

    private final Executor executor;
    private final Handler mainThreadHandler;
    private final ItemDao itemDao;
    private final RequestDao requestDao;
    private final UserRepository repository;

    /**
     * Constructs an instance of {@code ItemDetailPresenter} and initializes database access objects.
     *
     * @param context The context required to initialize the database and repository.
     */
    public ItemDetailPresenter(Context context) {
        executor = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());

        AppDatabase db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
        requestDao = AppDatabase.getRequestDao();
        repository = new UserRepository(context);
    }

    /**
     * Callback interface for fetching an {@link Item}.
     */
    public interface ItemCallback {
        void onItemFetched(Item item);
    }

    /**
     * Fetches an item by its ID from the database.
     *
     * @param itemId   The ID of the item to fetch.
     * @param callback The callback to return the fetched item.
     */
    public void getItemById(long itemId, ItemCallback callback) {
        executor.execute(() -> {
            Item item = itemDao.getItemByIdSync(itemId); // Assuming synchronous call exists
            mainThreadHandler.post(() -> callback.onItemFetched(item));
        });
    }

    /**
     * Callback interface for delete operation completion.
     */
    public interface DeleteCallback {
        void onDeleted();
    }

    /**
     * Deletes an item by its ID along with associated requests and counteroffers.
     *
     * @param itemId   The ID of the item to delete.
     * @param callback The callback to notify when the deletion is complete.
     */
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

    /**
     * Cancels a request for a specific item by the given user.
     *
     * @param itemId   The ID of the item associated with the request.
     * @param username The username of the user cancelling the request.
     */
    public void cancelRequest(long itemId, String username) {
        executor.execute(() -> {
            if (itemId > 0 && username != null && !username.isEmpty()) {
                repository.cancelItemRequest(itemId, username);
            }
        });
    }

    /**
     * Callback interface for fetching a request to determine accept/reject visibility.
     */
    public interface FetchRequestCallback {
        void onResult(boolean success, Request request);
    }

    /**
     * Checks if an accept/reject UI should be displayed for a request.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to notify the result.
     */
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

    /**
     * Callback interface for checking if a request exists.
     */
    public interface FetchResultCallback {
        void onResult(boolean result);
    }

    /**
     * Checks if a request for the specified item exists for the given user.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the requester.
     * @param callback The callback to notify the result.
     */
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

    /**
     * Finds a request for the specified item and user.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the requester.
     * @param callback The callback to return the result.
     */
    public void findRequest(long itemId, String username, UserRepository.FindRequestCallback callback) {
        repository.findRequest(itemId, username, callback);
    }

    /**
     * Finds items owned by the specified XChanger.
     *
     * @param xChangerUsername The username of the XChanger.
     * @param callback         The callback to return the result.
     */
    public void findItemsByXChanger(String xChangerUsername, UserRepository.UserItemsCallback callback) {
        repository.findItemsByXChanger(xChangerUsername, callback);
    }

    /**
     * Checks if the user is the requestee with a counteroffer for a specific item.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to return the counteroffer.
     */
    public void checkIfRequesteeWithCounteroffer(long itemId, String username, ItemDetailViewModel.CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesteeWithCounteroffer(itemId, username);
            mainThreadHandler.post(() -> callback.onResult(counteroffer));
        });
    }

    /**
     * Checks if the user is the requester with a counterofferee for a specific item.
     *
     * @param itemId   The ID of the item.
     * @param username The username of the user.
     * @param callback The callback to return the counteroffer.
     */
    public void checkIfRequesterWithCounterofferee(long itemId, String username, ItemDetailViewModel.CheckCounterofferCallback callback) {
        executor.execute(() -> {
            Counteroffer counteroffer = repository.checkIfRequesterWithCounterofferee(itemId, username);
            mainThreadHandler.post(() -> callback.onResult(counteroffer));
        });
    }
}
