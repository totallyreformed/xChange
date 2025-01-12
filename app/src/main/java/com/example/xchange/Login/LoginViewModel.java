package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;

/**
 * The {@code LoginViewModel} class serves as the ViewModel in the MVVM architecture for the login feature.
 * It bridges the gap between the {@link LoginPresenter} (data layer) and the {@code LoginActivity} (UI layer),
 * providing observable LiveData for login results and user-related operations.
 */
public class LoginViewModel extends ViewModel implements LoginPresenter.LoginView {

    private final LoginPresenter presenter;
    private final MutableLiveData<User> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginFailure = new MutableLiveData<>();

    /**
     * Constructs a {@code LoginViewModel} with the specified context.
     *
     * @param context The application context.
     */
    public LoginViewModel(Context context) {
        this.presenter = new LoginPresenter(context);
    }

    /**
     * Attempts to log in a user with the provided credentials.
     * Results are posted to the LiveData objects {@code loginSuccess} and {@code loginFailure}.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     */
    public void loginUser(String username, String password) {
        presenter.loginUser(username, password, this);
    }

    /**
     * Returns a {@link LiveData} object that observes successful login attempts.
     *
     * @return A {@link LiveData} object containing the {@link User} on successful login.
     */
    public LiveData<User> getLoginSuccess() {
        return loginSuccess;
    }

    /**
     * Returns a {@link LiveData} object that observes failed login attempts.
     *
     * @return A {@link LiveData} object containing an error message on login failure.
     */
    public LiveData<String> getLoginFailure() {
        return loginFailure;
    }

    /**
     * Callback method invoked upon successful login.
     * Posts the {@link User} object to the {@code loginSuccess} LiveData.
     *
     * @param user The logged-in {@link User}.
     */
    @Override
    public void onLoginSuccess(User user) {
        loginSuccess.postValue(user);
    }

    /**
     * Callback method invoked upon login failure.
     * Posts the failure message to the {@code loginFailure} LiveData.
     *
     * @param message A message describing the reason for login failure.
     */
    @Override
    public void onLoginFailure(String message) {
        loginFailure.postValue(message);
    }

    /**
     * Retrieves a {@link LiveData} object containing the user details for the specified username.
     *
     * @param username The username of the user.
     * @return A {@link LiveData} object containing the user details.
     */
    public LiveData<User> getUserByUsername(String username) {
        return presenter.getUserByUsername(username);
    }

    /**
     * Fetches notifications for the specified user.
     *
     * @param username The username of the user whose notifications are to be fetched.
     * @param callback The callback to handle the success or failure of the operation.
     */
    public void getNotificationsForUser(String username, UserRepository.NotificationCallback callback) {
        presenter.getNotificationsForUser(username, callback);
    }

    /**
     * Deletes all notifications for the specified user.
     *
     * @param username The username of the user whose notifications are to be deleted.
     * @param callback The callback to handle the success or failure of the operation.
     */
    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        presenter.deleteNotificationsForUser(username, callback);
    }
}