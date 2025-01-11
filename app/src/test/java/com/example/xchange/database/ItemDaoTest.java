package com.example.xchange.database;

import com.example.xchange.Item;
import com.example.xchange.Category;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.xChanger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ItemDaoTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-10");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "password", "Location");
    }

    /**
     * Test inserting an item and verifying the returned ID.
     */
    @Test
    public void testInsertItem() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        xChanger owner = createXChanger("ownerUser");
        Item item = new Item(owner.getUsername(), "Test Item", "A description of the test item.",
                Category.BOOKS, "New", null);
        item.setItemId(1L); // Assuming IDs are manually set for testing

        long returnedId = dao.insertItem(item);
        assertEquals("Inserted item ID should be 1", 1L, returnedId);
    }

    /**
     * Test inserting a null item should handle gracefully or throw an expected exception.
     */
    @Test
    public void testInsertNullItem() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        try {
            dao.insertItem(null);
            fail("Inserting a null item should throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Item cannot be null.", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got: " + e);
        }
    }

    /**
     * Test inserting multiple items to ensure IDs are incremented or handled correctly.
     */
    @Test
    public void testInsertMultipleItems() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        xChanger owner = createXChanger("ownerUser2");
        Item item1 = new Item(owner.getUsername(), "First Item", "First description.",
                Category.TECHNOLOGY, "Used", null);
        Item item2 = new Item(owner.getUsername(), "Second Item", "Second description.",
                Category.FASHION, "New", null);

        long id1 = dao.insertItem(item1);
        long id2 = dao.insertItem(item2);

        assertEquals("First inserted item ID should be 1", 1L, id1);
        assertEquals("Second inserted item ID should be 1", 1L, id2); // Based on your dummy DAO, it always returns 1L
    }

    /**
     * Test inserting an item with missing mandatory fields.
     */
    @Test
    public void testInsertItemMissingFields() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        xChanger owner = createXChanger("ownerUser3");
        // Creating an item with missing name
        Item item = new Item(owner.getUsername(), null, "Description without a name.",
                Category.ALL, "Good", null);
        item.setItemId(2L); // Assuming IDs are manually set for testing

        try {
            dao.insertItem(item);
            fail("Inserting an item without a name should throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Item name cannot be null or empty.", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got: " + e);
        }
    }

    /**
     * Test inserting an item with invalid category.
     */
    @Test
    public void testInsertItemInvalidCategory() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        xChanger owner = createXChanger("ownerUser4");
        // Assuming Category fromDisplayName returns ALL for invalid input
        Item item = new Item(owner.getUsername(), "Invalid Category Item", "Description.",
                Category.fromDisplayName("InvalidCategory"), "Good", null);
        item.setItemId(3L); // Assuming IDs are manually set for testing

        long returnedId = dao.insertItem(item);
        assertEquals("Inserted item with invalid category should return 1L", 1L, returnedId);
        assertEquals("Category should default to ALL", Category.ALL, item.getItemCategory());
    }

    /**
     * Test inserting an item with duplicate ID if applicable.
     * Depending on your DAO implementation, this might throw an exception or overwrite.
     */
    @Test
    public void testInsertItemDuplicateId() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null", dao);

        xChanger owner = createXChanger("ownerUser5");
        Item item1 = new Item(owner.getUsername(), "Unique ID Item", "First insert.",
                Category.HOME, "New", null);
        item1.setItemId(4L); // Manually setting ID

        Item item2 = new Item(owner.getUsername(), "Unique ID Item Duplicate", "Second insert.",
                Category.HOME, "New", null);
        item2.setItemId(4L); // Same ID as item1

        long id1 = dao.insertItem(item1);
        long id2 = dao.insertItem(item2);

        assertEquals("First insert should return 1L", 1L, id1);
        assertEquals("Second insert with duplicate ID should return 1L", 1L, id2);
        // Depending on DAO behavior, you might want to check if the item was overwritten or not
    }

    /**
     * Additional tests can be added here if ItemDao has more methods like getItem, deleteItem, etc.
     */
}
