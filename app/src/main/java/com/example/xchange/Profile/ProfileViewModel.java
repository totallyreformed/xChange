package com.example.xchange.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.xChange;

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
    private final MutableLiveData<List<Counteroffer>> sentCounterOffers = new MutableLiveData<>();
    private final MutableLiveData<List<Counteroffer>> receivedCounterOffers = new MutableLiveData<>();
    private final MutableLiveData<List<xChange>> userXChanges = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalExchangesCount = new MutableLiveData<>();


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
    public LiveData<List<xChange>> getUserXChanges() { return userXChanges; }
    public LiveData<Integer> getTotalExchangesCount() { return totalExchangesCount; }

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

    public void loadUserXChanges() {
        presenter.loadUserXChanges();
    }

    public void loadTotalExchanges() { presenter.loadTotalExchanges();}

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
    public void onXChangesLoaded(List<xChange> xChanges) { userXChanges.postValue(xChanges); }
    @Override
    public void onXChangesFailed(String message) {
        error.postValue(message);
    }

    @Override
    public void onTotalExchangesLoaded(int count) { totalExchangesCount.postValue(count); }

    @Override
    public void onTotalExchangesFailed(String message) { error.postValue(message); }

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
    public LiveData<List<Counteroffer>> getCounterOffersSent() {
        return sentCounterOffers;
    }

    public LiveData<List<Counteroffer>> getCounterOffersReceived() {
        return receivedCounterOffers;
    }

    public void loadCounterOffers() {
        presenter.loadSentCounterOffers();
        presenter.loadReceivedCounterOffers();
    }

    @Override
    public void onSentCounterOffersLoaded(List<Counteroffer> counterOffers) {
        sentCounterOffers.postValue(counterOffers);
    }

    @Override
    public void onReceivedCounterOffersLoaded(List<Counteroffer> counterOffers) {
        receivedCounterOffers.postValue(counterOffers);
    }

    @Override
    public void onCounterOffersFailed(String message) {
        error.postValue(message);
    }
}
