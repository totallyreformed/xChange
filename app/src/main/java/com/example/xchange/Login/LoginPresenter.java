package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.xchange.User;
import com.example.xchange.repository.UserRepository;

public class LoginPresenter {
    private final UserRepository userRepository;

    public interface LoginView {
        void onLoginSuccess(User user);
        void onLoginFailure(String message);
    }

    public LoginPresenter(Context context) {
        this.userRepository = new UserRepository(context);
    }


    public void loginAsXChanger(String username, String password, LoginView view) {
        userRepository.loginAsXChanger(username, password, new UserRepository.LoginCallback() {
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

    public void loginAsAdmin(String username, String password, LoginView view) {
        userRepository.loginAsAdmin(username, password, new UserRepository.LoginCallback() {
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

    public LiveData<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

}
