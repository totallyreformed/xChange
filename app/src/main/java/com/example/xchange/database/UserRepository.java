package com.example.xchange.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.xchange.Category;
import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.dao.CounterofferDao;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;
import com.example.xchange.database.dao.UserDao;
import com.example.xchange.database.dao.xChangeDao;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;
import com.example.xchange.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Repository class for managing interactions between the application and the database.
 * It handles operations for {@link User}, {@link Item}, {@link Request}, {@link Counteroffer},
 * {@link Notification}, and {@link xChange}.
 */
public class UserRepository {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final RequestDao requestDao;
    private final CounterofferDao counterofferDao;
    private final xChangeDao xChangeDao;
    private final ExecutorService executor;

    /**
     * Constructor to initialize the repository with required DAOs and an executor.
     *
     * @param context the application context used to access the database.
     */
    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        itemDao = db.itemDao();
        requestDao = db.requestDao();
        counterofferDao = db.getCounterofferDao();
        xChangeDao = db.xChangeDao();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface for handling login operations.
     */
    public interface LoginCallback {
        /**
         * Called when the login operation is successful.
         *
         * @param user the {@link User} object representing the logged-in user.
         */
        void onSuccess(User user);
        /**
         * Called when the login operation fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for handling notifications.
     */
    public interface NotificationCallback {
        /**
         * Called when notifications are successfully retrieved.
         *
         * @param notifications the list of {@link Notification} objects.
         */
        void onSuccess(List<Notification> notifications);
        /**
         * Called when notification retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for loading a counteroffer.
     */
    public interface LoadCounterofferCallback {
        void onSuccess(Counteroffer counteroffer);
        void onFailure(String message);
    }

    /**
     * Callback interface for handling user counteroffers.
     */
    public interface UserCounterOffersCallback {
        /**
         * Called when counteroffers are successfully retrieved.
         *
         * @param counterOffers the list of {@link Counteroffer} objects.
         */
        void onSuccess(List<Counteroffer> counterOffers);
        /**
         * Called when counteroffer retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for fetching an item.
     */
    public interface ItemCallback {
        /**
         * Called when an item is successfully fetched.
         *
         * @param item the {@link Item} object.
         */
        void onItemFetched(Item item);
    }

    /**
     * Callback interface for handling user registration operations.
     */
    public interface RegisterCallback {
        /**
         * Called when registration is successful.
         */
        void onSuccess();
        /**
         * Called when registration fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for retrieving user statistics.
     */
    public interface UserStatisticsCallback {
        /**
         * Called when user statistics are successfully retrieved.
         *
         * @param stats a descriptive string of user statistics.
         */
        void onSuccess(String stats);
        /**
         * Called when user statistics retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for finding a request.
     */
    public interface FindRequestCallback {
        /**
         * Called when the request is successfully found or not found.
         *
         * @param success a boolean indicating whether the request was found.
         * @param request the {@link Request} object, or null if not found.
         */
        void onResult(boolean success, @Nullable Request request);
    }

    /**
     * Callback interface for retrieving user items.
     */
    public interface UserItemsCallback {
        /**
         * Called when items are successfully retrieved.
         *
         * @param items the list of {@link Item} objects.
         */
        void onSuccess(List<Item> items);
        /**
         * Called when item retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for saving a request.
     */
    public interface SaveRequestCallback {
        /**
         * Called when the request is successfully saved.
         */
        void onSuccess();
        /**
         * Called when the save operation fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for counting user requests.
     */
    public interface UserRequestsCallback {
        /**
         * Called when the request count is successfully retrieved.
         *
         * @param count the count of user requests.
         */
        void onSuccess(int count);
        /**
         * Called when the request count retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for retrieving received requests.
     */
    public interface UserRequestsReceivedCallback {
        /**
         * Called when received requests are successfully retrieved.
         *
         * @param requests the list of {@link Request} objects.
         */
        void onSuccess(List<Request> requests);
        /**
         * Called when received request retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for retrieving sent requests.
     */
    public interface UserRequestsSentCallback {
        /**
         * Called when sent requests are successfully retrieved.
         *
         * @param requests the list of {@link Request} objects.
         */
        void onSuccess(List<Request> requests);
        /**
         * Called when sent request retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for checking counteroffers.
     */
    public interface CheckCounterofferCallback {
        /**
         * Called when the counteroffer check operation completes.
         *
         * @param success a boolean indicating whether the check was successful.
         */
        void onResult(boolean success);
    }

