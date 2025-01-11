package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;

/**
 * ViewModel class for managing the acceptance of requests and counteroffers in the xChange app.
 * Acts as a bridge between the UI and the Presenter layer.
 */
public class AcceptRequestViewModel extends ViewModel {

    private final AcceptRequestPresenter presenter;

    /**
     * Constructs an AcceptRequestViewModel.
     *
     * @param context The application context used to initialize the AcceptRequestPresenter.
     */
    public AcceptRequestViewModel(Context context) {
        // Initialize the presenter with the context.
        presenter = new AcceptRequestPresenter(context);
    }

    /**
     * Accepts a request and forwards the operation to the presenter.
     *
     * @param request  The request to be accepted.
     * @param rating   The rating provided for the request.
     * @param callback The callback to be invoked after the operation is completed.
     */
    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        presenter.acceptRequest(request, rating, callback);
    }

    /**
     * Accepts a counteroffer and forwards the operation to the presenter.
     *
     * @param counteroffer The counteroffer to be accepted.
     * @param rating       The rating provided for the counteroffer.
     * @param callback     The callback to be invoked after the operation is completed.
     */
    public void acceptCounteroffer(Counteroffer counteroffer, float rating, AcceptRequestCallback callback) {
        presenter.acceptCounteroffer(counteroffer, rating, callback);
    }

    /**
     * Callback interface for notifying the result of a request or counteroffer acceptance operation.
     */
    public interface AcceptRequestCallback {
        /**
         * Called when the operation is successful.
         *
         * @param xChangeId The ID of the xChange created as a result of the operation.
         */
        void onSuccess(long xChangeId);
        /**
         * Called when the operation fails.
         *
         * @param message The error message describing the failure.
         */
        void onFailure(String message);
    }
}
