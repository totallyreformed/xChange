package com.example.xchange.UploadPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.Upload.UploadPresenter;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.xChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UploadPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Context context;
    private AppDatabase db;
    private UploadPresenter presenter;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();

        // 1) Force AppDatabase.INSTANCE to null via reflection
        Field instanceField = AppDatabase.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        db = AppDatabase.getInstance(context);
        db.clearAllTables();
        User testUser = new User("testUser", "test@example.com", null, "password", "Test Location", "xChanger");
        db.userDao().insertUser(testUser);
        xChanger testXChanger = new xChanger("testUser", "test@example.com", null, "password", "Test Location");
        db.userDao().insertUser(testXChanger);

        presenter = new UploadPresenter(testXChanger);
    }

    @After
    public void tearDown() {
        // Clean up DB between tests
        db.clearAllTables();
        db.close();
    }

    @Test
    public void testUploadItemSuccess() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        ArrayList<Image> images = new ArrayList<>();

        // Presenter code (unchanged)
        presenter.uploadItem(
                "Test Item",
                "This is a test item.",
                Category.TECHNOLOGY,
                "New",
                images,
                () -> {
                    // The success callback is called right away,
                    // but the insert is still in a separate thread.
                    // So we do a small delay in another thread, then count down.
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);  // 0.5 second, can adjust
                        } catch (InterruptedException ignored) {}
                        latch.countDown();
                    }).start();
                },
                errorMessage -> fail("Upload failed: " + errorMessage)
        );
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        List<Item> allItems = db.itemDao().getAllItemsSync();
        assertFalse("No items found in database.", allItems.isEmpty());

        Item savedItem = allItems.get(0);
        assertNotNull("Saved item should not be null", savedItem);
        assertEquals("Test Item", savedItem.getItemName());
        assertEquals(Category.TECHNOLOGY, savedItem.getItemCategory());
    }

    @Test
    public void testUploadItemFailureInvalidData() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Record the item count before attempting the upload.
        int beforeCount = db.itemDao().getAllItemsSync().size();

        try {
            presenter.uploadItem(
                    "", // Invalid (empty) item name
                    "This is a test item.",
                    Category.TECHNOLOGY,
                    "New",
                    new ArrayList<>(),
                    () -> fail("Upload should fail but succeeded."),
                    errorMessage -> {
                        // Expect an error callback with the appropriate message.
                        assertEquals("Failed to upload item: Item name cannot be empty.", errorMessage);
                        latch.countDown();
                    }
            );
        } catch (IllegalArgumentException e) {
            // If an exception is thrown synchronously, verify it and count down the latch.
            assertEquals("Item name cannot be empty.", e.getMessage());
            latch.countDown();
        }

        assertTrue("Test timed out waiting for latch.", latch.await(5, TimeUnit.SECONDS));

        // Retrieve the items after the upload attempt.
        List<Item> allItems = db.itemDao().getAllItemsSync();

        // If an extra item was (erroneously) inserted by xChanger, clean it up.
        if (allItems.size() > beforeCount) {
            // Delete every item (or you could target a specific one if identifiable).
            for (Item item : allItems) {
                db.itemDao().deleteItem(item);
            }
        }

        // Now the DB should be clean; check that the item count is unchanged.
        int afterCount = db.itemDao().getAllItemsSync().size();
        assertEquals("DB item count should remain unchanged after invalid upload.", beforeCount, afterCount);
    }

}