    /**
     * Callback interface for accepting a request.
     */
    public interface AcceptRequestCallback {
        /**
         * Called when a request is successfully accepted.
         *
         * @param xChangeId the ID of the resulting {@link xChange}.
         */
        void onSuccess(long xChangeId);
        /**
         * Called when accepting the request fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for rejecting a request.
     */
    public interface RejectRequestCallback {
        /**
         * Called when a request is successfully rejected.
         */
        void onSuccess();
        /**
         * Called when rejecting the request fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for inserting an xChange.
     */
    public interface InsertXChangeCallback {
        /**
         * Called when an xChange is successfully inserted.
         *
         * @param xChangeId the ID of the inserted {@link xChange}.
         */
        void onSuccess(long xChangeId);
        /**
         * Called when inserting the xChange fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Generic callback interface for operations with no return value.
     */
    public interface OperationCallback {
        /**
         * Called when the operation is successful.
         */
        void onSuccess();
        /**
         * Called when the operation fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for retrieving requests.
     */
    public interface RequestItemsCallback {
        /**
         * Called when requests are successfully retrieved.
         *
         * @param requests the list of {@link Request} objects.
         */
        void onSuccess(List<Request> requests);
        /**
         * Called when request retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }

    /**
     * Callback interface for retrieving user xChanges.
     */
    public interface UserXChangesCallback {
        /**
         * Called when xChanges are successfully retrieved.
         *
         * @param xChanges the list of {@link xChange} objects.
         */
        void onSuccess(List<xChange> xChanges);
        /**
         * Called when xChange retrieval fails.
         *
         * @param message a descriptive error message.
         */
        void onFailure(String message);
    }


    /**
     * Logs in a user with the given username and password.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Registers a new user.
     *
     * @param newUser  the {@link User} object containing user details.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search for.
     * @return a {@link LiveData} object containing the user details.
     */
    public LiveData<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Retrieves user statistics such as item count.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves all items listed by a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Accepts a trade request, creating an {@link xChange} entry.
     *
     * @param request  the {@link Request} to be accepted.
     * @param rating   the rating provided for the trade.
     * @param callback the callback to handle success or failure.
     */
    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        executor.execute(() -> {
            try {
                xChanger requestee = request.getRequestee();
                requestee.acceptRequest(request, rating);
                userDao.updateUser(requestee);

                SimpleCalendar today = SimpleCalendar.today();
                xChange newXChange = new xChange(request, null, today);
                requestDao.deleteRequest(request);
                newXChange.acceptOffer(rating);
                long xChangeId = xChangeDao.insertXChange(newXChange);
                newXChange.setXChangeId(xChangeId);

                xChangeDao.updateXChange(newXChange);
                callback.onSuccess(xChangeId);
                // Pass the xChangeId to the callback
            } catch (Exception e) {
                Log.e("UserRepository", "Error accepting request", e);
                callback.onFailure("Failed to accept request.");
            }
        });
    }

    /**
     * Accepts a counteroffer, creating an {@link xChange} entry.
     *
     * @param counteroffer the {@link Counteroffer} to accept.
     * @param rating       the rating provided for the trade.
     * @param callback     the callback to handle success or failure.
     */
    public void acceptCounteroffer(Counteroffer counteroffer, float rating, AcceptRequestCallback callback) {
        executor.execute(() -> {
            try {
                xChanger counterofferee = counteroffer.getCounterofferee();
                counterofferee.acceptCounteroffer(counteroffer, 0);
                userDao.updateUser(counterofferee);

                SimpleCalendar today = SimpleCalendar.today();
                xChange newXChange = new xChange(counteroffer.getRequest(), counteroffer, today);
                requestDao.deleteRequest(counteroffer.getRequest());
                counterofferDao.deleteCounteroffer(counteroffer);

                newXChange.acceptOffer(rating);
                long xChangeId = xChangeDao.insertXChange(newXChange);
                newXChange.setXChangeId(xChangeId);

                callback.onSuccess(xChangeId);
            } catch (Exception e) {
                Log.e("UserRepository", "Error accepting counteroffer", e);
                callback.onFailure("Failed to accept counteroffer.");
            }
        });
    }

