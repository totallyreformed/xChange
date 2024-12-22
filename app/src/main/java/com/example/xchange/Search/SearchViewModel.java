// File: SearchViewModel.java
package com.example.xchange.Search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;

import java.util.List;

public class SearchViewModel extends AndroidViewModel implements SearchPresenter.SearchView {

    private final SearchPresenter presenter;
    private final MutableLiveData<List<Item>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        presenter = new SearchPresenter(application.getApplicationContext(), this);
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
    public void searchItems(String query, String category) {
        presenter.performSearch(query, category);
    }

    @Override
    public void onSearchResultsLoaded(List<Item> items) {
        searchResults.postValue(items);
    }

    @Override
    public void onSearchFailed(String message) {
        error.postValue(message);
    }
}
