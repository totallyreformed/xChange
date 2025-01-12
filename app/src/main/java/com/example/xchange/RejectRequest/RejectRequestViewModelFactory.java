package com.example.xchange.RejectRequest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating instances of {@link RejectRequestViewModel}.
 * <p>
 * This factory provides the necessary dependencies (e.g., {@link Application}) for the {@link RejectRequestViewModel}
 * during its creation.
 * </p>
 */
public class RejectRequestViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    /**
     * Constructor for initializing the RejectRequestViewModelFactory.
     *
     * @param application The application context.
     */
    public RejectRequestViewModelFactory(Application application) {
        this.application = application;
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
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RejectRequestViewModel.class)) {
            return (T) new RejectRequestViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
