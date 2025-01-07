package com.example.xchange.RejectRequest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RejectRequestViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public RejectRequestViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RejectRequestViewModel.class)) {
            return (T) new RejectRequestViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
