package com.example.xchange.Upload;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.xChanger;

/**
 * Factory class for creating instances of {@link UploadViewModel}.
 * <p>
 * This factory is used to pass dependencies like {@link Application} and {@link xChanger} to the ViewModel.
 * </p>
 */
public class UploadViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final xChanger xChanger;

    /**
     * Constructs an {@code UploadViewModelFactory} with the specified application context and user.
     *
     * @param application The application context.
     * @param xChanger    The {@link xChanger} instance representing the current user.
     */
    public UploadViewModelFactory(Application application, xChanger xChanger) {
        this.application = application;
        this.xChanger = xChanger;
    }

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @param <T>        The type of the ViewModel.
     * @return A new instance of the specified ViewModel class.
     * @throws IllegalArgumentException If the specified class is not assignable from {@link UploadViewModel}.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UploadViewModel.class)) {
            return (T) new UploadViewModel(application, xChanger);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
