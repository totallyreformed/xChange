package com.example.xchange;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.EditItem.EditItemViewModel;

public class EditItemViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final long itemId;

    public EditItemViewModelFactory(Application application, long itemId) {
        this.application = application;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditItemViewModel.class)) {
            return (T) new EditItemViewModel(application, itemId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
