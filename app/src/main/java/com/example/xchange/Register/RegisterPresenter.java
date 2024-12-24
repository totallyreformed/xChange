package com.example.xchange.Register;

import android.content.Context;

import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

public class RegisterPresenter {

    private final UserRepository repository;

    public interface RegisterView {
        void onRegisterSuccess(String message); // Keep only this method for success

        void onRegisterFailure(String message);
    }

    public RegisterPresenter(Context context) {
        this.repository = new UserRepository(context);
    }

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
