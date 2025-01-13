package com.example.xchange.ItemEditPresenterTest;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.EditItem.EditItemPresenter;
import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ItemEditPresenterTest {

    private EditItemPresenter presenter;
    private ItemDao itemDao;
    private ExecutorService executor;

    private Item testItem;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        presenter = new EditItemPresenter(itemDao, executor);

        // Initialize a test item
        testItem = new Item("testUser", "Test Item", "Test Description", Category.TECHNOLOGY, "New", null);
        executor.execute(() -> itemDao.insertItem(testItem));
    }

    @After
    public void tearDown() {
        executor.execute(() -> itemDao.deleteItem(testItem));
        executor.shutdown();
    }

    @Test
    public void testUpdateItem_ValidData() {
        // Arrange
        String updatedName = "Updated Item Name";
        String updatedDescription = "Updated Description";
        String updatedCondition = "Like New";
        String updatedCategory = "Books";
        ArrayList<Image> updatedImages = new ArrayList<>();
        updatedImages.add(new Image("/path/to/image.jpg", "Updated image"));

        // Act
        presenter.updateItem(testItem, updatedName, updatedDescription, updatedCondition, updatedCategory, updatedImages);

        // Verify
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertNotNull(updatedItem);
            assertEquals(updatedName, updatedItem.getItemName());
            assertEquals(updatedDescription, updatedItem.getItemDescription());
            assertEquals(updatedCondition, updatedItem.getItemCondition());
            assertEquals(Category.BOOKS, updatedItem.getItemCategory());
            assertEquals(updatedImages, updatedItem.getItemImages());
        });
    }

    @Test
    public void testUpdateItem_InvalidCategory() {
        // Arrange
        String invalidCategory = "InvalidCategory";

        // Act
        presenter.updateItem(testItem, "Test", "Test", "Used", invalidCategory, new ArrayList<>());

        // Verify
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertEquals(Category.TECHNOLOGY, updatedItem.getItemCategory()); // Category should remain unchanged
        });
    }

    @Test
    public void testUpdateItem_NullItem() {
        // Act
        try {
            presenter.updateItem(null, "Name", "Description", "New", "Technology", new ArrayList<>());
            fail("Expected IllegalArgumentException for null item.");
        } catch (IllegalArgumentException e) {
            assertEquals("Item cannot be null", e.getMessage());
        }
    }

    @Test
    public void testUpdateItem_EmptyName() {
        // Arrange
        String emptyName = "";

        // Act
        try {
            presenter.updateItem(testItem, emptyName, "Description", "New", "Technology", new ArrayList<>());
            fail("Expected IllegalArgumentException for empty name.");
        } catch (IllegalArgumentException e) {
            assertEquals("Item name cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testUpdateItem_NullDescription() {
        // Act
        presenter.updateItem(testItem, "Valid Name", null, "New", "Technology", new ArrayList<>());

        // Verify
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertNotNull(updatedItem);
            assertNull(updatedItem.getItemDescription());
        });
    }

    @Test
    public void testUpdateItem_EmptyImages() {
        // Act
        presenter.updateItem(testItem, "Valid Name", "Valid Description", "New", "Technology", new ArrayList<>());

        // Verify
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertNotNull(updatedItem);
            assertTrue(updatedItem.getItemImages().isEmpty());
        });
    }

    @Test
    public void testUpdateItem_ValidCategoryChange() {
        // Act
        presenter.updateItem(testItem, "Valid Name", "Valid Description", "New", "Fashion", new ArrayList<>());

        // Verify
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertNotNull(updatedItem);
            assertEquals(Category.FASHION, updatedItem.getItemCategory());
        });
    }
}
