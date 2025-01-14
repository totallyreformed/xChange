package com.example.xchange.SearchPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.Search.SearchPresenter;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SearchPresenterTest {

    private SearchPresenter presenter;
    private TestSearchView testView;

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        testView = new TestSearchView();
        presenter = new SearchPresenter(context, testView);
        AppDatabase.getItemDao().deleteAllItems();
        AppDatabase.getItemDao().insertItem(new Item("user1", "Book 1", "Description", Category.BOOKS, "New", new ArrayList<>()));
        AppDatabase.getItemDao().insertItem(new Item("user2", "Book 2", "Another Description", Category.BOOKS, "Used", new ArrayList<>()));

    }


    @After
    public void tearDown() {
        presenter = null;
    }

    @Test
    public void testDaoSearchItemsByCategory() throws InterruptedException {
        // Insert an extra test item in a different category
        AppDatabase.getItemDao().insertItem(new Item("user3", "Toy 1", "Toy Description", Category.TOYS, "New", new ArrayList<>()));

        // Setup latch to wait for the asynchronous callback.
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        // Perform search: since query is null, the presenter will filter by category only.
        presenter.performSearch(null, Category.BOOKS);

        // Wait for the callback (with a 5 second timeout)
        assertTrue("Latch timed out!", latch.await(5, TimeUnit.SECONDS));

        // Now get the results and run validations.
        List<Item> books = testView.getResults();
        assertNotNull("Results should not be null", books);
        assertFalse("Results should not be empty", books.isEmpty());
        assertEquals("Expected 2 items in the Books category", 2, books.size());
    }


    @Test
    public void testPerformSearchByNameNoResults() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch("Nonexistent Item", null);
        assertTrue("Latch timed out!", latch.await(5, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty", results.isEmpty());
    }

    @Test
    public void testPerformSearchByCategorySuccess() throws InterruptedException {
        // Setup latch to wait for the asynchronous callback.
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        // Perform the search for the Books category.
        presenter.performSearch(null, Category.BOOKS);

        // Wait for the callback (with a 5 second timeout)
        assertTrue("Latch timed out!", latch.await(5, TimeUnit.SECONDS));

        // Retrieve the results from the test view.
        List<Item> results = testView.getResults();

        // Validate the results.
        assertNotNull("Results should not be null", results);
        assertFalse("Results should not be empty", results.isEmpty());
        assertEquals("Expected 2 items in the Books category", 2, results.size());

        // Optionally log the results for debugging purposes.
        for (Item item : results) {
            Log.d("TestDebug", "Retrieved Item: " + item.getItemName() + ", Category: " + item.getItemCategory());
        }
    }


    @Test
    public void testPerformSearchByCategoryNoResults() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch(null, Category.TOYS);
        assertTrue("Latch timed out!", latch.await(5, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty", results.isEmpty());
    }

    @Test
    public void testPerformSearchByNameAndCategory() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        // Insert items
        AppDatabase.getItemDao().insertItem(new Item("user1", "Test Item 2", "Description", Category.TECHNOLOGY, "New", new ArrayList<>()));

        // Perform search
        presenter.performSearch("Test Item 2", Category.TECHNOLOGY);
        assertTrue("Latch timed out!", latch.await(5, TimeUnit.SECONDS));

        // Validate results
        List<Item> results = testView.getResults();
        assertNotNull("Results should not be null", results);
        assertFalse("Results should not be empty", results.isEmpty());
        assertEquals("Unexpected item name.", "Test Item 2", results.get(0).getItemName());
        assertEquals("Unexpected category.", Category.TECHNOLOGY, results.get(0).getItemCategory());
    }

    /**
     * A test implementation of the SearchView interface.
     */
    private static class TestSearchView implements SearchPresenter.SearchView {

        private List<Item> results;
        private CountDownLatch latch;

        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        public List<Item> getResults() {
            return results;
        }

        @Override
        public void onSearchResultsLoaded(List<Item> items) {
            Log.d("TestSearchView", "onSearchResultsLoaded called with items: " + items.size());
            this.results = items;
            if (latch != null) latch.countDown();
        }

        @Override
        public void onSearchFailed(String message) {
            Log.d("TestSearchView", "onSearchFailed called with message: " + message);
            this.results = null;
            if (latch != null) latch.countDown();
        }
    }
}
