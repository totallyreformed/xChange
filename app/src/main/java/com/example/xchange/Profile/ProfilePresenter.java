package com.example.xchange.Profile;

import android.content.Context;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;

import java.util.List;

public class ProfilePresenter {

    public interface ProfileView {
        void onProfileDataLoaded(User user, String stats);
        void onProfileDataFailed(String message);
        void onUserItemsLoaded(List<Item> items);
        void onUserItemsFailed(String message);
        void onSentRequestsCountLoaded(int count);
        void onReceivedRequestsCountLoaded(int count);
        void onRequestsCountFailed(String message);
        void onReceivedRequestsLoaded(List<Request> requests);
        void onSentRequestsLoaded(List<Request> requests);
        void onCounterOffersSentCountLoaded(int count);
        void onCounterOffersReceivedCountLoaded(int count);
        void onCounterOffersCountFailed(String message);

        void onSentCounterOffersLoaded(List<Counteroffer> counterOffers);

        void onReceivedCounterOffersLoaded(List<Counteroffer> counterOffers);

        void onCounterOffersFailed(String message);
        void onTotalExchangesLoaded(int count);
        void onTotalExchangesFailed(String message);
        void onXChangesLoaded(List<xChange> xChanges);
        void onXChangesFailed(String message);


    }

    private final UserRepository userRepository;
    private final ProfileView view;
    private final User user;

    public ProfilePresenter(Context context, User user, ProfileView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
        this.user = user;
    }

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
