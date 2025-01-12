// File: com/example/xchange/acceptrequest/AcceptRequestViewModelFactory.java

package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory class for creating instances of {@link AcceptRequestViewModel}.
 */
public class AcceptRequestViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    /**
     * Constructs an AcceptRequestViewModelFactory.
     *
     * @param context The application context to be used for creating the ViewModel.
     */
    public AcceptRequestViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @param <T>        The type of the ViewModel.
     * @return A new instance of the specified ViewModel.
     * @throws IllegalArgumentException If the provided class is not assignable from {@link AcceptRequestViewModel}.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AcceptRequestViewModel.class)) {
            return (T) new AcceptRequestViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
