package com.example.xchange.Profile;

import android.app.Application;
import android.util.Log;

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
    private final MutableLiveData<Integer> counterOffersSentCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> counterOffersReceivedCount = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application, User user) {
        super(application);
        presenter = new ProfilePresenter(application.getApplicationContext(), user, this);
    }

    // LiveData for User
    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<String> getUserStatistics() {
        return userStatistics;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<List<Item>> getUserItems() {
        return userItems;
    }

    // LiveData for Requests and Counters
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

    public LiveData<Integer> getCounterOffersSentCount() {
        return counterOffersSentCount;
    }

    public LiveData<Integer> getCounterOffersReceivedCount() {
        return counterOffersReceivedCount;
    }

    // Loading Data
    public void loadProfileData() {
        presenter.loadProfileData();
    }

    public void loadUserItems() {
        presenter.loadUserItems();
    }

    public void loadRequests() {
        presenter.loadSentRequests();
        presenter.loadReceivedRequests();
    }

    public void loadRequestsCount() {
        presenter.loadRequestsCount();
    }

    public void loadCounterOffersCount() {
        presenter.loadCounterOffersCount();
    }

    // Callbacks from Presenter
    @Override
    public void onProfileDataLoaded(User user, String stats) {
        userLiveData.postValue(user);
        userStatistics.postValue(stats);
    }

    @Override
    public void onProfileDataFailed(String message) {
        error.postValue(message);
    }

    @Override
    public void onUserItemsLoaded(List<Item> items) {
        userItems.postValue(items);
    }

    @Override
    public void onUserItemsFailed(String message) {
        error.postValue(message);
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

    @Override
    public void onCounterOffersSentCountLoaded(int count) {
        counterOffersSentCount.postValue(count);
    }

    @Override
    public void onCounterOffersReceivedCountLoaded(int count) {
        counterOffersReceivedCount.postValue(count);
    }

    @Override
    public void onCounterOffersCountFailed(String message) {
        error.postValue(message);
    }
}
