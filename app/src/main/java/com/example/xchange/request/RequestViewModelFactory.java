// RequestViewModelFactory.java
package com.example.xchange.request;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RequestViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public RequestViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RequestViewModel.class)) {
            return (T) new RequestViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
