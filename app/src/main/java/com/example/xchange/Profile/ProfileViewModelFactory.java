// ProfileViewModelFactory.java
package com.example.xchange.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.User;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final User user;

    public ProfileViewModelFactory(Application application, User user) {
        this.application = application;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(application, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
