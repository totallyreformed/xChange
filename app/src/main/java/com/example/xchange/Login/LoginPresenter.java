package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.xchange.Notification;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.List;

/**
 * The {@code LoginPresenter} class serves as the presenter in the MVP architecture for the login feature.
 * It interacts with the {@link UserRepository} to perform user-related operations, such as login
 * and fetching user data, and communicates the results back to the view.
 */
public class LoginPresenter {
    private final UserRepository userRepository;

    /**
     * Interface to define the contract for the login view.
     */
    public interface LoginView {
        /**
         * Called when login is successful.
         *
         * @param user The {@link User} object representing the logged-in user.
         */
        void onLoginSuccess(User user);
        /**
         * Called when login fails.
         *
         * @param message A message describing the reason for login failure.
         */
        void onLoginFailure(String message);
    }

    /**
     * Constructs a {@code LoginPresenter} with the specified context.
     *
     * @param context The application context.
     */
    public LoginPresenter(Context context) {
        this.userRepository = new UserRepository(context);
    }

    /**
     * Logs in the user by validating the provided credentials.
     * The result is communicated back to the view using the {@link LoginView} interface.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @param view     The {@link LoginView} instance to receive login results.
     */
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

    /**
     * Retrieves a {@link LiveData} object containing the user details for the specified username.
     *
     * @param username The username of the user.
     * @return A {@link LiveData} object containing the user details.
     */
    public LiveData<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    /**
     * Fetches notifications for the specified user.
     *
     * @param username The username of the user whose notifications are to be fetched.
     * @param callback The callback to handle the success or failure of the operation.
     */
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

    /**
     * Deletes all notifications for the specified user.
     *
     * @param username The username of the user whose notifications are to be deleted.
     * @param callback The callback to handle the success or failure of the operation.
     */
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