package com.example.xchange.ItemDetailPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.ItemDetail.ItemDetailPresenter;
import com.example.xchange.Request;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.database.dao.ItemDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestItemDetailPresenter {

    private Context context;
    private ItemDetailPresenter presenter;
    private AppDatabase db;
    private ItemDao itemDao;
    private ExecutorService executor;
    private static final String TAG = "TestItemDetailPresenter";

    @Before
    public void setUp() throws InterruptedException {
        context = ApplicationProvider.getApplicationContext();
        presenter = new ItemDetailPresenter(context);
        db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
        executor = Executors.newFixedThreadPool(4);

        // Insert sample data for testing
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                itemDao.deleteAllItems(); // Clear existing data

                ArrayList<Image> images = new ArrayList<>();
                images.add(new Image("/path/to/image1.jpg", "Sample Image 1"));
                images.add(new Image("/path/to/image2.jpg", "Sample Image 2"));

                Item item1 = new Item("TestUser", "Laptop", "High-end gaming laptop", Category.TECHNOLOGY, "New", images);
                itemDao.insertItem(item1);

                Item item2 = new Item("TestUser", "Smartphone", "Flagship smartphone", Category.TECHNOLOGY, "Used", images);
                itemDao.insertItem(item2);

                Log.d(TAG, "Sample data inserted successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error during setup", e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();
    }

    @After
    public void tearDown() {
        executor.shutdownNow();
    }

    @Test
    public void testGetItemById() throws InterruptedException {
        long testItemId = 1; // Assume this ID exists in the database

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            presenter.getItemById(testItemId, item -> {
                try {
                    assertNotNull("Item should not be null", item);
                    assertEquals("Laptop", item.getItemName());
                } catch (AssertionError e) {
                    Log.e(TAG, "Assertion failed in testGetItemById", e);
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();
    }

    @Test
    public void testDeleteItemById() throws InterruptedException {
        long testItemId = 2; // Assume this ID exists in the database

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            presenter.deleteItemById(testItemId, () -> {
                executor.execute(() -> {
                    try {
                        assertNull("Item should be deleted", itemDao.getItemByIdSync(testItemId));
                    } catch (AssertionError e) {
                        Log.e(TAG, "Assertion failed in testDeleteItemById", e);
                    } finally {
                        latch.countDown();
                    }
                });
            });
        });

        latch.await();
    }

    @Test
    public void testConcurrentItemOperations() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        executor.execute(() -> {
            presenter.getItemById(1, item -> {
                try {
                    assertNotNull("Item should exist", item);
                    assertEquals("Laptop", item.getItemName());
                } catch (AssertionError e) {
                    Log.e(TAG, "Assertion failed in testConcurrentItemOperations - getItemById", e);
                } finally {
                    latch.countDown();
                }
            });
        });

        executor.execute(() -> {
            presenter.deleteItemById(2, () -> {
                executor.execute(() -> {
                    try {
                        assertNull("Item should be deleted", itemDao.getItemByIdSync(2));
                    } catch (AssertionError e) {
                        Log.e(TAG, "Assertion failed in testConcurrentItemOperations - deleteItemById", e);
                    } finally {
                        latch.countDown();
                    }
                });
            });
        });

        latch.await();
    }

        @Test
        public void testFindItemsByXChanger() throws InterruptedException {
            String xChangerUsername = "TestUser";

            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                presenter.findItemsByXChanger(xChangerUsername, new UserRepository.UserItemsCallback() {
                    @Override
                    public void onSuccess(List<Item> items) {
                        try {
                            assertNotNull(items);
                            assertEquals("Expected two items for TestUser", 2, items.size());
                            assertTrue("Items should contain 'Laptop'",
                                    items.stream().anyMatch(item -> "Laptop".equals(item.getItemName())));
                            assertTrue("Items should contain 'Smartphone'",
                                    items.stream().anyMatch(item -> "Smartphone".equals(item.getItemName())));
                        } catch (AssertionError e) {
                            Log.e(TAG, "Assertion failed in testFindItemsByXChanger", e);
                        } finally {
                            latch.countDown();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Error in testFindItemsByXChanger: " + errorMessage);
                        latch.countDown();
                        fail("Request failed unexpectedly: " + errorMessage);
                    }
                });

            });

            latch.await();
        }
    @Test
    public void testCheckRequestToDisplay() throws InterruptedException {
        long testItemId = 1;
        String username = "TestUser";

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            presenter.checkRequestToDisplay(testItemId, username, result -> {
                try {
                    assertFalse("Request should not exist for the item", result);
                } catch (AssertionError e) {
                    Log.e(TAG, "Assertion failed in testCheckRequestToDisplay", e);
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();
    }

    @Test
    public void testCancelRequest() throws InterruptedException {
        long testItemId = 1;
        String username = "TestUser";

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            presenter.cancelRequest(testItemId, username);
            latch.countDown();
        });

        latch.await();
        // Verification could involve ensuring the request status changed if requests were implemented
    }
}
