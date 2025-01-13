package com.example.xchange.RejectRequestTest;

import static org.junit.Assert.*;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.xChanger;
import com.example.xchange.RejectRequest.RejectRequestPresenter;
import com.example.xchange.RejectRequest.RejectRequestViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RejectRequestPresenterTest {

    private RejectRequestPresenter presenter;
    private Context context;
    private xChanger testXChanger;
    private Request testRequest;
    private Counteroffer testCounteroffer;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        presenter = new RejectRequestPresenter(context);

        // Create test xChanger
        testXChanger = new xChanger(
                "testUser",
                "test@example.com",
                SimpleCalendar.today(),
                "password",
                "Test City"
        );

        // Create test items
        Item offeredItem = new Item("testUser", "Offered Item", "Description", null, "New", null);
        Item requestedItem = new Item("otherUser", "Requested Item", "Description", null, "Used", null);

        // Create test request
        testRequest = new Request(testXChanger, testXChanger, offeredItem, requestedItem, SimpleCalendar.today());

        // Create test counteroffer
        testCounteroffer = new Counteroffer(testRequest, offeredItem, testXChanger, testXChanger);
    }

    @After
    public void tearDown() {
        presenter = null;
        context = null;
        testXChanger = null;
        testRequest = null;
        testCounteroffer = null;
    }

    @Test
    public void testRejectRequestSuccess() throws InterruptedException {
        float rating = 4.5f;
        CountDownLatch latch = new CountDownLatch(1);

        presenter.rejectRequest(testXChanger, testRequest, rating, new RejectRequestViewModel.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                assertTrue(true);
                latch.countDown();
            }

            @Override
            public void onFailure(String message) {
                fail("Rejection of request failed: " + message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testRejectRequestFailure() throws InterruptedException {
        Request invalidRequest = null;
        CountDownLatch latch = new CountDownLatch(1);

        presenter.rejectRequest(testXChanger, invalidRequest, 0, new RejectRequestViewModel.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                fail("Rejection of invalid request should not succeed.");
                latch.countDown();
            }

            @Override
            public void onFailure(String message) {
                assertNotNull(message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testRejectCounterofferSuccess() throws InterruptedException {
        float rating = 3.0f;
        CountDownLatch latch = new CountDownLatch(1);

        presenter.rejectCounteroffer(testCounteroffer, rating, new RejectRequestViewModel.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                assertTrue(true);
                latch.countDown();
            }

            @Override
            public void onFailure(String message) {
                fail("Rejection of counteroffer failed: " + message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testRejectCounterofferFailure() throws InterruptedException {
        Counteroffer invalidCounteroffer = null;
        CountDownLatch latch = new CountDownLatch(1);

        presenter.rejectCounteroffer(invalidCounteroffer, 0, new RejectRequestViewModel.RejectRequestCallback() {
            @Override
            public void onSuccess() {
                fail("Rejection of invalid counteroffer should not succeed.");
                latch.countDown();
            }

            @Override
            public void onFailure(String message) {
                assertNotNull(message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}
