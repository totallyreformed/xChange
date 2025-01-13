package com.example.xchange.Search;

import android.content.Context;
import android.util.Log;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter class for handling search functionality in the xChange application.
 * <p>
 * This class acts as an intermediary between the data layer ({@link UserRepository}) and the view ({@link SearchView}),
 * managing item searches by name and category.
 * </p>
 */
public class SearchPresenter {

    /**
     * Interface for communicating search results and errors to the view.
     */
    public interface SearchView {
        /**
         * Called when search results are successfully loaded.
         *
         * @param items The list of items matching the search criteria.
         */
        void onSearchResultsLoaded(List<Item> items);
        /**
         * Called when the search operation fails.
         *
         * @param message The error message.
         */
        void onSearchFailed(String message);
    }

    private final UserRepository userRepository;
    private final SearchView view;

    /**
     * Constructor for initializing the SearchPresenter.
     *
     * @param context The application context.
     * @param view    The view interface for updating the UI.
     */
    public SearchPresenter(Context context, SearchView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
    }

    /**
     * Performs a search operation based on the query and optional category filter.
     *
     * @param query    The search query string.
     * @param category The category filter for the search. If null, the search is performed by name only.
     */
    public void performSearch(String query, Category category) {
        if (query == null || query.trim().isEmpty()) {
            // Return an empty list when the query is empty
            view.onSearchResultsLoaded(new ArrayList<>());
            return;
        }
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
