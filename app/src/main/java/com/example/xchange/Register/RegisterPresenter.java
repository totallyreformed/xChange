package com.example.xchange.Register;

import com.example.xchange.User;
import com.example.xchange.repository.UserRepository;

public class RegisterPresenter {

    private final UserRepository repository;

    public interface RegisterView {
        void onRegisterSuccess();
        void onRegisterFailure(String message);
    }

    public RegisterPresenter(UserRepository repository) {
        this.repository = repository;
    }

    public void registerUser(User user, RegisterView view) {
        repository.registerUser(user, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                view.onRegisterSuccess();
            }

            @Override
            public void onFailure(String message) {
                view.onRegisterFailure(message);
            }
        });
    }
}
