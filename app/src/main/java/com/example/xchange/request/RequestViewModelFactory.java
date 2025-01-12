package com.example.xchange.request;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating instances of {@link RequestViewModel}.
 * <p>
 * This factory provides the necessary dependencies (e.g., {@link Context}) for the {@link RequestViewModel}
 * during its creation.
 * </p>
 */
public class RequestViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    /**
     * Constructor for initializing the RequestViewModelFactory.
     *
     * @param context The application context.
     */
    public RequestViewModelFactory(Context context) {
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
        if (modelClass.isAssignableFrom(RequestViewModel.class)) {
            return (T) new RequestViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
