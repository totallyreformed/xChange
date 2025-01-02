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

    public LiveData<List<Item>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getError() {
        return error;
    }


    public void searchItems(String query, Category category) {
        if (category == null) {
            presenter.performSearch(query, null);
        } else {
            presenter.performSearch(null, category);
        }
    }

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


    @Override
    public void onSearchFailed(String message) {
        error.postValue(message);
    }
}
