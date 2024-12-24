package com.example.xchange.Upload;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UploadViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public UploadViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UploadViewModel(application);
    }
}
