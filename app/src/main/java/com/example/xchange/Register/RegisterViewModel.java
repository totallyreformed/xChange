package com.example.xchange.Register;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.User;

public class RegisterViewModel extends ViewModel implements RegisterPresenter.RegisterView {

    private final RegisterPresenter presenter;
    private final MutableLiveData<String> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> registerFailure = new MutableLiveData<>();

    public RegisterViewModel(Context context) {
        this.presenter = new RegisterPresenter(context);
    }

    public void registerUser(User user) {
        presenter.registerUser(user, this);
    }

    public LiveData<String> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<String> getRegisterFailure() {
        return registerFailure;
    }


    @Override
    public void onRegisterSuccess(String message) {
        registerSuccess.postValue(message);
    }


    @Override
    public void onRegisterFailure(String message) {
        registerFailure.postValue(message);
    }
}
