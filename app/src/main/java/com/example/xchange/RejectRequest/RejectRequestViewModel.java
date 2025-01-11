package com.example.xchange.RejectRequest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.xChanger;

public class RejectRequestViewModel extends AndroidViewModel {

    private final RejectRequestPresenter presenter;

    public RejectRequestViewModel(@NonNull Application application) {
        super(application);
        // Initialize the presenter with the Application context.
        presenter = new RejectRequestPresenter(application);
    }

    public void rejectRequest(xChanger xchanger, Request request, RejectRequestCallback callback) {
        presenter.rejectRequest(xchanger, request, callback);
    }

    public void rejectCounteroffer(Counteroffer counteroffer, RejectRequestCallback callback) {
        presenter.rejectCounteroffer(counteroffer, callback);
    }

    public interface RejectRequestCallback {
        void onSuccess();
        void onFailure(String message);
    }
}
