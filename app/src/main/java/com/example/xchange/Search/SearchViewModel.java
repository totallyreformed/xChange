package com.example.xchange.Search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ViewModel class for managing search operations in the xChange application.
 * <p>
 * This class handles search queries, processes search results, and updates the UI through LiveData.
 * It implements {@link SearchPresenter.SearchView} to receive callbacks from the presenter.
 * </p>
 */
public class SearchViewModel extends AndroidViewModel implements SearchPresenter.SearchView {

    private final SearchPresenter presenter;
    private final MutableLiveData<List<Item>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final String currentUser;

    /**
     * Constructor for initializing the SearchViewModel.
     *
     * @param application The application context.
     * @param user        The current user performing the search.
     */
    public SearchViewModel(@NonNull Application application, User user) {
        super(application);
        presenter = new SearchPresenter(application.getApplicationContext(), this);
        currentUser = user.getUsername();
    }

    /**
     * Retrieves the LiveData object for observing search results.
     *
     * @return LiveData containing the list of search results.
     */
    public LiveData<List<Item>> getSearchResults() {
        return searchResults;
    }

    /**
     * Retrieves the LiveData object for observing error messages.
     *
     * @return LiveData containing the error message.
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Initiates a search operation based on the query and category.
     *
     * @param query    The search query string.
     * @param category The category filter for the search. If null, the search is performed by query only.
     */
    public void searchItems(String query, Category category) {
        if (category == null) {
            // Perform search by name only.
            presenter.performSearch(query, null);
        } else {
            // Category is provided.
            if (query != null && !query.trim().isEmpty()) {
                // Perform search both by name and category.
                presenter.performSearch(query, category);
            } else {
                // Query is empty, perform search by category only.
                presenter.performSearch(null, category);
            }
        }
    }

    /**
     * Callback method triggered when search results are successfully loaded.
     * Filters out items belonging to the current user.
     *
     * @param items The list of items retrieved from the search.
     */
    @Override
    public void onSearchResultsLoaded(List<Item> items) {
        Log.d("SearchViewModel", "onSearchResultsLoaded called with items: " + items.size());
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            Log.d("SearchViewModel", "Checking item: " + item.getItemId() + ", Xchanger: " + item.getXchanger());
            if (!Objects.equals(item.getXchanger(), currentUser)) {
                filteredItems.add(item);
            }
        }
        Log.d("SearchViewModel", "Filtered items: " + filteredItems.size());
        searchResults.postValue(filteredItems);
    }

    /**
     * Callback method triggered when the search operation fails.
     * Updates the LiveData with the error message.
     *
     * @param message The error message.
     */
    @Override
    public void onSearchFailed(String message) {
        error.postValue(message);
    }
}
