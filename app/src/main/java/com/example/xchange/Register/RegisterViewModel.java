package com.example.xchange.Register;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.User;

/**
 * ViewModel class for managing user registration in the xChange application.
 * <p>
 * This class provides LiveData objects to observe the success or failure of user registration
 * and acts as an intermediary between the {@link RegisterPresenter} and the UI.
 * </p>
 */
public class RegisterViewModel extends ViewModel implements RegisterPresenter.RegisterView {

    private final RegisterPresenter presenter;
    private final MutableLiveData<String> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> registerFailure = new MutableLiveData<>();

    /**
     * Constructor for initializing the RegisterViewModel.
     *
     * @param context The application context.
     */
    public RegisterViewModel(Context context) {
        this.presenter = new RegisterPresenter(context);
    }

    /**
     * Initiates the user registration process.
     *
     * @param user The user to be registered.
     */
    public void registerUser(User user) {
        presenter.registerUser(user, this);
    }

    /**
     * Retrieves the LiveData for successful registration messages.
     *
     * @return LiveData containing the success message.
     */
    public LiveData<String> getRegisterSuccess() {
        return registerSuccess;
    }

    /**
     * Retrieves the LiveData for failure messages during registration.
     *
     * @return LiveData containing the failure message.
     */
    public LiveData<String> getRegisterFailure() {
        return registerFailure;
    }

    /**
     * Callback invoked when the registration process is successful.
     *
     * @param message The success message.
     */
    @Override
    public void onRegisterSuccess(String message) {
        registerSuccess.postValue(message);
    }

    /**
     * Callback invoked when the registration process fails.
     *
     * @param message The failure message.
     */
    @Override
    public void onRegisterFailure(String message) {
        registerFailure.postValue(message);
    }
}
