package com.example.xchange.MainActivity;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.List;

public class MainActivityPresenter {
    private final MainActivityViewModel viewModel;
    private final UserRepository userRepository;

    public MainActivityPresenter(MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.userRepository = new UserRepository(context);
    }

    public LiveData<List<Item>> loadItems() {
        return userRepository.getAllItems(); // Adjust based on your UserRepository implementation
    }

    public void loadUser(User user) {
        if (user != null) {
            viewModel.updateUsername(user.getUsername());
        } else {
            viewModel.updateUsername("Guest");
        }
    }

    public void fetchTotalRequests(MutableLiveData<Integer> liveData) {
        userRepository.getTotalRequests(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    liveData.postValue(count);
                } catch (NumberFormatException e) {
                    liveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                liveData.postValue(0);
            }
        });
    }

    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        userRepository.deleteNotificationsForUser(username, callback);
    }

    public void fetchTotalExchanges(MutableLiveData<Integer> liveData) {
        userRepository.getTotalExchanges(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    liveData.postValue(count);
                } catch (NumberFormatException e) {
                    liveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                liveData.postValue(0);
            }
        });
    }

    public void fetchTotalItems(MutableLiveData<Integer> liveData) {
        userRepository.getTotalItems(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    liveData.postValue(count);
                } catch (NumberFormatException e) {
                    liveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                liveData.postValue(0);
            }
        });
    }

    public void fetchTotalCategories(MutableLiveData<Integer> liveData) {
        userRepository.getTotalCategories(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    liveData.postValue(count);
                } catch (NumberFormatException e) {
                    liveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                liveData.postValue(0);
            }
        });
    }
}