    /**
     * Rejects a trade request.
     *
     * @param xchanger the {@link xChanger} rejecting the request.
     * @param request  the {@link Request} to reject.
     * @param callback the callback to handle success or failure.
     */
    public void rejectRequest(xChanger xchanger, Request request, RejectRequestCallback callback) {
        executor.execute(() -> {
            try {
                xchanger.rejectRequest(request, 0);
                requestDao.deleteRequest(request);

                xChanger requester = request.getRequester();
                xChanger requestee = request.getRequestee();
                userDao.updateUser(requester);
                userDao.updateUser(requestee);

                SimpleCalendar today = SimpleCalendar.today();
                xChange newXChange = new xChange(request, null, today);
                requestDao.deleteRequest(request);
                newXChange.rejectOffer(0.0f);
                long xChangeId = xChangeDao.insertXChange(newXChange);
                newXChange.setXChangeId(xChangeId);

                callback.onSuccess();
            } catch (Exception e) {
                Log.e("UserRepository", "Error rejecting request", e);
                callback.onFailure("Failed to reject request.");
            }
        });
    }

    /**
     * Rejects a counteroffer.
     *
     * @param counteroffer the {@link Counteroffer} to reject.
     * @param callback     the callback to handle success or failure.
     */
    public void rejectCounteroffer(Counteroffer counteroffer, RejectRequestCallback callback) {
        executor.execute(() -> {
            try {
                xChanger counterofferee = counteroffer.getCounterofferee();
                counterofferee.rejectCounteroffer(counteroffer, 0);
                counterofferDao.deleteCounteroffer(counteroffer);
                requestDao.deleteRequest(counteroffer.getRequest());
                userDao.updateUser(counterofferee);
                SimpleCalendar today = SimpleCalendar.today();
                xChange newXChange = new xChange(counteroffer.getRequest(), counteroffer, today);
                requestDao.deleteRequest(counteroffer.getRequest());
                newXChange.rejectOffer(0.0f);
                long xChangeId = xChangeDao.insertXChange(newXChange);
                newXChange.setXChangeId(xChangeId);

                callback.onSuccess();
            }
            catch (Exception e) {
                Log.e("UserRepository", "Error rejecting request", e);
                callback.onFailure("Failed to reject request.");
            }
        });
    }

    /**
     * Retrieves the total number of users.
     *
     * @param callback the callback to handle success or failure.
     */
    public void getTotalUsers(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalUsers = userDao.getTotalUsers();
                String stats = "Total Users: " + totalUsers;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total categories");
            }
        });
    }

    /**
     * Adds a notification to the database.
     *
     * @param notification the {@link Notification} to add.
     * @param callback     the callback to handle success or failure.
     */
    public void addNotification(Notification notification, OperationCallback callback) {
        executor.execute(() -> {
            try {
                AppDatabase.getNotificationDao().insertNotification(notification);
                callback.onSuccess();
            } catch (Exception e) {
                Log.e("UserRepository", "Error adding notification", e);
                callback.onFailure("Failed to add notification.");
            }
        });
    }

    /**
     * Retrieves notifications for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
    public void getNotificationsForUser(String username, UserRepository.NotificationCallback callback) {
        executor.execute(() -> {
            try {
                List<Notification> notifications = AppDatabase.getNotificationDao().getNotificationsForUser(username);
                callback.onSuccess(notifications);
            } catch (Exception e) {
                Log.e("UserRepository", "Error retrieving notifications", e);
                callback.onFailure("Failed to retrieve notifications.");
            }
        });
    }

    /**
     * Deletes all notifications for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
    public void deleteNotificationsForUser(String username, OperationCallback callback) {
        executor.execute(() -> {
            try {
                AppDatabase.getNotificationDao().deleteNotificationsForUser(username);
                callback.onSuccess();
            } catch (Exception e) {
                Log.e("UserRepository", "Error deleting notifications", e);
                callback.onFailure("Failed to delete notifications.");
            }
        });
    }


    /**
     * Inserts a new {@link xChange} into the database and updates its ID.
     *
     * @param xchange  the {@link xChange} object to insert.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves an {@link xChange} by its ID.
     *
     * @param id the ID of the {@link xChange}.
     * @return a {@link LiveData} object containing the {@link xChange}.
     */
    public LiveData<xChange> getXChangeById(long id) {
        return xChangeDao.getXChangeById(id);
    }

