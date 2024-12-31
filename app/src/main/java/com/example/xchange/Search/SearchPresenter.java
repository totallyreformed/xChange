// File: SearchPresenter.java
package com.example.xchange.Search;

import android.content.Context;
import android.util.Log;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.database.UserRepository;

import java.util.List;

public class SearchPresenter {

    public interface SearchView {
        void onSearchResultsLoaded(List<Item> items);
        void onSearchFailed(String message);
    }

    private final UserRepository userRepository;
    private final SearchView view;

    public SearchPresenter(Context context, SearchView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
    }

    public void performSearch(String query, Category category) {
        if (category == null) {
            // Perform search by name only
            userRepository.searchItemsByName(query, new UserRepository.UserItemsCallback() {
                @Override
                public void onSuccess(List<Item> items) {
                    view.onSearchResultsLoaded(items);
                }

                @Override
                public void onFailure(String message) {
                    view.onSearchFailed(message);
                }
            });
        } else {
            // Perform search with category filter
            userRepository.searchItemsByNameAndCategory(query, category, new UserRepository.UserItemsCallback() {
                @Override
                public void onSuccess(List<Item> items) {
                    view.onSearchResultsLoaded(items);
                }

                @Override
                public void onFailure(String message) {
                    view.onSearchFailed(message);
                }
            });
        }
    }

}
