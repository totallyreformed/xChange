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

/**
 * ViewModel class for the MainActivity in the xChange application.
 * <p>
 * Provides LiveData for UI components and interacts with the MainActivityPresenter
 * to fetch data and update the UI state. Manages statistics and user information.
 * </p>
 */
public class MainActivityViewModel extends AndroidViewModel {

    // LiveData for Admin Statistics
    private final MutableLiveData<Integer> totalRequestsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalExchangesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalCategoriesLiveData = new MutableLiveData<>();
    private final MainActivityPresenter presenter;
    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private LiveData<List<Item>> itemsLiveData;

    /**
     * Constructor for initializing the ViewModel.
     *
     * @param application The application context.
     * @param context     The activity context for presenter initialization.
     */
    public MainActivityViewModel(Application application, Context context) {
        super(application);
        presenter = new MainActivityPresenter(this, context);
        itemsLiveData = presenter.loadItems(); // Assuming loadItems() returns LiveData
    }

    /**
     * Retrieves the LiveData for the username.
     *
     * @return LiveData containing the username.
     */
    public LiveData<String> getUsername() {
        return usernameLiveData;
    }

    /**
     * Retrieves the LiveData for the list of items.
     *
     * @return LiveData containing the list of items.
     */
    public LiveData<List<Item>> getItemsList() {
        return itemsLiveData;
    }

    /**
     * Loads the current user's information.
     *
     * @param user The current user.
     */
    public void loadUser(User user) {
        presenter.loadUser(user);
    }

    /**
     * Updates the username in the LiveData.
     *
     * @param username The new username.
     */
    public void updateUsername(String username) {
        usernameLiveData.setValue(username);
    }

    /**
     * Retrieves the LiveData for total requests statistics.
     *
     * @return LiveData containing the total requests count.
     */
    public LiveData<Integer> getTotalRequestsLiveData() {
        return totalRequestsLiveData;
    }

    /**
     * Retrieves the LiveData for total exchanges statistics.
     *
     * @return LiveData containing the total exchanges count.
     */
    public LiveData<Integer> getTotalExchangesLiveData() {
        return totalExchangesLiveData;
    }

    /**
     * Retrieves the LiveData for total items statistics.
     *
     * @return LiveData containing the total items count.
     */
    public LiveData<Integer> getTotalItemsLiveData() {
        return totalItemsLiveData;
    }

    /**
     * Retrieves the LiveData for total categories statistics.
     *
     * @return LiveData containing the total categories count.
     */
    public LiveData<Integer> getTotalCategoriesLiveData() {
        return totalCategoriesLiveData;
    }

    /**
     * Fetches the total number of requests and updates the respective LiveData.
     */
    public void fetchTotalRequests() {
        presenter.fetchTotalRequests(totalRequestsLiveData);
    }

    /**
     * Deletes notifications for the specified user.
     *
     * @param username The username of the user.
     * @param callback The callback to handle the operation result.
     */
    public void deleteNotificationsForUser(String username, UserRepository.OperationCallback callback) {
        presenter.deleteNotificationsForUser(username, callback);
    }

    /**
     * Fetches the total number of exchanges and updates the respective LiveData.
     */
    public void fetchTotalExchanges() {
        presenter.fetchTotalExchanges(totalExchangesLiveData);
    }

    /**
     * Fetches the total number of items and updates the respective LiveData.
     */
    public void fetchTotalItems() {
        presenter.fetchTotalItems(totalItemsLiveData);
    }

    /**
     * Fetches the total number of categories and updates the respective LiveData.
     */
    public void fetchTotalCategories() {
        presenter.fetchTotalCategories(totalCategoriesLiveData);
    }

    /**
     * Fetches all statistics, including total requests, exchanges, items, and categories.
     */
    public void fetchAllStatistics() {
        fetchTotalRequests();
        fetchTotalExchanges();
        fetchTotalItems();
        fetchTotalCategories();
    }
}