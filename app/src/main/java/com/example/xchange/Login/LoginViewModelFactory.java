package com.example.xchange.Login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * The {@code LoginViewModelFactory} class is responsible for creating instances of {@link LoginViewModel}.
 * It implements the {@link ViewModelProvider.Factory} interface to provide a context-aware way of
 * instantiating ViewModels for the login feature.
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    /**
     * Constructs a new {@code LoginViewModelFactory} with the specified context.
     * The application context is used to avoid memory leaks.
     *
     * @param context The application context used to initialize the {@link LoginViewModel}.
     */
    public LoginViewModelFactory(Context context) {
        this.context = context.getApplicationContext(); // Use ApplicationContext to avoid leaks
    }

    /**
     * Creates a new instance of the given {@link ViewModel} class.
     *
     * @param modelClass The class of the {@link ViewModel} to create.
     * @param <T>        The type parameter of the {@link ViewModel}.
     * @return An instance of the specified {@link ViewModel}.
     * @throws IllegalArgumentException if the {@code modelClass} is not assignable from {@link LoginViewModel}.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
