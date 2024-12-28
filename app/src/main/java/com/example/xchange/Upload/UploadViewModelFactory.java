package com.example.xchange.Upload;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.xChanger;

public class UploadViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final xChanger xChanger;

    public UploadViewModelFactory(Application application, xChanger xChanger) {
        this.application = application;
        this.xChanger = xChanger;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UploadViewModel.class)) {
            return (T) new UploadViewModel(application, xChanger);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
