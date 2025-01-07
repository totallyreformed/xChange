package com.example.xchange.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.xchange.Category;
import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.dao.CounterofferDao;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;
import com.example.xchange.database.dao.UserDao;
import com.example.xchange.database.dao.xChangeDao;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserRepository {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final RequestDao requestDao;
    private final CounterofferDao counterofferDao;
    private final xChangeDao xChangeDao;
    private final ExecutorService executor;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        itemDao = db.itemDao();
        requestDao = db.requestDao();
        counterofferDao = db.getCounterofferDao();
        xChangeDao = db.xChangeDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onFailure(String message);
    }
    public interface UserCounterOffersCallback {
        void onSuccess(List<Counteroffer> counterOffers);
        void onFailure(String message);
    }
    public interface ItemCallback {
        void onItemFetched(Item item);
    }
    public interface RegisterCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public interface UserStatisticsCallback {
        void onSuccess(String stats);
        void onFailure(String message);
    }

    public interface FindRequestCallback {
        void onResult(boolean success, @Nullable Request request);
    }
    public interface UserItemsCallback {
        void onSuccess(List<Item> items);
        void onFailure(String message);
    }
    public interface SaveRequestCallback {
        void onSuccess();
        void onFailure(String message);
    }
    public interface UserRequestsCallback {
        void onSuccess(int count);
        void onFailure(String message);
    }
    public interface UserRequestsReceivedCallback {
        void onSuccess(List<Request> requests);
        void onFailure(String message);
    }
    public interface UserRequestsSentCallback {
        void onSuccess(List<Request> requests);
        void onFailure(String message);
    }
    public interface CheckCounterofferCallback {
        void onResult(boolean success);
    }

    // Interface for AcceptRequest Callback
    public interface AcceptRequestCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public interface InsertXChangeCallback {
        void onSuccess(long xChangeId);
        void onFailure(String message);
    }

    public interface OperationCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public interface RequestItemsCallback {
        void onSuccess(List<Request> requests);
        void onFailure(String message);
    }

    public void loginUser(String username, String password, LoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.loginUser(username, password);
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onFailure("Invalid credentials");
            }
        });
    }

    public void registerUser(User newUser, RegisterCallback callback) {
        executor.execute(() -> {
            try {
                User existingUser = userDao.findByUsername_initial(newUser.getUsername());
                if (existingUser != null) {
                    callback.onFailure("Username already exists");
                } else {
                    userDao.insertUser(newUser);
                    callback.onSuccess();
                }
            } catch (Exception e) {
                callback.onFailure("Registration failed: " + e.getMessage());
            }
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void getUserStatistics(String username, UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int itemCount = userDao.getItemCountByUsername(username); // Ensure this method exists in UserDao
                String stats = "Items Listed: " + itemCount;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve statistics");
            }
        });
    }

    public void getItemsByUsername(String username, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = userDao.getItemsByUsername(username);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve user items.");
            }
        });
    }

    // Accept Request
    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        executor.execute(() -> {
            try {
                // Update the request to inactive
                request.make_unactive();
                requestDao.updateRequest(request);

                // Retrieve the requester and requestee from the request
                xChanger requester = (xChanger) request.getRequester();
                xChanger requestee = (xChanger) request.getRequestee();

                // Update requestee's side (e.g., add rating)
                requestee.acceptRequest(request, rating);
                userDao.updateUser(requestee); // Ensure the requestee's data is updated in the database

                // Optionally, update requester if there are any changes on their side
                userDao.updateUser(requester);

                // Create a new xChange entry
                xChange newXChange = new xChange(request, null, null); // Adjust parameters as needed
                // Insert the xChange into the database
                long xChangeId = xChangeDao.insertXChange(newXChange);
                newXChange.setXChangeId(xChangeId); // Set the generated ID

                // Optionally, update xChange with additional details if necessary
                xChangeDao.updateXChange(newXChange);

                // Notify or update other parts of the system
                callback.onSuccess();
            } catch (Exception e) {
                Log.e("UserRepository", "Error accepting request", e);
                callback.onFailure("Failed to accept request.");
            }
        });
    }

    // Insert xChange
    public void insertXChange(xChange xchange, InsertXChangeCallback callback) {
        executor.execute(() -> {
            try {
                long id = xChangeDao.insertXChange(xchange);
                xchange.setXChangeId(id);
                xChangeDao.updateXChange(xchange);
                callback.onSuccess(id);
            } catch (Exception e) {
                Log.e("UserRepository", "Error inserting xChange", e);
                callback.onFailure("Failed to insert xChange.");
            }
        });
    }

    // Example: Retrieve xChange by ID
    public LiveData<xChange> getXChangeById(long id) {
        return xChangeDao.getXChangeById(id);
    }

    // Example: Retrieve all xChanges
    public LiveData<List<xChange>> getAllXChanges() {
        return xChangeDao.getAllXChanges();
    }

    // Example: Mark xChange as Inactive
    public void markXChangeAsInactive(long id, OperationCallback callback) {
        executor.execute(() -> {
            try {
                xChangeDao.markXChangeAsInactive(id);
                callback.onSuccess();
            } catch (Exception e) {
                Log.e("UserRepository", "Error marking xChange as inactive", e);
                callback.onFailure("Failed to mark xChange as inactive.");
            }
        });
    }

    // Get All Items (For Admin)
    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    // Get Total Item Count (For Admin)
    public LiveData<Integer> getItemCount() {
        return itemDao.getItemCount();
    }

    // Get Total Requests sent count (For Admin)
    public LiveData<Integer> getSentRequestsCount() {
        return requestDao.getRequestsSentCount();
    }

    // Get Total Request Received Count (For Admin)
    public LiveData<Integer> getReceivedRequestsCount() {
        return requestDao.getRequestsReceivedCount();
    }

    public void searchItemsByName(String query, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = itemDao.searchItemsByName(query);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onFailure("Error searching items by name.");
            }
        });
    }

    public void filterItemsByCategory(Category category, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = itemDao.filterItemsByCategory(category);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onFailure("Error filtering items by category.");
            }
        });
    }

    public void searchItemsByNameAndCategory(String query, Category category, UserItemsCallback callback) {
        new Thread(() -> {
            try {
                List<Item> items = itemDao.searchItemsByNameAndCategory(query, category);
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onSuccess(items);
                });

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("Error searching items"));
            }
        }).start();
    }
    public void saveRequest(Request request, SaveRequestCallback callback) {
        executor.execute(() -> {
            try {
                long requestId = AppDatabase.getRequestDao().insertRequest(request);
                if (requestId > 0) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to insert request.");
                }
            } catch (Exception e) {
                callback.onFailure("Error saving request: " + e.getMessage());
            }
        });
    }


    public void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    public void getSentRequestsCount(String username, UserRequestsCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = AppDatabase.getRequestDao().getAllRequests();
                int count=0;
                for(Request req:requests){
                    if(Objects.equals(req.getRequester().getUsername(), username)){
                        count++;
                    }
                }
                callback.onSuccess(count);
            } catch (Exception e) {
                callback.onFailure("Failed to fetch sent requests count: " + e.getMessage());
            }
        });

    }

    // Get Sent Requests (For Admin)
    public void getSentRequests(RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = requestDao.getAllSentRequestsAdmin();
                callback.onSuccess(requests);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve sent requests.");
            }
        });
    }

    public void getReceivedRequestsCount(String username, UserRequestsCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = AppDatabase.getRequestDao().getAllRequests();
                int count=0;
                for(Request req:requests){
                    if(Objects.equals(req.getRequestee().getUsername(), username)){
                        count++;
                    }
                }
                callback.onSuccess(count);
            } catch (Exception e) {
                callback.onFailure("Failed to fetch received requests count: " + e.getMessage());
            }
        });
    }
    public void getRequestsReceived(String username, UserRequestsReceivedCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = AppDatabase.getRequestDao().getAllRequests();
                List<Request> receivedRequests = new ArrayList<>();
                for (Request req : requests) {
                    if (Objects.equals(req.getRequestee().getUsername(), username)) {
                        receivedRequests.add(req);
                    }
                }
                callback.onSuccess(receivedRequests);
            } catch (Exception e) {
                callback.onFailure("Failed to fetch received requests: " + e.getMessage());
            }
        });
    }
    public void getSentRequests(String username, UserRequestsSentCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = AppDatabase.getRequestDao().getAllRequests();
                List<Request> sentRequests = new ArrayList<>();
                for (Request req : requests) {
                    if (Objects.equals(req.getRequester().getUsername(), username)) {
                        sentRequests.add(req);
                    }
                }
                callback.onSuccess(sentRequests);
            } catch (Exception e) {
                callback.onFailure("Failed to fetch sent requests: " + e.getMessage());
            }
        });
    }

    // New Method: Get Active Received Requests
    public void getActiveReceivedRequests(String username, UserRepository.RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                LiveData<List<Request>> liveData = requestDao.getAllReceivedRequests(username);
                // Observe LiveData on the main thread
                new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(requests -> {
                    callback.onSuccess(requests);
                }));
            } catch (Exception e) {
                callback.onFailure("Failed to fetch received requests: " + e.getMessage());
            }
        });
    }

    // New Method: Get Active Sent Requests
    public void getActiveSentRequests(String username, UserRepository.RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                LiveData<List<Request>> liveData = requestDao.getAllSentRequests(username);
                // Observe LiveData on the main thread
                new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(requests -> {
                    callback.onSuccess(requests);
                }));
            } catch (Exception e) {
                callback.onFailure("Failed to fetch sent requests: " + e.getMessage());
            }
        });
    }



    // Get Received Requests (For Admin)
    public void getReceivedRequestsAdmin(RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Request> requests = requestDao.getAllReceivedRequestsAdmin();
                callback.onSuccess(requests);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve received requests.");
            }
        });
    }

    public void getTotalRequests(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalRequests = userDao.getTotalRequests();
                String stats = "Total Requests: " + totalRequests;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total requests");
            }
        });
    }

    public void getTotalExchanges(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalExchanges = userDao.getTotalExchanges();
                String stats = "Total Exchanges: " + totalExchanges;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total exchanges");
            }
        });
    }

    public void getTotalItems(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalItems = userDao.getTotalItems();
                String stats = "Total Items: " + totalItems;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total items");
            }
        });
    }

    public void getTotalCategories(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalCategories = userDao.getTotalCategories();
                String stats = "Total Categories: " + totalCategories;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total categories");
            }
        });
    }

    public void cancelItemRequest(long itemId, String username) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            LiveData<Item> itemLiveData = itemDao.getItemById(itemId);
            itemLiveData.observeForever(item -> {
                executor.execute(() -> {
                    try {
                        List<Request> requests = requestDao.getAllRequests();
                        Request tobedeleted = null;

                        for (Request req : requests) {
                            if (req.getRequestedItem() != null && req.getRequestedItem().equals(item) &&
                                    username.equals(req.getRequester().getUsername())) {
                                tobedeleted = req;
                                break;
                            }
                        }
                        List<Counteroffer> counters=AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                        for(Counteroffer counter:counters){
                            if (counter.getRequest() != null && counter.getRequest().equals(tobedeleted)) {
                                AppDatabase.getCounterofferDao().deleteCounteroffer(counter);
                            }
                        }
                        if (tobedeleted != null) {
                            requestDao.deleteRequest(tobedeleted);
                        }
                    } catch (Exception e) {
                        Log.e("UserRepository", "Error canceling request: ", e);
                    }
                });
            });
        });
    }

    public void findRequest(long itemId, String username, FindRequestCallback callback) {
        executor.execute(() -> {
            try {
                Item item = itemDao.getItemByIdSync(itemId); // Ensure this method exists or adjust accordingly
                if (item == null) {
                    callback.onResult(false, null);
                    return;
                }
                List<Request> requests = requestDao.getAllRequests();

                for (Request req : requests) {
                    if (req.getRequestedItem() != null &&
                            req.getRequestedItem().equals(item) &&
                            username.equals(req.getRequestee().getUsername())) {
                        callback.onResult(true, req);
                        return;
                    }
                }
                callback.onResult(false, null);
            } catch (Exception e) {
                Log.e("FindRequest", "Error finding request: ", e);
                callback.onResult(false, null);
            }
        });
    }
    public void findItemsByXChanger(String xChangerUsername, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = itemDao.getItemsByXChanger(xChangerUsername); // Synchronous method
                callback.onSuccess(items);
            } catch (Exception e) {
                Log.e("UserRepository", "Error finding items by xChanger: ", e);
                callback.onFailure("Failed to fetch items for xChanger.");
            }
        });
    }
    public void getCounterOffersSentCount(String username, UserRequestsCallback callback) {
        executor.execute(() -> {
            try {
                List<Counteroffer> counters = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                long count = counters.stream()
                        .filter(counter -> username.equals(counter.getCounterofferer().getUsername()))
                        .count();

                callback.onSuccess((int) count);
            } catch (Exception e) {
                Log.e("CounterOfferError", "Error fetching sent counter offers: " + e.getMessage(), e);
                callback.onFailure("Failed to fetch counter offers sent count: " + e.getMessage());
            }
        });
    }

    public void getCounterOffersReceivedCount(String username, UserRequestsCallback callback) {
        executor.execute(() -> {
            try {
                // Fetch all counteroffers synchronously
                List<Counteroffer> counters = AppDatabase.getCounterofferDao().getAllCounteroffersSync();

                long count = counters.stream()
                        .filter(counter -> username.equals(counter.getCounterofferee().getUsername()))
                        .count();

                // Return the count via the callback
                callback.onSuccess((int) count);
            } catch (Exception e) {
                callback.onFailure("Failed to fetch counter offers received count: " + e.getMessage());
            }
        });
    }
    public void getSentCounterOffers(String username, UserCounterOffersCallback callback) {
        executor.execute(() -> {
//            AppDatabase.getRequestDao().deleteAllRequests();
//            AppDatabase.getCounterofferDao().deleteAll();
            try {
                List<Counteroffer> counters = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                List<Counteroffer> sentCounterOffers = new ArrayList<>();
                for (Counteroffer counter : counters) {
                    if (username.equals(counter.getCounterofferer().getUsername())) {
                        sentCounterOffers.add(counter);
                    }
                }
                callback.onSuccess(sentCounterOffers);
            } catch (Exception e) {
                Log.e("CounterOfferError", "Error fetching sent counter offers: " + e.getMessage(), e);
                callback.onFailure("Failed to fetch sent counter offers: " + e.getMessage());
            }
        });
    }


    public void getReceivedCounterOffers(String username, UserCounterOffersCallback callback) {
        executor.execute(() -> {
            try {
                List<Counteroffer> counters = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                List<Counteroffer> receivedCounterOffers = new ArrayList<>();
                for (Counteroffer counter : counters) {
                    if (username.equals(counter.getCounterofferee().getUsername())) {
                        receivedCounterOffers.add(counter);
                    }
                }
                callback.onSuccess(receivedCounterOffers);
            } catch (Exception e) {
                Log.e("CounterOfferError", "Error fetching received counter offers: " + e.getMessage(), e);
                callback.onFailure("Failed to fetch received counter offers: " + e.getMessage());
            }
        });
    }
    public Counteroffer checkIfRequesteeWithCounteroffer(long itemId,String username) {
        try {
            List<Request> requests = requestDao.getAllRequests();
            Item item=itemDao.getItemByIdSync(itemId);
            for (Request req : requests) {
                if (req.getRequestee().getUsername().equals(username) && item.equals(req.getRequestedItem())) {
                    List<Counteroffer> counteroffers = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                    for (Counteroffer counter : counteroffers) {
                        if (counter.getCounterofferer().getUsername().equals(username)&& item.equals(req.getRequestedItem())) {
                            return counter; // Found a match
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("UserRepository", "Error checking requestee with counteroffer: " + e.getMessage(), e);
        }
        return null;
    }

    public Counteroffer checkIfRequesterWithCounterofferee(long itemId,String username) {
        try {
            List<Request> requests = requestDao.getAllRequests();
            Item item=itemDao.getItemByIdSync(itemId);
            for (Request req : requests) {
                if (req.getRequester().getUsername().equals(username)) {
                    List<Counteroffer> counteroffers = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                    for (Counteroffer counter : counteroffers) {
                        if (counter.getCounterofferee().getUsername().equals(username) && item.equals(req.getRequestedItem())) {
                            return counter; // Found a match
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("UserRepository", "Error checking requester with counterofferee: " + e.getMessage(), e);
        }
        return null;
    }
    public Item getOfferedItemForCounteroffer(long itemId, String username) {
        try {
            List<Request> requests = requestDao.getAllRequests();
            Item requestedItem = itemDao.getItemByIdSync(itemId);
            for (Request req : requests) {
                if (req.getRequestee().getUsername().equals(username) && requestedItem.equals(req.getRequestedItem())) {
                    List<Counteroffer> counteroffers = AppDatabase.getCounterofferDao().getAllCounteroffersSync();
                    for (Counteroffer counter : counteroffers) {
                        if (counter.getCounterofferer().getUsername().equals(username) && requestedItem.equals(req.getRequestedItem())) {
                            return counter.getOfferedItem(); // Return the offered item
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("UserRepository", "Error fetching offered item for counteroffer: " + e.getMessage(), e);
        }
        return null; // Return null if no match found
    }



}