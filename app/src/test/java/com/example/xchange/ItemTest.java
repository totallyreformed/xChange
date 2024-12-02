package com.example.xchange;

import android.graphics.Bitmap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Item testItem;

    @BeforeEach
    public void setUp() {
        // Reset the static field for consistent testing
        resetLastItemId();

        // Initialize test item
        ArrayList<Bitmap> images = new ArrayList<>();
        testItem = new Item("Test Item", "This is a test item.", "Electronics", "New", images);
    }

    @Test
    public void testItemCreation() {
        assertEquals(1L, testItem.getItemId());
        assertEquals("Test Item", testItem.getItemName());
        assertEquals("This is a test item.", testItem.getItemDescription());
        assertEquals("Electronics", testItem.getItemCategory());
        assertEquals("New", testItem.getItemCondition());
        assertNotNull(testItem.getItemImages());
        assertTrue(testItem.getItemImages().isEmpty());
    }

    @Test
    public void testSetItemName() {
        testItem.setItemName("Updated Item Name");
        assertEquals("Updated Item Name", testItem.getItemName());
    }

    @Test
    public void testSetItemDescription() {
        testItem.setItemDescription("Updated description.");
        assertEquals("Updated description.", testItem.getItemDescription());
    }

    @Test
    public void testSetItemCategory() {
        testItem.setItemCategory("Books");
        assertEquals("Books", testItem.getItemCategory());
    }

    @Test
    public void testSetItemCondition() {
        testItem.setItemCondition("Used");
        assertEquals("Used", testItem.getItemCondition());
    }

    @Test
    public void testSetItemImages() {
        ArrayList<Bitmap> newImages = new ArrayList<>();
        testItem.setItemImages(newImages);
        assertNotNull(testItem.getItemImages());
        assertEquals(newImages, testItem.getItemImages());
    }

    @Test
    public void testAddItemImage() {
        // Create a mock Bitmap
        Bitmap mockImage = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        ArrayList<Bitmap> images = new ArrayList<>();
        Item testItem = new Item("Test Item", "Description", "Category", "New", images);
        testItem.addItemImage(mockImage);

        assertEquals(1, testItem.getItemImages().size());
        assertTrue(testItem.getItemImages().contains(mockImage));
    }

    @Test
    public void testEditItem() {
        ArrayList<Bitmap> newImages = new ArrayList<>();
        testItem.editItem("Edited Name", "Edited Description", "Clothing", "Fair", newImages);

        assertEquals("Edited Name", testItem.getItemName());
        assertEquals("Edited Description", testItem.getItemDescription());
        assertEquals("Clothing", testItem.getItemCategory());
        assertEquals("Fair", testItem.getItemCondition());
        assertEquals(newImages, testItem.getItemImages());
    }

    @Test
    public void testIncrementItemId() {
        ArrayList<Bitmap> images = new ArrayList<>();
        Item secondItem = new Item("Second Item", "Another test item.", "Books", "Used", images);
        assertEquals(2L, secondItem.getItemId());
    }

    // Helper method to reset the static `lastItemId` field for testing
    private void resetLastItemId() {
        try {
            java.lang.reflect.Field field = Item.class.getDeclaredField("lastItemId");
            field.setAccessible(true);
            field.set(null, 0L);
        } catch (Exception e) {
            fail("Failed to reset lastItemId: " + e.getMessage());
        }
    }
}
