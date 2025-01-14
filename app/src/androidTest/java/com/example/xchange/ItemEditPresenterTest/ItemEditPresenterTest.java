package com.example.xchange.ItemEditPresenterTest;

import android.content.Context;
import android.util.Log;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ItemEditPresenterTest {

    private EditItemPresenter presenter;
    private ItemDao itemDao;
    private ExecutorService executor;

    private Item testItem;

    @Before
    public void setUp() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        presenter = new EditItemPresenter(itemDao, executor);

        testItem = new Item("testUser", "Test Item", "Test Description", Category.TECHNOLOGY, "New", null);
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {
            long id = itemDao.insertItem(testItem);
            testItem.setItemId(id); // Ensure ID is set after insertion
            latch.countDown();
        });
        latch.await(); // Wait for the insertion to complete
    }


    @After
    public void tearDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {
            itemDao.deleteItem(testItem);
            latch.countDown();
        });
        latch.await(); // Wait for deletion
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
    @Test
    public void testUpdateItem_ValidData() throws InterruptedException {
        // Arrange
        String updatedName = "Updated Item Name";
        String updatedDescription = "Updated Description";
        String updatedCondition = "Like New";
        String updatedCategory = "Books";
        ArrayList<Image> updatedImages = new ArrayList<>();
        updatedImages.add(new Image("/path/to/image.jpg", "Updated image"));

        CountDownLatch latch = new CountDownLatch(1);

        // Act
        executor.execute(() -> {
            presenter.updateItem(testItem, updatedName, updatedDescription, updatedCondition, updatedCategory, updatedImages);
            latch.countDown(); // Signal update completion
        });

        latch.await(); // Wait for the update to complete

        // Assert
        executor.execute(() -> {
            Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
            assertNotNull(updatedItem);
            assertEquals(updatedName, updatedItem.getItemName());
            assertEquals(updatedDescription, updatedItem.getItemDescription());
            assertEquals(updatedCondition, updatedItem.getItemCondition());
            assertEquals(Category.BOOKS, updatedItem.getItemCategory());

            assertEquals(updatedImages.size(), updatedItem.getItemImages().size());
            for (int i = 0; i < updatedImages.size(); i++) {
                assertEquals(updatedImages.get(i).getFilePath(), updatedItem.getItemImages().get(i).getFilePath());
                assertEquals(updatedImages.get(i).getDescription(), updatedItem.getItemImages().get(i).getDescription());
            }
        });
    }

    @Test
    public void testUpdateItem_InvalidCategory() {
        String invalidCategory = "InvalidCategory";
        presenter.updateItem(testItem, "Test", "Test", "Used", invalidCategory, new ArrayList<>());
        Item updatedItem = itemDao.getItemByIdSync(testItem.getItemId());
        assertNotNull(updatedItem);
        assertEquals(Category.TECHNOLOGY, updatedItem.getItemCategory());
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
