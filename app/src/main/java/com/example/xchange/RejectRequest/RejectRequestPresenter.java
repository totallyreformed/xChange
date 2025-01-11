package com.example.xchange.RejectRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

/**
 * Presenter class for handling the rejection of requests and counteroffers in the xChange application.
 * <p>
 * This class acts as an intermediary between the data layer (repository) and the view,
 * providing methods to reject requests and counteroffers and handle callbacks on the main thread.
 * </p>
 */
public class RejectRequestPresenter {

    private final UserRepository userRepository;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor for initializing the RejectRequestPresenter.
     *
     * @param context The application context.
     */
    public RejectRequestPresenter(Context context) {
        // Initialize the repository using the provided context.
        userRepository = new UserRepository(context);
    }

    /**
     * Rejects a request and notifies the callback of the result.
     *
     * @param xchanger  The xChanger rejecting the request.
     * @param request   The request to be rejected.
     * @param callback  The callback to notify of success or failure.
     */
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

    /**
     * Rejects a counteroffer and notifies the callback of the result.
     *
     * @param counteroffer The counteroffer to be rejected.
     * @param callback     The callback to notify of success or failure.
     */
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