    /**
     * Retrieves a list of {@link xChange} objects associated with a specific username.
     *
     * @param username the username to search for.
     * @return a list of {@link xChange} objects.
     */
    public List<xChange> getXChangerByUser(String username) {
        return xChangeDao.getXChangerByUser(username);
    }

    /**
     * Retrieves all {@link xChange} objects in the database.
     *
     * @return a {@link LiveData} object containing the list of {@link xChange} objects.
     */
    public LiveData<List<xChange>> getAllXChanges() {
        return xChangeDao.getAllXChanges();
    }

    /**
     * Marks an {@link xChange} as inactive in the database.
     *
     * @param id       the ID of the {@link xChange} to mark as inactive.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves all items in the database.
     *
     * @return a {@link LiveData} object containing the list of {@link Item} objects.
     */
    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    /**
     * Retrieves the total number of items in the database.
     *
     * @return a {@link LiveData} object containing the total item count.
     */
    public LiveData<Integer> getItemCount() {
        return itemDao.getItemCount();
    }

    /**
     * Retrieves the total number of sent requests.
     *
     * @return a {@link LiveData} object containing the count of sent requests.
     */
    public LiveData<Integer> getSentRequestsCount() {
        return requestDao.getRequestsSentCount();
    }

    /**
     * Searches for items by name.
     *
     * @param query    the search query.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Searches for items by name and category.
     *
     * @param query    the search query.
     * @param category the {@link Category} to filter by.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Saves a request to the database.
     *
     * @param request  the {@link Request} to save.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Shuts down the executor service gracefully.
     */
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

    /**
     * Retrieves the count of sent requests for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves all sent requests for administrative purposes.
     *
     * @param callback the callback to handle the list of sent requests or an error message.
     */
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

    /**
     * Retrieves the count of received requests for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the request count or an error message.
     */
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

    /**
     * Retrieves all requests received by a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of received requests or an error message.
     */
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

    /**
     * Retrieves all requests sent by a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of sent requests or an error message.
     */
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

    /**
     * Retrieves active received requests for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of active received requests or an error message.
     */
    public void getActiveReceivedRequests(String username, UserRepository.RequestItemsCallback callback) {
        executor.execute(() -> {
            try {
                LiveData<List<Request>> liveData = requestDao.getAllReceivedRequests(username);
                // Define the observer
                Observer<List<Request>> observer = new Observer<List<Request>>() {
                    @Override
                    public void onChanged(List<Request> requests) {
                        onRequestReceived(requests, callback);
                        liveData.removeObserver(this); // Remove observer after receiving data
                    }
                };
                // Observe LiveData on the main thread
                new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(observer));
            } catch (Exception e) {
                callback.onFailure("Failed to fetch received requests: " + e.getMessage());
            }
        });
    }

    /**
     * Retrieves the count of active requests for a specific item.
     *
     * @param itemId the ID of the item.
     * @return the count of active requests.
     */
    public int getActiveRequestCountForItem(long itemId) {
        return requestDao.countActiveRequestsForItem(itemId);
    }

    /**
     * Invokes the callback with the received requests.
     *
     * @param requests the list of received requests.
     * @param callback the callback to handle the received requests.
     */
    private void onRequestReceived(List<Request> requests, UserRepository.RequestItemsCallback callback) {
        callback.onSuccess(requests);
    }

    /**
     * Retrieves the total number of requests in the system.
     *
     * @param callback the callback to handle the request count or an error message.
     */
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

    /**
     * Retrieves the total number of exchanges in the system.
     *
     * @param callback the callback to handle the exchange count or an error message.
     */
    public void getTotalExchanges(UserStatisticsCallback callback) {
        executor.execute(() -> {
            try {
                int totalExchanges = xChangeDao.getAllXChangesSync().size();
                String stats = "Total Exchanges: " + totalExchanges;
                callback.onSuccess(stats);
            } catch (Exception e) {
                callback.onFailure("Failed to retrieve total exchanges");
            }
        });
    }

