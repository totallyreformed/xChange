package com.example.xchange.Register;

import android.content.Context;

import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

/**
 * Presenter class for handling user registration logic in the xChange application.
 * <p>
 * This class acts as an intermediary between the data layer (repository) and the view,
 * providing methods to register a user and handle success or failure callbacks.
 * </p>
 */
public class RegisterPresenter {

    private final UserRepository repository;

    /**
     * Interface representing the view for registration operations.
     * <p>
     * The view is notified of the success or failure of the registration process.
     * </p>
     */
    public interface RegisterView {
        /**
         * Called when the user registration is successful.
         *
         * @param message The success message.
         */
        void onRegisterSuccess(String message);
        /**
         * Called when the user registration fails.
         *
         * @param message The failure message.
         */
        void onRegisterFailure(String message);
    }

    /**
     * Constructor for initializing the RegisterPresenter.
     *
     * @param context The application context.
     */
    public RegisterPresenter(Context context) {
        this.repository = new UserRepository(context);
    }

    /**
     * Registers a new user and notifies the view of the result.
     *
     * @param user The user to be registered.
     * @param view The view to be notified of the result.
     */
    public void registerUser(User user, RegisterView view) {
        repository.registerUser(user, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                // Pass a success message to the view
                view.onRegisterSuccess("Registration successful!");
            }

            @Override
            public void onFailure(String message) {
                // Pass the failure message to the view
                view.onRegisterFailure(message);
            }
        });
    }
}
