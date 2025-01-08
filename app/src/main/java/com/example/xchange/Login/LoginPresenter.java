package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.xchange.Notification;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.List;

public class LoginPresenter {
    private final UserRepository userRepository;

    public interface LoginView {
        void onLoginSuccess(User user);
        void onLoginFailure(String message);
    }

    public LoginPresenter(Context context) {
        this.userRepository = new UserRepository(context);
    }


    public void loginUser(String username, String password, LoginView view) {
        userRepository.loginUser(username, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                view.onLoginSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                view.onLoginFailure(message);
            }
        });
    }

//    public void loginAsXChanger(String username, String password, LoginView view) {
//        userRepository.loginAsXChanger(username, password, new UserRepository.LoginCallback() {
//            @Override
//            public void onSuccess(User user) {
//                view.onLoginSuccess(user);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                view.onLoginFailure(message);
//            }
//        });
//    }
//
//    public void loginAsAdmin(String username, String password, LoginView view) {
//        userRepository.loginAsAdmin(username, password, new UserRepository.LoginCallback() {
//            @Override
//            public void onSuccess(User user) {
//                view.onLoginSuccess(user);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                view.onLoginFailure(message);
//            }
//        });
//    }

    public LiveData<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    // Fetch notifications for a user
    public void getNotificationsForUser(String username, UserRepository.NotificationCallback callback) {
        userRepository.getNotificationsForUser(username, new UserRepository.NotificationCallback() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                callback.onSuccess(notifications);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

    // Delete notifications for a user
    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        userRepository.deleteNotificationsForUser(username, new UserRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

}