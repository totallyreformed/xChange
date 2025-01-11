package com.example.xchange.Profile;

import android.content.Context;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;

import java.util.List;

/**
 * Presenter class for managing profile-related data and interactions in the xChange application.
 * <p>
 * Interacts with the {@link UserRepository} to fetch data such as user statistics, items,
 * requests, counter-offers, and exchanges. Passes the results to the associated {@link ProfileView}
 * for updating the UI.
 * </p>
 */
public class ProfilePresenter {

    /**
     * Interface representing the view that displays profile-related data.
     */
    public interface ProfileView {
        /**
         * Called when profile data is successfully loaded.
         *
         * @param user  The user whose profile data was loaded.
         * @param stats The user's profile statistics.
         */
        void onProfileDataLoaded(User user, String stats);
        /**
         * Called when loading profile data fails.
         *
         * @param message The error message.
         */
        void onProfileDataFailed(String message);
        /**
         * Called when user items are successfully loaded.
         *
         * @param items The list of items belonging to the user.
         */
        void onUserItemsLoaded(List<Item> items);
        /**
         * Called when loading user items fails.
         *
         * @param message The error message.
         */
        void onUserItemsFailed(String message);
        /**
         * Called when the count of sent requests is successfully loaded.
         *
         * @param count The count of sent requests.
         */
        void onSentRequestsCountLoaded(int count);
        /**
         * Called when the count of received requests is successfully loaded.
         *
         * @param count The count of received requests.
         */
        void onReceivedRequestsCountLoaded(int count);
        /**
         * Called when loading the count of requests fails.
         *
         * @param message The error message.
         */
        void onRequestsCountFailed(String message);
        /**
         * Called when the list of received requests is successfully loaded.
         *
         * @param requests The list of received requests.
         */
        void onReceivedRequestsLoaded(List<Request> requests);
        /**
         * Called when the list of sent requests is successfully loaded.
         *
         * @param requests The list of sent requests.
         */
        void onSentRequestsLoaded(List<Request> requests);
        /**
         * Called when the count of sent counter-offers is successfully loaded.
         *
         * @param count The count of sent counter-offers.
         */
        void onCounterOffersSentCountLoaded(int count);
        /**
         * Called when the count of received counter-offers is successfully loaded.
         *
         * @param count The count of received counter-offers.
         */
        void onCounterOffersReceivedCountLoaded(int count);
        /**
         * Called when loading the count of counter-offers fails.
         *
         * @param message The error message.
         */
        void onCounterOffersCountFailed(String message);
        /**
         * Called when the list of sent counter-offers is successfully loaded.
         *
         * @param counterOffers The list of sent counter-offers.
         */
        void onSentCounterOffersLoaded(List<Counteroffer> counterOffers);
        /**
         * Called when the list of received counter-offers is successfully loaded.
         *
         * @param counterOffers The list of received counter-offers.
         */
        void onReceivedCounterOffersLoaded(List<Counteroffer> counterOffers);
        /**
         * Called when loading counter-offers fails.
         *
         * @param message The error message.
         */
        void onCounterOffersFailed(String message);
        /**
         * Called when the total number of exchanges is successfully loaded.
         *
         * @param count The total number of exchanges.
         */
        void onTotalExchangesLoaded(int count);
        /**
         * Called when loading the total number of exchanges fails.
         *
         * @param message The error message.
         */
        void onTotalExchangesFailed(String message);
        /**
         * Called when the list of xChanges is successfully loaded.
         *
         * @param xChanges The list of xChanges.
         */
        void onXChangesLoaded(List<xChange> xChanges);
        /**
         * Called when loading the list of xChanges fails.
         *
         * @param message The error message.
         */
        void onXChangesFailed(String message);


    }

    private final UserRepository userRepository;
    private final ProfileView view;
    private final User user;

    /**
     * Constructor for initializing the ProfilePresenter.
     *
     * @param context The application context.
     * @param user    The user whose profile data is being managed.
     * @param view    The view interface for displaying profile data.
     */
    public ProfilePresenter(Context context, User user, ProfileView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
        this.user = user;
    }

