package com.example.xchange.MainActivity;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.List;

/**
 * Presenter class for the MainActivity in the xChange application.
 * <p>
 * Handles interactions between the MainActivityViewModel and the UserRepository.
 * Manages data fetching, user updates, and statistics retrieval for the main activity.
 * </p>
 */
public class MainActivityPresenter {
    private final MainActivityViewModel viewModel;
    private final UserRepository userRepository;

    /**
     * Constructor for initializing the presenter.
     *
     * @param viewModel The ViewModel associated with MainActivity.
     * @param context   The application context for accessing the UserRepository.
     */
    public MainActivityPresenter(MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.userRepository = new UserRepository(context);
    }

    /**
     * Loads all items from the repository.
     *
     * @return A LiveData object containing the list of items.
     */
    public LiveData<List<Item>> loadItems() {
        return userRepository.getAllItems(); // Adjust based on your UserRepository implementation
    }

    /**
     * Updates the username in the ViewModel based on the provided user.
     *
     * @param user The current user. If null, the username is set to "Guest".
     */
    public void loadUser(User user) {
        if (user != null) {
            viewModel.updateUsername(user.getUsername());
        } else {
            viewModel.updateUsername("Guest");
        }
    }

    /**
     * Fetches the total number of requests and updates the provided LiveData.
     *
     * @param liveData The LiveData to post the total requests count.
     */
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

    /**
     * Deletes all notifications for a specific user.
     *
     * @param username The username of the user whose notifications should be deleted.
     * @param callback The callback to handle the operation result.
     */
    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        userRepository.deleteNotificationsForUser(username, callback);
    }

    /**
     * Fetches the total number of exchanges and updates the provided LiveData.
     *
     * @param liveData The LiveData to post the total exchanges count.
     */
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

    /**
     * Fetches the total number of items and updates the provided LiveData.
     *
     * @param liveData The LiveData to post the total items count.
     */
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

    /**
     * Fetches the total number of items and updates the provided LiveData.
     *
     * @param liveData The LiveData to post the total items count.
     */
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