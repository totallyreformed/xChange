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

    public LiveData<Boolean> rejectRequest(Request request) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        // Implement rejection logic, similar to accept
        try {
            request.make_unactive();
            AppDatabase.getRequestDao().updateRequest(request);
            result.postValue(true);
        } catch (Exception e) {
            Log.e("AcceptRequestPresenter", "Error rejecting request: " + e.getMessage());
            result.postValue(false);
        }
        return result;
    }
}
