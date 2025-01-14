package com.example.xchange.RequestsPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.Item;
import com.example.xchange.Notification;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.request.RequestPresenter;
import com.example.xchange.request.RequestViewModel;
import com.example.xchange.xChanger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class RequestPresenterTest {

    private RequestPresenter presenter;
    private RequestViewModel viewModel;
    private UserRepository userRepository;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        viewModel = new RequestViewModel(context);
        presenter = new RequestPresenter(context, viewModel);
        userRepository = new UserRepository(context);
    }

    @Test
    public void testLoadUserItems() throws InterruptedException {
        String testUsername = "testUser";

        // Insert mock data for testing
        List<Item> testItems = new ArrayList<>();
        testItems.add(new Item("testUser", "Item 1", "Description 1", null, "Good", new ArrayList<>()));
        testItems.add(new Item("testUser", "Item 2", "Description 2", null, "Fair", new ArrayList<>()));

        for (Item item : testItems) {
            AppDatabase.getItemDao().insertItem(item);
        }

        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> viewModel.getUserItems().observeForever(items -> {
                if (items != null && items.size() == testItems.size()) {
                    assertEquals(testItems.size(), items.size());
                    latch.countDown();
                }
            }));
        });
        thread.start();

        // Call the presenter method
        presenter.loadUserItems(testUsername);

        // Wait for the response
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testCreateRequest() throws InterruptedException {
        xChanger requester = new xChanger("Requester", "requester@example.com", SimpleCalendar.today(), "pass", "location");
        xChanger requestee = new xChanger("Requestee", "requestee@example.com", SimpleCalendar.today(), "pass", "location");

        Item offeredItem = new Item("Requester", "Offered Item", "Description", null, "Good", new ArrayList<>());
        Item requestedItem = new Item("Requestee", "Requested Item", "Description", null, "Fair", new ArrayList<>());

        AppDatabase.getItemDao().insertItem(offeredItem);
        AppDatabase.getItemDao().insertItem(requestedItem);

        CountDownLatch requestLatch = new CountDownLatch(1);

        // Call presenter to create the request
        presenter.createRequest(requester, requestee, offeredItem, requestedItem);

        // Allow time for notification to be processed
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            userRepository.getNotificationsForUser(requestee.getUsername(), new UserRepository.NotificationCallback() {
                @Override
                public void onSuccess(List<Notification> notifications) {
                    try {
                        assertFalse("Notifications should not be empty", notifications.isEmpty());
                        assertEquals("Your item 'Requested Item' has been requested by Requester.", notifications.get(0).getMessage());
                    } finally {
                        requestLatch.countDown();
                    }
                }

                @Override
                public void onFailure(String message) {
                    fail("Notification retrieval failed: " + message);
                    requestLatch.countDown();
                }
            });
        }, 1000); // Delay to ensure `createRequest` processes

        assertTrue("Notification callback timed out", requestLatch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testInvalidRequestCreation() {
        final boolean[] exceptionCaught = {false}; // Use an array to modify inside the thread

        Thread thread = new Thread(() -> {
            xChanger requester = new xChanger("Requester", "requester@example.com", SimpleCalendar.today(), "pass", "location");
            xChanger requestee = new xChanger("Requestee", "requestee@example.com", SimpleCalendar.today(), "pass", "location");

            try {
                presenter.createRequest(requester, requestee, null, null);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertEquals("Offered item and requested item must not be null.", e.getMessage());
                exceptionCaught[0] = true; // Mark exception as caught
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        assertTrue("Expected exception not caught", exceptionCaught[0]);
    }
    @Test
    public void testCreateRequestWithInvalidFields() throws InterruptedException {
        xChanger requester = null; // Invalid requester
        xChanger requestee = new xChanger("Requestee", "requestee@example.com", SimpleCalendar.today(), "pass", "location");

        Item offeredItem = new Item("Requestee", "Offered Item", "Description", null, "Good", new ArrayList<>());
        Item requestedItem = new Item("Requester", "Requested Item", "Description", null, "Fair", new ArrayList<>());

        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                presenter.createRequest(requester, requestee, offeredItem, requestedItem);
                fail("Expected an IllegalArgumentException for null requester");
            } catch (IllegalArgumentException e) {
                assertEquals("Requester, requestee, offered item, and requested item must not be null.", e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        thread.start();
        assertTrue("Thread did not complete in time", latch.await(5, TimeUnit.SECONDS));
    }


}
