package com.example.xchange.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Category;
import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ItemDaoTest {

    private AppDatabase database;
    private ItemDao itemDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        itemDao = database.itemDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveItem() throws InterruptedException {
        // Create a new item
        Item item = new Item(
                "xChangerName",
                "Test Item",
                "This is a test item description",
                Category.fromDisplayName("Fashion"),
                "New",
                new ArrayList<>()
        );

        // Insert the item
        long itemId = itemDao.insertItem(item);
        assertNotNull(itemId);

        // Retrieve the item by ID
        LiveData<Item> retrievedItem = itemDao.getItemById(itemId);
        Item itemFromDb = getValue(retrievedItem);

        assertNotNull(itemFromDb);
        assertEquals(item.getItemName(), itemFromDb.getItemName());
        assertEquals(item.getItemDescription(), itemFromDb.getItemDescription());
        assertEquals(item.getItemCategory(), itemFromDb.getItemCategory());
    }

    @Test
    public void testSearchItemsByName() {
        // Create and insert items
        Item item1 = new Item("xChangerName", "Test Item 1", "Description 1", Category.fromDisplayName("Electronics"), "New", new ArrayList<>());
        Item item2 = new Item("xChangerName", "Another Item", "Description 2", Category.fromDisplayName("Fashion"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        // Search for items by name
        List<Item> results = itemDao.searchItemsByName("Test");
        assertEquals(1, results.size());
        assertEquals(item1.getItemName(), results.get(0).getItemName());
    }

    @Test
    public void testFilterItemsByCategory() {
        // Create and insert items
        Item item1 = new Item("xChangerName", "Test Item 1", "Description 1", Category.fromDisplayName("Electronics"), "New", new ArrayList<>());
        Item item2 = new Item("xChangerName", "Another Item", "Description 2", Category.fromDisplayName("Fashion"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        // Filter items by category
        List<Item> electronicsItems = itemDao.filterItemsByCategory(Category.fromDisplayName("Electronics"));
        assertEquals(1, electronicsItems.size());
        assertEquals(item1.getItemCategory(), electronicsItems.get(0).getItemCategory());
    }

    @Test
    public void testDeleteItemById() throws InterruptedException {
        // Create and insert an item
        Item item = new Item("xChangerName", "Test Item", "Description", Category.fromDisplayName("Fashion"), "New", new ArrayList<>());
        long itemId = itemDao.insertItem(item);

        // Verify item exists
        LiveData<Item> retrievedItem = itemDao.getItemById(itemId);
        assertNotNull(getValue(retrievedItem));

        // Delete the item
        itemDao.deleteItemById(itemId);

        // Verify item no longer exists
        LiveData<Item> deletedItem = itemDao.getItemById(itemId);
        assertNotNull(deletedItem);
        assertEquals(null, getValue(deletedItem));
    }

    // Helper to get LiveData value synchronously
    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] data = new Object[1];
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) data[0];
    }
}
