// File: SearchViewModelFactory.java
package com.example.xchange.Search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.User;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final User user;

    public SearchViewModelFactory(Application application, User user) {
        this.application = application;
        this.user = user;
    }

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
