package com.example.xchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ItemTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String xchanger = "User1";
        String name = "Laptop";
        String description = "A powerful gaming laptop.";
        Category category = Category.TECHNOLOGY;
        String condition = "New";
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("png_path/png2.png", "Front view"));

        // Act
        Item item = new Item(xchanger, name, description, category, condition, images);

        // Assert
        assertNull(item.getItemId(), "Item ID should be null before DB insertion");
        assertEquals(name, item.getItemName(), "Item name mismatch");
        assertEquals(description, item.getItemDescription(), "Item description mismatch");
        assertEquals(category, item.getItemCategory(), "Item category mismatch");
        assertEquals(condition, item.getItemCondition(), "Item condition mismatch");
        assertEquals(images, item.getItemImages(), "Item images mismatch");
    }

    @Test
    void testSetters() {
        // Arrange
        Item item = new Item("Old xChanger", "Old Name", "Old Description", Category.fromDisplayName("Category"), "Old Condition", null);

        // Act
        String newName = "New Name";
        String newDescription = "New Description";
        Category newCategory = Category.BOOKS;
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
        Item item = new Item("xChanger", "Item with Images", "Description", Category.FASHION, "Condition", null);
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
        String initialxChanger = "Mitsos";
        String initialName = "Initial Name";
        String initialDescription = "Initial Description";
        Category initialCategory = Category.BOOKS;
        String initialCondition = "Initial Condition";
        ArrayList<Image> initialImages = new ArrayList<>();
        initialImages.add(new Image("png_path/png2.png", "Front view"));

        Item item = new Item(initialxChanger, initialName, initialDescription, initialCategory, initialCondition, initialImages);

        // New values
        String newName = "Updated Name";
        String newDescription = "Updated Description";
        Category newCategory = Category.FURNITURE;
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
        Item item = new Item("xChanger", "Item", "Description", Category.SPORTS, "Condition", null);
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("C:\\Users\\swkra\\Documents\\Android\\app\\src\\test\\java\\com\\example\\xchange\\png_path\\png1.png");
        filePaths.add("C:\\Users\\swkra\\Documents\\Android\\app\\src\\test\\java\\com\\example\\xchange\\png_path\\png2.png");

        // Act
        item.addImagesFromFilePaths(filePaths);

        // Assert
        ArrayList<Image> images = item.getItemImages();
        assertNotNull(images, "Item images should not be null");
        assertEquals("C:\\Users\\swkra\\Documents\\Android\\app\\src\\test\\java\\com\\example\\xchange\\png_path\\png1.png", images.get(0).getFilePath(), "First image file path mismatch");
        assertEquals("C:\\Users\\swkra\\Documents\\Android\\app\\src\\test\\java\\com\\example\\xchange\\png_path\\png2.png", images.get(1).getFilePath(), "Second image file path mismatch");
        assertEquals(2, images.size(), "Item should have 2 images");
    }
}
