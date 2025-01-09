// File: com/example/xchange/AcceptRequest/AcceptRequestViewModel.java

package com.example.xchange.AcceptRequest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

public class AcceptRequestViewModel extends ViewModel {

    private final UserRepository repository;

    public AcceptRequestViewModel(@NonNull Context context) {
        repository = new UserRepository(context);
    }

    /**
     * Accepts a regular Request.
     *
     * @param request  The Request to be accepted.
     * @param rating   The rating value provided by the user.
     * @param callback Callback to handle success or failure.
     */
    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        repository.acceptRequest(request, null, rating, new UserRepository.AcceptRequestCallback() {
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

    /**
     * Accepts a Counteroffer.
     *
     * @param xchanger     The current user accepting the counteroffer.
     * @param counteroffer The Counteroffer to be accepted.
     * @param rating       The rating value provided by the user.
     * @param callback     Callback to handle success or failure.
     */
    public void acceptCounteroffer(xChanger xchanger, Counteroffer counteroffer, float rating, AcceptRequestCallback callback) {
        repository.acceptRequest(null, counteroffer, rating, new UserRepository.AcceptRequestCallback() {
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

    /**
     * Callback interface to handle the results of acceptance operations.
     */
    public interface AcceptRequestCallback {
        void onSuccess(long xChangeId);

        void onFailure(String message);
    }
}