    /**
     * Loads the profile data, such as user statistics, and notifies the view.
     */
    public void loadProfileData() {
        userRepository.getUserStatistics(user.getUsername(), new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                view.onProfileDataLoaded(user, stats);
            }

            @Override
            public void onFailure(String message) {
                view.onProfileDataFailed(message);
            }
        });
    }

    /**
     * Loads the user's items and notifies the view.
     */
    public void loadUserItems() {
        userRepository.getItemsByUsername(user.getUsername(), new UserRepository.UserItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                view.onUserItemsLoaded(items);
            }

            @Override
            public void onFailure(String message) {
                view.onUserItemsFailed(message);
            }
        });
    }

    /**
     * Loads the counts of sent and received requests and notifies the view.
     */
    public void loadRequestsCount() {
        userRepository.getSentRequestsCount(user.getUsername(), new UserRepository.UserRequestsCallback() {
            @Override
            public void onSuccess(int count) {
                view.onSentRequestsCountLoaded(count);
            }

            @Override
            public void onFailure(String message) {
                view.onRequestsCountFailed(message);
            }
        });

        userRepository.getReceivedRequestsCount(user.getUsername(), new UserRepository.UserRequestsCallback() {
            @Override
            public void onSuccess(int count) {
                view.onReceivedRequestsCountLoaded(count);
            }

            @Override
            public void onFailure(String message) {
                view.onRequestsCountFailed(message);
            }
        });
    }

    /**
     * Loads the received requests and notifies the view.
     */
    public void loadReceivedRequests() {
        userRepository.getRequestsReceived(user.getUsername(), new UserRepository.UserRequestsReceivedCallback() {
            @Override
            public void onSuccess(List<Request> requests) {
                view.onReceivedRequestsLoaded(requests);
            }

            @Override
            public void onFailure(String message) {
                view.onRequestsCountFailed(message);
            }
        });
    }

    /**
     * Loads the sent requests and notifies the view.
     */
    public void loadSentRequests() {
        userRepository.getSentRequests(user.getUsername(), new UserRepository.UserRequestsSentCallback() {
            @Override
            public void onSuccess(List<Request> requests) {
                view.onSentRequestsLoaded(requests);
            }

            @Override
            public void onFailure(String message) {
                view.onRequestsCountFailed(message);
            }
        });
    }

    /**
     * Loads the counts of sent and received counter-offers and notifies the view.
     */
    public void loadCounterOffersCount() {
        userRepository.getCounterOffersSentCount(user.getUsername(), new UserRepository.UserRequestsCallback() {
            @Override
            public void onSuccess(int count) {
                view.onCounterOffersSentCountLoaded(count);
            }

            @Override
            public void onFailure(String message) {
                view.onCounterOffersCountFailed(message);
            }
        });

        userRepository.getCounterOffersReceivedCount(user.getUsername(), new UserRepository.UserRequestsCallback() {
            @Override
            public void onSuccess(int count) {
                view.onCounterOffersReceivedCountLoaded(count);
            }

            @Override
            public void onFailure(String message) {
                view.onCounterOffersCountFailed(message);
            }
        });
    }

    /**
     * Loads the sent counter-offers and notifies the view.
     */
    public void loadSentCounterOffers() {
        userRepository.getSentCounterOffers(user.getUsername(), new UserRepository.UserCounterOffersCallback() {
            @Override
            public void onSuccess(List<Counteroffer> counterOffers) {
                view.onSentCounterOffersLoaded(counterOffers);
            }

            @Override
            public void onFailure(String message) {
                view.onCounterOffersFailed(message);
            }
        });
    }

    /**
     * Loads the received counter-offers and notifies the view.
     */
    public void loadReceivedCounterOffers() {
        userRepository.getReceivedCounterOffers(user.getUsername(), new UserRepository.UserCounterOffersCallback() {
            @Override
            public void onSuccess(List<Counteroffer> counterOffers) {
                view.onReceivedCounterOffersLoaded(counterOffers);
            }

            @Override
            public void onFailure(String message) {
                view.onCounterOffersFailed(message);
            }
        });
    }

    /**
     * Loads the user's xChanges and notifies the view.
     */
    public void loadUserXChanges() {
        userRepository.getUserXChanges(user.getUsername(), new UserRepository.UserXChangesCallback() {
            @Override
            public void onSuccess(List<xChange> xChanges) {
                view.onXChangesLoaded(xChanges);
            }

            @Override
            public void onFailure(String message) {
                view.onXChangesFailed(message);
            }
        });
    }

    /**
     * Loads the total count of exchanges and notifies the view.
     */
    public void loadTotalExchanges() {
        userRepository.getTotalExchangesCount(user.getUsername(), new UserRepository.UserRequestsCallback() {
            @Override
            public void onSuccess(int count) {
                view.onTotalExchangesLoaded(count);
            }

            @Override
            public void onFailure(String message) {
                view.onTotalExchangesFailed(message);
            }
        });
    }
}
