package com.example.xchange.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executor;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
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
                // Check for duplicates before inserting
                User existingUser = userDao.findByUsername_initial(newUser.getUsername());
                if (existingUser != null) {
                    callback.onFailure("Username already exists");
                } else {
                    userDao.insertUser(newUser); // Insert the new user
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
}
