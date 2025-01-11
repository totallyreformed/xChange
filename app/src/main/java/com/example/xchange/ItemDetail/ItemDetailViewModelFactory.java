package com.example.xchange.ItemDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating instances of {@link ItemDetailViewModel}.
 * This factory ensures that the {@link ItemDetailViewModel} receives the required
 * {@link Application} context when it is instantiated.
 */
public class ItemDetailViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    /**
     * Constructs an {@code ItemDetailViewModelFactory}.
     *
     * @param application The application context to be passed to the {@link ItemDetailViewModel}.
     */
    public ItemDetailViewModelFactory(Application application) {
        this.application = application;
    }

    /**
     * Creates a new instance of the given {@code modelClass}.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return An instance of the requested ViewModel.
     * @throws IllegalArgumentException If the {@code modelClass} is not assignable to {@link ItemDetailViewModel}.
     */
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemDetailViewModel.class)) {
            return (T) new ItemDetailViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
