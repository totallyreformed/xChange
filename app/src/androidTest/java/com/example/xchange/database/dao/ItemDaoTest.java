package com.example.xchange.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        database.close();
    }

    @Test
    public void testInsertAndRetrieveItem() throws InterruptedException {
        Item item = new Item(
                "xChangerName",
                "Test Item",
                "This is a test item description",
                Category.fromDisplayName("Fashion"),
                "New",
                new ArrayList<>()
        );

        long itemId = itemDao.insertItem(item);
        assertNotNull(itemId);

        LiveData<Item> retrievedItem = itemDao.getItemById(itemId);
        Item itemFromDb = getValue(retrievedItem);

        assertNotNull(itemFromDb);
        assertEquals(item.getItemName(), itemFromDb.getItemName());
        assertEquals(item.getItemDescription(), itemFromDb.getItemDescription());
        assertEquals(item.getItemCategory(), itemFromDb.getItemCategory());
    }

    @Test
    public void testSearchItemsByName() {
        Item item1 = new Item("xChangerName", "Test Item 1", "Description 1", Category.fromDisplayName("Electronics"), "New", new ArrayList<>());
        Item item2 = new Item("xChangerName", "Another Item", "Description 2", Category.fromDisplayName("Fashion"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        List<Item> results = itemDao.searchItemsByName("Test");
        assertEquals(1, results.size());
        assertEquals(item1.getItemName(), results.get(0).getItemName());
    }

    @Test
    public void testFilterItemsByCategory() {
        Item item1 = new Item("xChangerName", "Test Item 1", "Description 1", Category.fromDisplayName("Electronics"), "New", new ArrayList<>());
        Item item2 = new Item("xChangerName", "Another Item", "Description 2", Category.fromDisplayName("Fashion"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        List<Item> electronicsItems = itemDao.filterItemsByCategory(Category.fromDisplayName("Electronics"));
        assertEquals(1, electronicsItems.size());
        assertEquals(item1.getItemCategory(), electronicsItems.get(0).getItemCategory());
    }

    @Test
    public void testUpdateItem() throws InterruptedException {
        // Insert an item into the database
        Item item = new Item("xChangerName", "Test Item", "Description", Category.fromDisplayName("Fashion"), "New", new ArrayList<>());
        long itemId = itemDao.insertItem(item);
        item.setItemId(itemId); // Set the generated ID to the item object

        // Update the item's fields
        item.setItemName("Updated Item");
        item.setItemDescription("Updated Description");
        item.setItemCategory(Category.fromDisplayName("Electronics"));

        // Perform the update operation
        itemDao.updateItem(item);

        // Retrieve the updated item from the database
        LiveData<Item> updatedItem = itemDao.getItemById(itemId);
        Item itemFromDb = getValue(updatedItem);

        // Assertions to verify the update
        assertNotNull(itemFromDb);
        assertEquals("Updated Item", itemFromDb.getItemName());
        assertEquals("Updated Description", itemFromDb.getItemDescription());
        assertEquals(Category.fromDisplayName("Electronics"), itemFromDb.getItemCategory());
    }

    @Test
    public void testDeleteItemById() throws InterruptedException {
        Item item = new Item("xChangerName", "Test Item", "Description", Category.fromDisplayName("Fashion"), "New", new ArrayList<>());
        long itemId = itemDao.insertItem(item);

        LiveData<Item> retrievedItem = itemDao.getItemById(itemId);
        assertNotNull(getValue(retrievedItem));

        itemDao.deleteItemById(itemId);

        LiveData<Item> deletedItem = itemDao.getItemById(itemId);
        assertEquals(null, getValue(deletedItem));
    }

    @Test
    public void testDeleteAllItems() throws InterruptedException {
        // Create and insert items
        Item item1 = new Item("xChangerName", "Test Item 1", "Description 1", Category.fromDisplayName("Electronics"), "New", new ArrayList<>());
        Item item2 = new Item("xChangerName", "Test Item 2", "Description 2", Category.fromDisplayName("Fashion"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        List<Item> allItems = getValue(itemDao.getAllItems());
        assertNotNull(allItems);
        assertEquals(2, allItems.size());
        itemDao.deleteAllItems();

        List<Item> afterDeletion = getValue(itemDao.getAllItems());
        assertNotNull(afterDeletion); // LiveData should not be null
        assertTrue(afterDeletion.isEmpty());
    }


    @Test
    public void testGetItemsByXChanger() {
        Item item1 = new Item("xChanger1", "Item 1", "Description 1", Category.fromDisplayName("Fashion"), "New", new ArrayList<>());
        Item item2 = new Item("xChanger2", "Item 2", "Description 2", Category.fromDisplayName("Electronics"), "Used", new ArrayList<>());

        itemDao.insertItem(item1);
        itemDao.insertItem(item2);

        List<Item> itemsForXChanger1 = itemDao.getItemsByXChanger("xChanger1");
        assertEquals(1, itemsForXChanger1.size());
        assertEquals(item1.getItemName(), itemsForXChanger1.get(0).getItemName());
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
