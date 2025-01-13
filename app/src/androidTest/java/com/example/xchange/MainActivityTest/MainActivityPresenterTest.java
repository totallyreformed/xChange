package com.example.xchange.MainActivityTest;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivityPresenter;
import com.example.xchange.MainActivity.MainActivityViewModel;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivityPresenterTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainActivityViewModel viewModel;
    private MainActivityPresenter presenter;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        // Set up a context and create required objects.
        viewModel = new MainActivityViewModel(ApplicationProvider.getApplicationContext(), ApplicationProvider.getApplicationContext());
        presenter = new MainActivityPresenter(viewModel, ApplicationProvider.getApplicationContext());
        userRepository = new UserRepository(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        // Clean up resources if needed.
        userRepository.shutdownExecutor();
    }

    @Test
    public void testLoadItems() throws InterruptedException {
        // Simulate loading items.
        CountDownLatch latch = new CountDownLatch(1);

        LiveData<List<Item>> itemsLiveData = presenter.loadItems();
        itemsLiveData.observeForever(items -> {
            if (items != null) {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testLoadUser() {
        // Simulate loading a user.
        User user = new User("testUser", "test@example.com", null, "password", "location", "xChanger");
        presenter.loadUser(user);
        assertEquals("testUser", viewModel.getUsername().getValue());

        presenter.loadUser(null);
        assertEquals("Guest", viewModel.getUsername().getValue());
    }

    @Test
    public void testFetchTotalRequests() throws InterruptedException {
        // Simulate fetching total requests.
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(count -> {
            if (count != null) {
                latch.countDown();
            }
        });

        presenter.fetchTotalRequests(liveData);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testDeleteNotificationsForUser() {
        // Simulate deleting notifications for a user.
        User user = new User("testUser", "test@example.com", null, "password", "location", "xChanger");

        presenter.deleteNotificationsForUser(user.getUsername(), new UserRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                assertTrue(true);
            }

            @Override
            public void onFailure(String message) {
                fail("Failed to delete notifications: " + message);
            }
        });
    }

    @Test
    public void testFetchTotalExchanges() throws InterruptedException {
        // Simulate fetching total exchanges.
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(count -> {
            if (count != null) {
                latch.countDown();
            }
        });

        presenter.fetchTotalExchanges(liveData);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testFetchTotalItems() throws InterruptedException {
        // Simulate fetching total items.
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(count -> {
            if (count != null) {
                latch.countDown();
            }
        });

        presenter.fetchTotalItems(liveData);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testFetchTotalCategories() throws InterruptedException {
        // Simulate fetching total categories.
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(count -> {
            if (count != null) {
                latch.countDown();
            }
        });

        presenter.fetchTotalCategories(liveData);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}
