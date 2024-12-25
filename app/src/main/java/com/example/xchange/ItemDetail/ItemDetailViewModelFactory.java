// File: ItemDetailViewModelFactory.java
package com.example.xchange.ItemDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ItemDetailViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ItemDetailViewModelFactory(Application application) {
        this.application = application;
    }

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
