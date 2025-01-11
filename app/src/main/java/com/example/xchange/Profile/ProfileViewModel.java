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

/**
 * ViewModel class for managing profile-related data and interactions in the xChange application.
 * <p>
 * Acts as an intermediary between the ProfilePresenter and the UI, providing LiveData objects
 * for profile data, user items, requests, counter-offers, and exchanges.
 * Implements the {@link ProfilePresenter.ProfileView} interface to handle callbacks from the presenter.
 * </p>
 */
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


    /**
     * Constructor for initializing the ProfileViewModel.
     *
     * @param application The application context.
     * @param user        The user whose profile data is being managed.
     */
    public ProfileViewModel(@NonNull Application application, User user) {
        super(application);
        presenter = new ProfilePresenter(application.getApplicationContext(), user, this);
    }

    /**
     * Retrieves the LiveData for the user.
     *
     * @return LiveData containing the user object.
     */
    public LiveData<User> getUser() {
        return userLiveData;
    }

    /**
     * Retrieves the LiveData for user statistics.
     *
     * @return LiveData containing the user statistics as a string.
     */
    public LiveData<String> getUserStatistics() {
        return userStatistics;
    }

    /**
     * Retrieves the LiveData for error messages.
     *
     * @return LiveData containing the error message.
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Retrieves the LiveData for user items.
     *
     * @return LiveData containing a list of user items.
     */
    public LiveData<List<Item>> getUserItems() {
        return userItems;
    }

    /**
     * Retrieves the LiveData for sent requests count.
     *
     * @return LiveData containing the count of sent requests.
     */
    public LiveData<Integer> getSentRequestsCount() {
        return sentRequestsCount;
    }

    /**
     * Retrieves the LiveData for received requests count.
     *
     * @return LiveData containing the count of received requests.
     */
    public LiveData<Integer> getReceivedRequestsCount() {
        return receivedRequestsCount;
    }

    /**
     * Retrieves the LiveData for sent requests.
     *
     * @return LiveData containing a list of sent requests.
     */
    public LiveData<List<Request>> getRequestsSent() {
        return sentRequests;
    }

    /**
     * Retrieves the LiveData for received requests.
     *
     * @return LiveData containing a list of received requests.
     */
    public LiveData<List<Request>> getRequestsReceived() {
        return receivedRequests;
    }

    /**
     * Retrieves the LiveData for sent counter-offers count.
     *
     * @return LiveData containing the count of sent counter-offers.
     */
    public LiveData<Integer> getCounterOffersSentCount() {
        return counterOffersSentCount;
    }

    /**
     * Retrieves the LiveData for received counter-offers count.
     *
     * @return LiveData containing the count of received counter-offers.
     */
    public LiveData<Integer> getCounterOffersReceivedCount() {
        return counterOffersReceivedCount;
    }

    /**
     * Retrieves the LiveData for user exchanges.
     *
     * @return LiveData containing a list of user exchanges.
     */
    public LiveData<List<xChange>> getUserXChanges() { return userXChanges; }

    /**
     * Retrieves the LiveData for total exchanges count.
     *
     * @return LiveData containing the total number of exchanges.
     */
    public LiveData<Integer> getTotalExchangesCount() { return totalExchangesCount; }

    /**
     * Loads profile data such as statistics.
     */
    public void loadProfileData() {
        presenter.loadProfileData();
    }

    /**
     * Loads the user's items.
     */
    public void loadUserItems() {
        presenter.loadUserItems();
    }

    /**
     * Loads sent and received requests.
     */
    public void loadRequests() {
        presenter.loadSentRequests();
        presenter.loadReceivedRequests();
    }

    /**
     * Loads the user's exchanges.
     */
    public void loadUserXChanges() {
        presenter.loadUserXChanges();
    }

    /**
     * Loads the total number of exchanges.
     */
    public void loadTotalExchanges() { presenter.loadTotalExchanges();}

    /**
     * Loads counts for sent and received requests.
     */
    public void loadRequestsCount() {
        presenter.loadRequestsCount();
    }

    /**
     * Loads counts for sent and received counter-offers.
     */
    public void loadCounterOffersCount() {
        presenter.loadCounterOffersCount();
    }

    /**
     * Callback when profile data is successfully loaded.
     *
     * @param user  The user whose profile data was loaded.
     * @param stats The user's statistics.
     */
    @Override
    public void onProfileDataLoaded(User user, String stats) {
        userLiveData.postValue(user);
        userStatistics.postValue(stats);
    }

    /**
     * Callback when profile data fails to load.
     *
     * @param message The error message.
     */
    @Override
    public void onProfileDataFailed(String message) {
        error.postValue(message);
    }

    /**
     * Callback when xChanges data is successfully loaded.
     *
     * @param xChanges The list of xChanges.
     */
    @Override
    public void onXChangesLoaded(List<xChange> xChanges) { userXChanges.postValue(xChanges); }

    /**
     * Callback when xChanges data fails to load.
     *
     * @param message The error message.
     */
    @Override
    public void onXChangesFailed(String message) {
        error.postValue(message);
    }

    /**
     * Callback when the total number of exchanges is successfully loaded.
     *
     * @param count The total number of exchanges.
     */
    @Override
    public void onTotalExchangesLoaded(int count) { totalExchangesCount.postValue(count); }

    /**
     * Callback when the total number of exchanges fails to load.
     *
     * @param message The error message.
     */
    @Override
    public void onTotalExchangesFailed(String message) { error.postValue(message); }

    /**
     * Callback when user items are successfully loaded.
     *
     * @param items The list of user items.
     */
    @Override
    public void onUserItemsLoaded(List<Item> items) {
        userItems.postValue(items);
    }

    /**
     * Callback when loading user items fails.
     *
     * @param message The error message.
     */
    @Override
    public void onUserItemsFailed(String message) {
        error.postValue(message);
    }

    /**
     * Callback when the count of sent requests is successfully loaded.
     *
     * @param count The count of sent requests.
     */
    @Override
    public void onSentRequestsCountLoaded(int count) {
        sentRequestsCount.postValue(count);
    }

    /**
     * Callback when the count of received requests is successfully loaded.
     *
     * @param count The count of received requests.
     */
    @Override
    public void onReceivedRequestsCountLoaded(int count) {
        receivedRequestsCount.postValue(count);
    }

    /**
     * Callback when loading the count of requests fails.
     *
     * @param message The error message.
     */
    @Override
    public void onRequestsCountFailed(String message) {
        error.postValue(message);
    }

    /**
     * Callback when received requests are successfully loaded.
     *
     * @param requests The list of received requests.
     */
    @Override
    public void onReceivedRequestsLoaded(List<Request> requests) {
        receivedRequests.postValue(requests);
    }

    /**
     * Callback when sent requests are successfully loaded.
     *
     * @param requests The list of sent requests.
     */
    @Override
    public void onSentRequestsLoaded(List<Request> requests) {
        sentRequests.postValue(requests);
    }

    /**
     * Callback when the count of sent counter-offers is successfully loaded.
     *
     * @param count The count of sent counter-offers.
     */
    @Override
    public void onCounterOffersSentCountLoaded(int count) {
        counterOffersSentCount.postValue(count);
    }

    /**
     * Callback when the count of received counter-offers is successfully loaded.
     *
     * @param count The count of received counter-offers.
     */
    @Override
    public void onCounterOffersReceivedCountLoaded(int count) {
        counterOffersReceivedCount.postValue(count);
    }

    /**
     * Callback when loading the count of counter-offers fails.
     *
     * @param message The error message.
     */
    @Override
    public void onCounterOffersCountFailed(String message) {
        error.postValue(message);
    }

    /**
     * Retrieves the LiveData for sent counter-offers.
     *
     * @return LiveData containing the list of sent counter-offers.
     */
    public LiveData<List<Counteroffer>> getCounterOffersSent() {
        return sentCounterOffers;
    }

    /**
     * Retrieves the LiveData for received counter-offers.
     *
     * @return LiveData containing the list of received counter-offers.
     */
    public LiveData<List<Counteroffer>> getCounterOffersReceived() {
        return receivedCounterOffers;
    }


    /**
     * Loads sent and received counter-offers.
     */
    public void loadCounterOffers() {
        presenter.loadSentCounterOffers();
        presenter.loadReceivedCounterOffers();
    }

    /**
     * Callback when sent counter-offers are successfully loaded.
     *
     * @param counterOffers The list of sent counter-offers.
     */
    @Override
    public void onSentCounterOffersLoaded(List<Counteroffer> counterOffers) {
        sentCounterOffers.postValue(counterOffers);
    }

    /**
     * Callback when received counter-offers are successfully loaded.
     *
     * @param counterOffers The list of received counter-offers.
     */
    @Override
    public void onReceivedCounterOffersLoaded(List<Counteroffer> counterOffers) {
        receivedCounterOffers.postValue(counterOffers);
    }

    /**
     * Callback when loading counter-offers fails.
     *
     * @param message The error message.
     */
    @Override
    public void onCounterOffersFailed(String message) {
        error.postValue(message);
    }
}
