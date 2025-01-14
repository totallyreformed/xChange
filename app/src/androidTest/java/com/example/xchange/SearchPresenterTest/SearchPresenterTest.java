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
    private UserRepository userRepository;
    private TestSearchView testView;
    private AppDatabase database;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext(); // Initialize context first
        Log.d("TestSetup", "Context: " + context);

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        Log.d("TestSetup", "Database is null: " + (database == null));
        testView = new TestSearchView();
        userRepository = new UserRepository(context);
        presenter = new SearchPresenter(context, testView);

        // Populate database with test data
        database.itemDao().insertItem(new Item("user1", "Book 1", "Description", Category.BOOKS, "New", new ArrayList<>()));
        database.itemDao().insertItem(new Item("user2", "Book 2", "Another Description", Category.BOOKS, "Used", new ArrayList<>()));
    }

    @After
    public void tearDown() {
        // Clear test data from the database
        clearTestData();
        if (database != null) {
            database.close();
        }
        presenter = null;
        userRepository = null;
    }

    @Test
    public void testPerformSearchByNameSuccess() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch("Test Item 1", null);
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Test Item 1", results.get(0).getItemName());
    }

    @Test
    public void testPerformSearchByNameNoResults() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch("Nonexistent Item", null);
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerformSearchByCategorySuccess() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        Log.d("TestCategory", "Searching for Category: " + Category.BOOKS.getDisplayName());
        presenter.performSearch(null, Category.BOOKS);

        assertTrue("Search operation timed out.", latch.await(5, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        Log.d("TestResults", "Search returned " + (results != null ? results.size() : "null") + " items");

        if (results == null || results.isEmpty()) {
            Log.d("TestError", "Query failed for Category.BOOKS");
            fail("Expected non-empty results, but got none. Check database and category alignment.");
        }

        assertEquals(Category.BOOKS, results.get(0).getItemCategory());
    }



    @Test
    public void testPerformSearchByCategoryNoResults() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch(null, Category.TOYS);
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerformSearchByNameAndCategory() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        testView.setLatch(latch);

        presenter.performSearch("Test Item 2", Category.TECHNOLOGY);
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        List<Item> results = testView.getResults();
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Test Item 2", results.get(0).getItemName());
        assertEquals(Category.TECHNOLOGY, results.get(0).getItemCategory());
    }

    private void clearTestData() {
        AppDatabase.getItemDao().deleteAllItems();
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
