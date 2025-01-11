package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;

public class AcceptRequestViewModel extends ViewModel {

    private final AcceptRequestPresenter presenter;

    public AcceptRequestViewModel(Context context) {
        // Initialize the presenter with the context.
        presenter = new AcceptRequestPresenter(context);
    }

    public void acceptRequest(Request request, float rating, AcceptRequestCallback callback) {
        presenter.acceptRequest(request, rating, callback);
    }

    public void acceptCounteroffer(Counteroffer counteroffer, float rating, AcceptRequestCallback callback) {
        presenter.acceptCounteroffer(counteroffer, rating, callback);
    }

    public interface AcceptRequestCallback {
        void onSuccess(long xChangeId);
        void onFailure(String message);
    }
}
