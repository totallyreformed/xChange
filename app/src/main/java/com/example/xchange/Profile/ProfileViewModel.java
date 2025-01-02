package com.example.xchange.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel implements ProfilePresenter.ProfileView {

    private final ProfilePresenter presenter;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userStatistics = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> userItems = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Integer> sentRequestsCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> receivedRequestsCount = new MutableLiveData<>();
    private final MutableLiveData<List<Request>> sentRequests = new MutableLiveData<>();
    private final MutableLiveData<List<Request>> receivedRequests = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application, User user) {
        super(application);
        presenter = new ProfilePresenter(application.getApplicationContext(), user, this);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<String> getUserStatistics() {
        return userStatistics;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadProfileData() {
        presenter.loadProfileData();
    }

    public LiveData<List<Item>> getUserItems() {
        return userItems;
    }

    public void loadUserItems() {
        presenter.loadUserItems();
    }

    @Override
    public void onProfileDataLoaded(User user, String stats) {
        userLiveData.postValue(user);
        userStatistics.postValue(stats);
    }

    @Override
    public void onProfileDataFailed(String message) {
        error.postValue(message);
    }
    public void loadRequests() {
        presenter.loadSentRequests();
        presenter.loadReceivedRequests();
    }


    @Override
    public void onUserItemsLoaded(List<Item> items) {
        userItems.postValue(items);
    }

    @Override
    public void onUserItemsFailed(String message) {
        error.postValue(message);
    }

    public LiveData<Integer> getSentRequestsCount() {
        return sentRequestsCount;
    }

    public LiveData<Integer> getReceivedRequestsCount() {
        return receivedRequestsCount;
    }

    public LiveData<List<Request>> getRequestsSent() {
        return sentRequests;
    }

    public LiveData<List<Request>> getRequestsReceived() {
        return receivedRequests;
    }

    public void loadRequestsCount() {
        presenter.loadRequestsCount();
    }

    @Override
    public void onSentRequestsCountLoaded(int count) {
        sentRequestsCount.postValue(count);
    }

    @Override
    public void onReceivedRequestsCountLoaded(int count) {
        receivedRequestsCount.postValue(count);
    }

    @Override
    public void onRequestsCountFailed(String message) {
        error.postValue(message);
    }

    @Override
    public void onReceivedRequestsLoaded(List<Request> requests) {
        receivedRequests.postValue(requests);
    }

    @Override
    public void onSentRequestsLoaded(List<Request> requests) {
        sentRequests.postValue(requests);
    }
}
