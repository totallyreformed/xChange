// File: com/example/xchange/acceptrequest/AcceptRequestViewModel.java

package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;

public class AcceptRequestViewModel extends ViewModel {

    private final AcceptRequestPresenter presenter;

    public AcceptRequestViewModel(Context context) {
        presenter = new AcceptRequestPresenter(context, this);
    }

    /**
     * Initiates the acceptance of a request.
     *
     * @param request The Request object to be accepted.
     * @param rating  The rating provided by the user.
     * @return LiveData<Boolean> indicating success or failure.
     */
    public LiveData<Boolean> acceptRequest(Request request, float rating) {
        return presenter.acceptRequest(request, rating);
    }

    /**
     * Initiates the rejection of a request.
     *
     * @param request The Request object to be rejected.
     * @return LiveData<Boolean> indicating success or failure.
     */
    public LiveData<Boolean> rejectRequest(Request request) {
        return presenter.rejectRequest(request);
    }
}
