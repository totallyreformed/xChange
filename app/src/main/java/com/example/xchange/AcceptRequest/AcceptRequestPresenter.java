package com.example.xchange.AcceptRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;

/**
 * Presenter class for handling the acceptance of requests and counteroffers in the xChange app.
 * Acts as a mediator between the view and the repository.
 */
public class AcceptRequestPresenter {

    private final UserRepository userRepository;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructs an AcceptRequestPresenter.
     *
     * @param context The application context used to initialize the UserRepository.
     */
    public AcceptRequestPresenter(Context context) {
        // Initialize the repository using the application context.
        this.userRepository = new UserRepository(context);
    }

    /**
     * Accepts a request and handles the callback on the main thread.
     *
     * @param request  The request to be accepted.
     * @param rating   The rating provided for the request.
     * @param callback The callback to be invoked after the operation is completed.
     */
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

    /**
     * Accepts a counteroffer and handles the callback on the main thread.
     *
     * @param counteroffer The counteroffer to be accepted.
     * @param rating       The rating provided for the counteroffer.
     * @param callback     The callback to be invoked after the operation is completed.
     */
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
