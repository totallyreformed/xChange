package com.example.xchange.RejectRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class RejectRequestPresenter {

    private final UserRepository userRepository;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public RejectRequestPresenter(Context context) {
        // Initialize the repository using the provided context.
        userRepository = new UserRepository(context);
    }

    public void rejectRequest(xChanger xchanger, Request request, RejectRequestViewModel.RejectRequestCallback callback) {
        userRepository.rejectRequest(xchanger, request, new UserRepository.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                // Post result back to the main thread.
                mainThreadHandler.post(callback::onSuccess);
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> callback.onFailure(message));
            }
        });
    }

    public void rejectCounteroffer(Counteroffer counteroffer, RejectRequestViewModel.RejectRequestCallback callback) {
        userRepository.rejectCounteroffer(counteroffer, new UserRepository.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                mainThreadHandler.post(callback::onSuccess);
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> callback.onFailure(message));
            }
        });
    }
}
