package com.example.xchange.MainActivity;

import android.app.Application;
import android.content.Context;

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
    private final MainActivityPresenter presenter;
    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private LiveData<List<Item>> itemsLiveData;

    public MainActivityViewModel(Application application, Context context) {
        super(application);
        presenter = new MainActivityPresenter(this, context);
        itemsLiveData = presenter.loadItems(); // Assuming loadItems() returns LiveData
    }

    public LiveData<String> getUsername() {
        return usernameLiveData;
    }

    public LiveData<List<Item>> getItemsList() {
        return itemsLiveData;
    }

    public void loadUser(User user) {
        presenter.loadUser(user);
    }

    public void updateUsername(String username) {
        usernameLiveData.setValue(username);
    }

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

    public void fetchTotalRequests() {
        presenter.fetchTotalRequests(totalRequestsLiveData);
    }

    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        presenter.deleteNotificationsForUser(username, callback);
    }

    public void fetchTotalExchanges() {
        presenter.fetchTotalExchanges(totalExchangesLiveData);
    }

    public void fetchTotalItems() {
        presenter.fetchTotalItems(totalItemsLiveData);
    }

    public void fetchTotalCategories() {
        presenter.fetchTotalCategories(totalCategoriesLiveData);
    }

    public void fetchAllStatistics() {
        fetchTotalRequests();
        fetchTotalExchanges();
        fetchTotalItems();
        fetchTotalCategories();
    }
}