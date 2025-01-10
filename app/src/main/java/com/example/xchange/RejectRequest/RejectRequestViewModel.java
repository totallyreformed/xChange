package com.example.xchange.RejectRequest;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class RejectRequestViewModel extends AndroidViewModel {
    private final UserRepository repository;

    public RejectRequestViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void rejectRequest(xChanger xchanger, Request request, RejectRequestCallback callback) {
        repository.rejectRequest(xchanger, request, new UserRepository.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

    public void rejectCounteroffer(Counteroffer counteroffer, RejectRequestCallback callback) {
        repository.rejectCounteroffer(counteroffer, new UserRepository.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

    public interface RejectRequestCallback {
        void onSuccess();

        void onFailure(String message);
    }
}
