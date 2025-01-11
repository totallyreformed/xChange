package com.example.xchange.Search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.User;

/**
 * Factory class for creating instances of {@link SearchViewModel}.
 * <p>
 * This factory provides the necessary dependencies, such as {@link Application} and {@link User},
 * for the {@link SearchViewModel} during its creation.
 * </p>
 */
public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final User user;

    /**
     * Constructor for initializing the SearchViewModelFactory.
     *
     * @param application The application context.
     * @param user        The current user.
     */
    public SearchViewModelFactory(Application application, User user) {
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
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(application, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
