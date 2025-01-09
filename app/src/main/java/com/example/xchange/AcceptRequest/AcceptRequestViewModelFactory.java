// File: com/example/xchange/acceptrequest/AcceptRequestViewModelFactory.java

package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AcceptRequestViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public AcceptRequestViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AcceptRequestViewModel.class)) {
            return (T) new AcceptRequestViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
