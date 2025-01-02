package com.example.xchange.MainActivity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.User;

import com.example.xchange.database.UserRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    // LiveData for Admin Statistics
    private final MutableLiveData<Integer> totalRequestsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalExchangesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalCategoriesLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    private final MainActivityPresenter presenter;
    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private LiveData<List<Item>> itemsLiveData;

    public MainActivityViewModel(Application application) {
        super(application);
        presenter = new MainActivityPresenter(this);
        itemsLiveData = (LiveData<List<Item>>) presenter.loadItems();
        userRepository = new UserRepository(application.getApplicationContext());
    }

    // Getter for username LiveData
    public LiveData<String> getUsername() {
        return usernameLiveData;
    }

    // Getter for items LiveData
    public LiveData<List<Item>> getItemsList() {
        return itemsLiveData;
    }

    // Method to load user
    public void loadUser(User user) {
        presenter.loadUser(user);
    }

    // Called by the Presenter to update username
    public void updateUsername(String username) {
        usernameLiveData.setValue(username);
    }

    // Getters for LiveData
    public LiveData<Integer> getTotalRequestsLiveData() {
        return totalRequestsLiveData;
    }

    public LiveData<Integer> getTotalExchangesLiveData() {
        return totalExchangesLiveData;
    }

    public LiveData<Integer> getTotalItemsLiveData() {
        return totalItemsLiveData;
    }

    public LiveData<Integer> getTotalCategoriesLiveData() {
        return totalCategoriesLiveData;
    }

    // Methods to fetch statistics
    public void fetchTotalRequests() {
        userRepository.getTotalRequests(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    totalRequestsLiveData.postValue(count);
                } catch (NumberFormatException e) {
                    totalRequestsLiveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                totalRequestsLiveData.postValue(0);
            }
        });
    }

    public void fetchTotalExchanges() {
        userRepository.getTotalExchanges(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    totalExchangesLiveData.postValue(count);
                } catch (NumberFormatException e) {
                    totalExchangesLiveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                totalExchangesLiveData.postValue(0);
            }
        });
    }

    public void fetchTotalItems() {
        userRepository.getTotalItems(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    totalItemsLiveData.postValue(count);
                } catch (NumberFormatException e) {
                    totalItemsLiveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                totalItemsLiveData.postValue(0);
            }
        });
    }

    public void fetchTotalCategories() {
        userRepository.getTotalCategories(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                try {
                    int count = Integer.parseInt(stats);
                    totalCategoriesLiveData.postValue(count);
                } catch (NumberFormatException e) {
                    totalCategoriesLiveData.postValue(0);
                }
            }

            @Override
            public void onFailure(String message) {
                totalCategoriesLiveData.postValue(0);
            }
        });
    }

    // Method to fetch all statistics at once
    public void fetchAllStatistics() {
        fetchTotalRequests();
        fetchTotalExchanges();
        fetchTotalItems();
        fetchTotalCategories();
    }
}