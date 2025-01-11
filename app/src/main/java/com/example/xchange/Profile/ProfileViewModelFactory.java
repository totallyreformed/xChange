package com.example.xchange.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.User;

/**
 * Factory class for creating instances of {@link ProfileViewModel}.
 * <p>
 * Provides the necessary dependencies (Application and User) to the {@link ProfileViewModel}
 * during its creation.
 * </p>
 */
public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final User user;

    /**
     * Constructor for initializing the ProfileViewModelFactory.
     *
     * @param application The application context.
     * @param user        The user whose profile data is managed by the ViewModel.
     */
    public ProfileViewModelFactory(Application application, User user) {
        this.application = application;
        this.user = user;
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
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(application, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
