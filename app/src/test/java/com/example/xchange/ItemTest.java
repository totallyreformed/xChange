package com.example.xchange;

import android.graphics.Bitmap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @BeforeEach
    void resetLastItemId() throws Exception {
        // Use reflection to reset the private static lastItemId field before each test
        Field lastItemIdField = Item.class.getDeclaredField("lastItemId");
        lastItemIdField.setAccessible(true);
        lastItemIdField.set(null, 0L);
    }

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String name = "Laptop";
        String description = "A powerful gaming laptop.";
        String category = "Electronics";
        String condition = "New";
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("png_path/png2.png", "Front view"));

        // Act
        Item item = new Item(name, description, category, condition, images);

        // Assert
        assertEquals(1L, item.getItemId(), "Item ID should be 1");
        assertEquals(name, item.getItemName(), "Item name mismatch");
        assertEquals(description, item.getItemDescription(), "Item description mismatch");
        assertEquals(category, item.getItemCategory(), "Item category mismatch");
        assertEquals(condition, item.getItemCondition(), "Item condition mismatch");
        assertEquals(images, item.getItemImages(), "Item images mismatch");
    }

    @Test
    void testItemIdAutoIncrement() {
        // Arrange & Act
        Item item1 = new Item("Item1", "Description1", "Category1", "Condition1", null);
        Item item2 = new Item("Item2", "Description2", "Category2", "Condition2", null);
        Item item3 = new Item("Item3", "Description3", "Category3", "Condition3", null);

        // Assert
        assertEquals(1L, item1.getItemId(), "First item ID should be 1");
        assertEquals(2L, item2.getItemId(), "Second item ID should be 2");
        assertEquals(3L, item3.getItemId(), "Third item ID should be 3");
    }

    @Test
    void testSetters() {
        // Arrange
        Item item = new Item("Old Name", "Old Description", "Old Category", "Old Condition", null);

        // Act
        String newName = "New Name";
        String newDescription = "New Description";
        String newCategory = "New Category";
        String newCondition = "Used Condition";
        ArrayList<Image> newImages = new ArrayList<>();
        newImages.add(new Image("png_path/png1.png", "Side view"));

        item.setItemName(newName);
        item.setItemDescription(newDescription);
        item.setItemCategory(newCategory);
        item.setItemCondition(newCondition);
        item.setItemImages(newImages);

        // Assert
        assertEquals(newName, item.getItemName(), "Item name was not updated correctly");
        assertEquals(newDescription, item.getItemDescription(), "Item description was not updated correctly");
        assertEquals(newCategory, item.getItemCategory(), "Item category was not updated correctly");
        assertEquals(newCondition, item.getItemCondition(), "Item condition was not updated correctly");
        assertEquals(newImages, item.getItemImages(), "Item images were not updated correctly");
    }

    @Test
    void testAddItemImage() {
        // Arrange
        Item item = new Item("Item with Images", "Description", "Category", "Condition", null);
        Image image1 = new Image("png_path/png2.png", "Front view");
        Image image2 = new Image("png_path/png1.png", "Back view");

        // Act
        item.addItemImage(image1);
        item.addItemImage(image2);

        // Assert
        ArrayList<Image> images = item.getItemImages();
        assertNotNull(images, "Item images should not be null");
        assertEquals(2, images.size(), "Item should have 2 images");
        assertEquals(image1, images.get(0), "First image mismatch");
        assertEquals(image2, images.get(1), "Second image mismatch");
    }

    @Test
    void testEditItem() {
        // Arrange
        String initialName = "Initial Name";
        String initialDescription = "Initial Description";
        String initialCategory = "Initial Category";
        String initialCondition = "Initial Condition";
        ArrayList<Image> initialImages = new ArrayList<>();
        initialImages.add(new Image("png_path/png2.png", "Front view"));

        Item item = new Item(initialName, initialDescription, initialCategory, initialCondition, initialImages);

        // New values
        String newName = "Updated Name";
        String newDescription = "Updated Description";
        String newCategory = "Updated Category";
        String newCondition = "Updated Condition";
        ArrayList<Image> newImages = new ArrayList<>();
        newImages.add(new Image("png_path/png1.png", "Side view"));
        newImages.add(new Image("path/to/image3.jpg", "Top view"));

        // Act
        item.editItem(newName, newDescription, newCategory, newCondition, newImages);

        // Assert
        assertEquals(newName, item.getItemName(), "Item name was not edited correctly");
        assertEquals(newDescription, item.getItemDescription(), "Item description was not edited correctly");
        assertEquals(newCategory, item.getItemCategory(), "Item category was not edited correctly");
        assertEquals(newCondition, item.getItemCondition(), "Item condition was not edited correctly");
        assertEquals(newImages, item.getItemImages(), "Item images were not edited correctly");
    }

    @Test
    void testAddImagesFromFilePaths() {
        // Arrange
        Item item = new Item("Item", "Description", "Category", "Condition", null);
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("png_path/png2.png");
        filePaths.add("png_path/png1.png");

        // Act
        item.addImagesFromFilePaths(filePaths);

        // Assert
        ArrayList<Image> images = item.getItemImages();
        assertNotNull(images, "Item images should not be null");
        assertEquals(2, images.size(), "Item should have 2 images");
        assertEquals("png_path/png2.png", images.get(0).getFilePath(), "First image file path mismatch");
        assertEquals("png_path/png1.png", images.get(1).getFilePath(), "Second image file path mismatch");
    }
}
