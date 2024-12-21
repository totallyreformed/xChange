package com.example.xchange.Register;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final UserRepository repository;
    private final MutableLiveData<String> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> registerFailure = new MutableLiveData<>();

    public RegisterViewModel(Context context) {
        repository = new UserRepository(context);
    }

    public LiveData<String> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<String> getRegisterFailure() {
        return registerFailure;
    }

    public void registerUser(User user) {
        repository.registerUser(user, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                registerSuccess.postValue("Registration Successful!");
            }

            @Override
            public void onFailure(String message) {
                registerFailure.postValue(message);
            }
        });
    }
}
