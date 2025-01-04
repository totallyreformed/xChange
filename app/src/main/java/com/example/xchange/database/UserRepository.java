package com.example.xchange.database;

import android.content.Context;
import android.net.DnsResolver;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;
import com.example.xchange.database.dao.UserDao;

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
    private final ExecutorService executor;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        itemDao = db.itemDao();
        requestDao = db.requestDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onFailure(String message);
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
                List<Request> requests = requestDao.getAllSentRequests();
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



    // Get Received Requests (For Admin)
    public void getReceivedRequests(RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                // Assuming you have a way to identify Admin's username or use a global query
                // For simplicity, fetch all received requests
                List<Request> requests = requestDao.getAllReceivedRequests(); // Implement this in RequestDao
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

                // No matching request found
                Log.d("FindRequest", "No matching request found in Repository.");
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



}