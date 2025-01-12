package com.example.xchange.MainActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;

/**
 * Factory class for creating instances of MainActivityViewModel.
 * <p>
 * Provides the necessary dependencies (Application and Context) to the MainActivityViewModel
 * during its creation.
 * </p>
 */
public class MainActivityViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final Context context;

    /**
     * Constructor for initializing the ViewModelFactory.
     *
     * @param application The application context.
     * @param context     The activity context.
     */
    public MainActivityViewModelFactory(Application application, Context context) {
        this.application = application;
        this.context = context;
    }

    /**
     * Creates an instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @param <T>        The type of the ViewModel.
     * @return A new instance of the requested ViewModel.
     * @throws IllegalArgumentException If the requested ViewModel class is not recognized.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(application, context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}