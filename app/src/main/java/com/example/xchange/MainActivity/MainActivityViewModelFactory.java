package com.example.xchange.MainActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final Context context;

    public MainActivityViewModelFactory(Application application, Context context) {
        this.application = application;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(application, context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}