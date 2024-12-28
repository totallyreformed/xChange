// File: SearchViewModel.java
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

public class SearchViewModel extends AndroidViewModel implements SearchPresenter.SearchView {

    private final SearchPresenter presenter;
    private final MutableLiveData<List<Item>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final String currentUser;

    public SearchViewModel(@NonNull Application application, User user) {
        super(application);
        presenter = new SearchPresenter(application.getApplicationContext(), this);
        currentUser = user.getUsername();
    }

    /**
     * Expose LiveData for search results.
     *
     * @return LiveData containing a list of items.
     */
    public LiveData<List<Item>> getSearchResults() {
        return searchResults;
    }

    /**
     * Expose LiveData for error messages.
     *
     * @return LiveData containing error messages.
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Initiate a search operation.
     *
     * @param query    The search query (item name).
     * @param category The category to filter by (can be null or empty for no filtering).
     */

    public void searchItems(String query, Category category) {
        if (category == null) {
            // Perform search without category filter
            presenter.performSearch(query, null);
        } else {
            // Perform search with category filter
            presenter.performSearch(query, category);
        }
    }

    @Override
    public void onSearchResultsLoaded(List<Item> items) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            Log.d("SearchViewModel", "Item Xchanger: " + item.getXchanger() + ", CurrentUser: " + currentUser);
            if (!Objects.equals(item.getXchanger(), currentUser)) {
                Log.d("SearchViewModel", "Adding item: " + item.getItemId());
                filteredItems.add(item);
            } else {
                Log.d("SearchViewModel", "Excluding item: " + item.getItemId());
            }
        }
        searchResults.postValue(filteredItems); // Δημοσίευση των φιλτραρισμένων αποτελεσμάτων
    }

    @Override
    public void onSearchFailed(String message) {
        error.postValue(message);
    }
}
