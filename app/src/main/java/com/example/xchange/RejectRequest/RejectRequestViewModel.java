package com.example.xchange.RejectRequest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.xChanger;

/**
 * ViewModel class for managing the rejection of requests and counteroffers in the xChange application.
 * <p>
 * This class provides methods to handle the rejection of requests and counteroffers through the {@link RejectRequestPresenter}.
 * It serves as an intermediary between the UI and the presenter, ensuring data and operations are managed properly.
 * </p>
 */
public class RejectRequestViewModel extends AndroidViewModel {

    private final RejectRequestPresenter presenter;

    /**
     * Constructor for initializing the RejectRequestViewModel.
     *
     * @param application The application context.
     */
    public RejectRequestViewModel(@NonNull Application application) {
        super(application);
        // Initialize the presenter with the Application context.
        presenter = new RejectRequestPresenter(application);
    }

    /**
     * Rejects a request and notifies the callback of the result.
     *
     * @param xchanger  The xChanger rejecting the request.
     * @param request   The request to be rejected.
     * @param callback  The callback to notify of success or failure.
     */
    public void rejectRequest(xChanger xchanger, Request request, RejectRequestCallback callback) {
        presenter.rejectRequest(xchanger, request, callback);
    }

    /**
     * Rejects a counteroffer and notifies the callback of the result.
     *
     * @param counteroffer The counteroffer to be rejected.
     * @param callback     The callback to notify of success or failure.
     */
    public void rejectCounteroffer(Counteroffer counteroffer, RejectRequestCallback callback) {
        presenter.rejectCounteroffer(counteroffer, callback);
    }

    /**
     * Interface for handling callbacks from request and counteroffer rejection operations.
     */
    public interface RejectRequestCallback {
        /**
         * Called when the rejection operation is successful.
         */
        void onSuccess();
        /**
         * Called when the rejection operation fails.
         *
         * @param message The error message.
         */
        void onFailure(String message);
    }
}
