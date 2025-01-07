// File: com/example/xchange/acceptrequest/AcceptRequestPresenter.java

package com.example.xchange.AcceptRequest;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Request;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;

public class AcceptRequestPresenter {

    private final UserRepository userRepository;
    private final AcceptRequestViewModel viewModel;

    public AcceptRequestPresenter(Context context, AcceptRequestViewModel viewModel) {
        this.userRepository = new UserRepository(context);
        this.viewModel = viewModel;
    }

    /**
     * Handles the acceptance of a request by invoking the UserRepository's acceptRequest method.
     *
     * @param request The Request object to be accepted.
     * @param rating  The rating provided by the user.
     * @return LiveData<Boolean> indicating success or failure.
     */
    public LiveData<Boolean> acceptRequest(Request request, float rating) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        userRepository.acceptRequest(request, rating, new UserRepository.AcceptRequestCallback() {
            @Override
            public void onSuccess() {
                result.postValue(true);
            }

            @Override
            public void onFailure(String message) {
                Log.e("AcceptRequestPresenter", "Error accepting request: " + message);
                result.postValue(false);
            }
        });
        return result;
    }
}
