package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class AcceptRequestViewModel extends ViewModel {

    private final UserRepository repository;

    public AcceptRequestViewModel(Context context) {
        repository = new UserRepository(context);
    }

    public void acceptRequest(Request request, @Nullable Counteroffer counteroffer, float rating, AcceptRequestCallback callback) {
        repository.acceptRequest(request, counteroffer, rating, new UserRepository.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                callback.onSuccess(xChangeId);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

    public interface AcceptRequestCallback {
        void onSuccess(long xChangeId);

        void onFailure(String message);
    }
}
