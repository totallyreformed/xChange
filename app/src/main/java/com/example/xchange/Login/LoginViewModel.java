package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.User;

public class LoginViewModel extends ViewModel implements LoginPresenter.LoginView {

    private final LoginPresenter presenter;
    private final MutableLiveData<User> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginFailure = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        this.presenter = new LoginPresenter(context);
    }

    public void loginAsXChanger(String username, String password) {
        presenter.loginAsXChanger(username, password, this);
    }

    public void loginAsAdmin(String username, String password) {
        presenter.loginAsAdmin(username, password, this);
    }

    public LiveData<User> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getLoginFailure() {
        return loginFailure;
    }

    @Override
    public void onLoginSuccess(User user) {
        loginSuccess.postValue(user);
    }

    @Override
    public void onLoginFailure(String message) {
        loginFailure.postValue(message);
    }
    public LiveData<User> getUserByUsername(String username) {
        return presenter.getUserByUsername(username);
    }
}