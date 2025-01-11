package com.example.xchange.EditItem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.EditItem.EditItemViewModel;

/**
 * Factory class for creating instances of {@link EditItemViewModel}.
 * Ensures the necessary dependencies are provided when initializing the ViewModel.
 */
public class EditItemViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final long itemId;

    /**
     * Constructs an EditItemViewModelFactory.
     *
     * @param application The application context to be passed to the ViewModel.
     * @param itemId      The ID of the item to be edited, to be passed to the ViewModel.
     */
    public EditItemViewModelFactory(Application application, long itemId) {
        this.application = application;
        this.itemId = itemId;
    }

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @param <T>        The type of the ViewModel.
     * @return A new instance of the specified ViewModel class.
     * @throws IllegalArgumentException If the provided class is not assignable from {@link EditItemViewModel}.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditItemViewModel.class)) {
            return (T) new EditItemViewModel(application, itemId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
