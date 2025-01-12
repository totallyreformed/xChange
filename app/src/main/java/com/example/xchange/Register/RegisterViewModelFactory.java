package com.example.xchange.Register;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating instances of {@link RegisterViewModel}.
 * <p>
 * This factory provides the necessary dependencies (e.g., context) for the {@link RegisterViewModel}
 * during its creation.
 * </p>
 */
public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    /**
     * Constructor for initializing the RegisterViewModelFactory.
     *
     * @param context The application context.
     */
    public RegisterViewModelFactory(Context context) {
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
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
