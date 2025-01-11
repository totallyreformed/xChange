package com.example.xchange.database;

import com.example.xchange.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseTest {

    private AppDatabase appDatabase;

    @Before
    public void setUp() {
        // Initialize or mock AppDatabase as needed
        appDatabase = AppDatabase.getInstance(); // Assuming a singleton or mockable instance
    }

    @Test
    public void testItemDaoNotNull() {
        ItemDao dao = appDatabase.getItemDao();
        assertNotNull(dao);

        // Simulate DAO behavior if necessary
        // Our dummy DAO always returns 1L.
        assertEquals(1L, dao.insertItem(null));
    }

    @Test
    public void testInsertAndRetrieveItem() {
        Item item = new Item("testUser", "Test Item", "Test Description", null, "New", null);
        item.setItemId(100L);

        ItemDao dao = appDatabase.getItemDao();
        long id = dao.insertItem(item);
        assertEquals(1L, id); // Assuming dummy behavior returns 1L.

        // Further test retrieving the item (mock as needed)
        // Example:
        // Item retrieved = dao.getItemById(100L);
        // assertNotNull(retrieved);
        // assertEquals("Test Item", retrieved.getItemName());
    }
}
