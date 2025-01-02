package com.example.xchange.Profile;

import android.content.Context;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

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

}
