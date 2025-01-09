package com.example.xchange.RejectRequest;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Notification;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class RejectRequestViewModel extends AndroidViewModel {
    private final UserRepository repository;

    public RejectRequestViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    /**
     * Rejects a regular Request.
     *
     * @param request  The Request to be rejected.
     * @param callback Callback to handle success or failure.
     */
    public void rejectRequest(Request request, float rating, RejectRequestCallback callback) {
        repository.rejectRequest(request, null, rating, new UserRepository.RejectRequestCallback() {
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

    /**
     * Rejects a Counteroffer.
     *
     * @param xchanger   The current user rejecting the counteroffer.
     * @param counteroffer The Counteroffer to be rejected.
     * @param callback   Callback to handle success or failure.
     */
    public void rejectCounteroffer(xChanger xchanger, Counteroffer counteroffer, float rating, RejectRequestCallback callback) {
        repository.rejectRequest(null, counteroffer, rating, new UserRepository.RejectRequestCallback() {
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
