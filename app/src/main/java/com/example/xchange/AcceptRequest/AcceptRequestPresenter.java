package com.example.xchange.AcceptRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;

public class AcceptRequestPresenter {

    private final UserRepository userRepository;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public AcceptRequestPresenter(Context context) {
        // Initialize the repository using the application context.
        this.userRepository = new UserRepository(context);
    }

    public void acceptRequest(Request request, float rating, AcceptRequestViewModel.AcceptRequestCallback callback) {
        userRepository.acceptRequest(request, rating, new UserRepository.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                mainThreadHandler.post(() -> callback.onSuccess(xChangeId));
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> callback.onFailure(message));
            }
        });
    }

    public void acceptCounteroffer(Counteroffer counteroffer, float rating, AcceptRequestViewModel.AcceptRequestCallback callback) {
        userRepository.acceptCounteroffer(counteroffer, rating, new UserRepository.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                mainThreadHandler.post(() -> callback.onSuccess(xChangeId));
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> callback.onFailure(message));
            }
        });
    }
}
