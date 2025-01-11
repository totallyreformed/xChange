package com.example.xchange.database;

import com.example.xchange.Item;
import com.example.xchange.Category;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.xChanger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ItemDaoTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-10");
    private ItemDao dao;

    @Before
    public void setUp() {
        // Initialize the ItemDao (assuming AppDatabase.getItemDao() returns a mocked or stubbed instance)
        dao = AppDatabase.getItemDao();
        assertNotNull("ItemDao should not be null in setup", dao);
    }

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "password", "Location");
    }

    @Test
    public void testInsertItem() {
        xChanger owner = createXChanger("ownerUser");
        Item item = new Item(owner.getUsername(), "Test Item", "A description of the test item.",
                Category.BOOKS, "New", null);
        item.setItemId(1L);

        long returnedId = dao.insertItem(item);
        assertEquals("Inserted item ID should be 1", 1L, returnedId);
    }

    @Test
    public void testInsertNullItem() {
        try {
            dao.insertItem(null);
            fail("Inserting a null item should throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Item cannot be null.", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got: " + e);
        }
    }

    @Test
    public void testInsertMultipleItems() {
        xChanger owner = createXChanger("ownerUser2");
        Item item1 = new Item(owner.getUsername(), "First Item", "First description.",
                Category.TECHNOLOGY, "Used", null);
        Item item2 = new Item(owner.getUsername(), "Second Item", "Second description.",
                Category.FASHION, "New", null);

        long id1 = dao.insertItem(item1);
        long id2 = dao.insertItem(item2);

        assertEquals("First inserted item ID should be 1", 1L, id1);
        assertEquals("Second inserted item ID should be 1", 1L, id2); // Based on your dummy DAO
    }

    @Test
    public void testInsertItemMissingFields() {
        xChanger owner = createXChanger("ownerUser3");
        Item item = new Item(owner.getUsername(), null, "Description without a name.",
                Category.ALL, "Good", null);
        item.setItemId(2L);

        try {
            dao.insertItem(item);
            fail("Inserting an item without a name should throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Item name cannot be null or empty.", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got: " + e);
        }
    }

    @Test
    public void testInsertItemInvalidCategory() {
        xChanger owner = createXChanger("ownerUser4");
        Item item = new Item(owner.getUsername(), "Invalid Category Item", "Description.",
                Category.fromDisplayName("InvalidCategory"), "Good", null);
        item.setItemId(3L);

        long returnedId = dao.insertItem(item);
        assertEquals("Inserted item with invalid category should return 1L", 1L, returnedId);
        assertEquals("Category should default to ALL", Category.ALL, item.getItemCategory());
    }

    @Test
    public void testInsertItemDuplicateId() {
        xChanger owner = createXChanger("ownerUser5");
        Item item1 = new Item(owner.getUsername(), "Unique ID Item", "First insert.",
                Category.HOME, "New", null);
        item1.setItemId(4L);

        Item item2 = new Item(owner.getUsername(), "Unique ID Item Duplicate", "Second insert.",
                Category.HOME, "New", null);
        item2.setItemId(4L);

        long id1 = dao.insertItem(item1);
        long id2 = dao.insertItem(item2);

        assertEquals("First insert should return 1L", 1L, id1);
        assertEquals("Second insert with duplicate ID should return 1L", 1L, id2);
    }
}
