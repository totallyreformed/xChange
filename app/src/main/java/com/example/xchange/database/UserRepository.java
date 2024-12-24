package com.example.xchange.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.UserDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UserRepository {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final ExecutorService executor;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        itemDao = db.itemDao();
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

    public interface UserItemsCallback {
        void onSuccess(List<Item> items);
        void onFailure(String message);
    }

    // Login as xChanger
    public void loginAsXChanger(String username, String password, LoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.loginxChanger(username, password);
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onFailure("Invalid xChanger credentials");
            }
        });

    }

    public void loginAsAdmin(String username, String password, LoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.loginadmin(username, password);
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onFailure("Invalid admin credentials");
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

    /**
     * Search for items by name (case-insensitive and partial matches).
     *
     * @param query    The search query.
     * @param callback The callback to handle the results.
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
     * Filter items by category.
     *
     * @param category The category to filter by.
     * @param callback The callback to handle the results.
     */
    public void filterItemsByCategory(String category, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = itemDao.filterItemsByCategory(category);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onFailure("Error filtering items by category.");
            }
        });
    }

    /**
     * Search for items by name and filter by category simultaneously.
     *
     * @param query    The search query.
     * @param category The category to filter by.
     * @param callback The callback to handle the results.
     */
    public void searchItemsByNameAndCategory(String query, String category, UserItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Item> items = itemDao.searchItemsByNameAndCategory(query, category);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onFailure("Error searching items by name and category.");
            }
        });
    }
}
