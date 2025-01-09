package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class AcceptRequestViewModel extends ViewModel {

    private final UserRepository repository;

    public AcceptRequestViewModel(Context context) {
        repository = new UserRepository(context);
    }

    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        repository.acceptRequest(request, rating, new UserRepository.AcceptRequestCallback() {
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

    public interface AcceptRequestCallback {
        void onSuccess();

        void onFailure(String message);
    }
}