    /**
     * Retrieves the total number of exchanges for a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the exchange count or an error message.
     */
    public void getTotalExchangesCount(String username, UserRequestsCallback callback) {
        executor.execute(() -> {
            int count = 0;
            try {
                List<xChange> xchanges = xChangeDao.getAllXChangesSync();
                for (xChange xchange : xchanges) {
                    if ((xchange.getOfferee() != null && username.equals(xchange.getOfferee().getUsername())) ||
                            (xchange.getOfferer() != null && username.equals(xchange.getOfferer().getUsername()))) {
                        count++;
                    }
                }
                callback.onSuccess(count);
            } catch (Exception e) {
                // Log and return error message via callback
                Log.e("UserRepository", "Error fetching total exchanges count", e);
                callback.onFailure("Failed to fetch total exchanges count.");
            }
        });
    }

    /**
     * Retrieves all exchanges associated with a specific user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of exchanges or an error message.
     */
    public void getUserXChanges(String username, UserXChangesCallback callback) {
        executor.execute(() -> {
            try {
//                AppDatabase.getxChangeDao().deleteAll();
                List<xChange> xchanges = xChangeDao.getAllXChangesSync();
                List<xChange> tosend=new ArrayList<>();
                for(xChange xchange:xchanges){
                    if(xchange.getOfferee().getUsername().equals(username) ||
                            xchange.getOfferer().getUsername().equals(username)){
                        tosend.add(xchange);
                    }
                }

                callback.onSuccess(tosend);
            } catch (Exception e) {
                Log.e("UserRepository", "Error fetching xChanges for user", e);
                callback.onFailure("Failed to fetch xChanges.");
            }
        });
    }

    /**
     * Retrieves the total number of items in the system.
     *
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves the total number of categories in the system.
     *
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Cancels a user's item request by deleting it and associated counteroffers.
     *
     * @param itemId   the ID of the item for which the request is to be canceled.
     * @param username the username of the requester.
     */
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

    /**
     * Finds a request for a specific item and user.
     *
     * @param itemId   the ID of the item.
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Finds items associated with a specific xChanger.
     *
     * @param xChangerUsername the username of the xChanger.
     * @param callback         the callback to handle success or failure.
     */
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

    /**
     * Retrieves the count of counteroffers sent by a user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves the count of counteroffers received by a user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle success or failure.
     */
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

    /**
     * Retrieves counteroffers sent by a user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of sent counteroffers or an error message.
     */
    public void getSentCounterOffers(String username, UserCounterOffersCallback callback) {
        executor.execute(() -> {

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

    /**
     * Retrieves counteroffers received by a user.
     *
     * @param username the username of the user.
     * @param callback the callback to handle the list of received counteroffers or an error message.
     */
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

    /**
     * Checks if a user is the requestee with a counteroffer for a specific item.
     *
     * @param itemId   the ID of the item.
     * @param username the username of the user.
     * @return the {@link Counteroffer} if found, or null otherwise.
     */
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

    /**
     * Checks if a user is the requester with a counterofferee for a specific item.
     *
     * @param itemId   the ID of the item.
     * @param username the username of the user.
     * @return the {@link Counteroffer} if found, or null otherwise.
     */
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

    /**
     * Retrieves the offered item for a counteroffer by a user for a specific item.
     *
     * @param itemId   the ID of the item.
     * @param username the username of the user.
     * @return the offered {@link Item} if found, or null otherwise.
     */
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

    /**
     * Retrieves all exchanges in the system.
     *
     * @param callback the callback to handle the list of exchanges or an error message.
     */
    public void getAllXChanges(UserXChangesCallback callback) {
        executor.execute(() -> {
            try {
                List<xChange> xChanges = xChangeDao.getAllXChangesSync();
                callback.onSuccess(xChanges);
            } catch (Exception e) {
                Log.e("UserRepository", "Error fetching all xChanges", e);
                callback.onFailure("Failed to fetch xChanges.");
            }
        });
    }

}