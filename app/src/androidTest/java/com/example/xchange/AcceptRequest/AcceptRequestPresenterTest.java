package com.example.xchange.AcceptRequest;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptRequestPresenterTest {

    private AcceptRequestPresenter presenter;
    private UserRepository userRepository;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        userRepository = new UserRepository(context);
        presenter = new AcceptRequestPresenter(context);
    }

    @After
    public void tearDown() {
        presenter = null;
        userRepository.shutdownExecutor();
    }

    @Test
    public void testAcceptRequest_RealObjects() {
        xChanger requester = new xChanger("requester", "requester@example.com", SimpleCalendar.today(), "password", "location");
        xChanger requestee = new xChanger("requestee", "requestee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("requester", "Offered Item", "Description", null, "New", null);
        Item requestedItem = new Item("requestee", "Requested Item", "Description", null, "Used", null);
        Request request = new Request(requester, requestee, offeredItem, requestedItem, SimpleCalendar.today());
        float rating = 4.5f;

        presenter.acceptRequest(request, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                assertTrue(xChangeId > 0);
                System.out.println("Request accepted successfully with xChangeId: " + xChangeId);
            }

            @Override
            public void onFailure(String message) {
                fail("Request acceptance failed with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptCounteroffer_RealObjects() {
        xChanger counterofferer = new xChanger("counterofferer", "counterofferer@example.com", SimpleCalendar.today(), "password", "location");
        xChanger counterofferee = new xChanger("counterofferee", "counterofferee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("counterofferer", "Offered Counter Item", "Description", null, "Good", null);
        Item requestedItem = new Item("counterofferee", "Requested Counter Item", "Description", null, "Fair", null);
        Request request = new Request(counterofferer, counterofferee, offeredItem, requestedItem, SimpleCalendar.today());
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, counterofferer, counterofferee);
        float rating = 4.0f;

        presenter.acceptCounteroffer(counteroffer, rating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                assertTrue(xChangeId > 0);
                System.out.println("Counteroffer accepted successfully with xChangeId: " + xChangeId);
            }

            @Override
            public void onFailure(String message) {
                fail("Counteroffer acceptance failed with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptRequest_InvalidRating() {
        xChanger requester = new xChanger("requester", "requester@example.com", SimpleCalendar.today(), "password", "location");
        xChanger requestee = new xChanger("requestee", "requestee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("requester", "Offered Item", "Description", null, "New", null);
        Item requestedItem = new Item("requestee", "Requested Item", "Description", null, "Used", null);
        Request request = new Request(requester, requestee, offeredItem, requestedItem, SimpleCalendar.today());
        float invalidRating = -1.0f;

        presenter.acceptRequest(request, invalidRating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                fail("Request acceptance should have failed with invalid rating.");
            }

            @Override
            public void onFailure(String message) {
                assertNotNull(message);
                System.out.println("Request acceptance failed as expected with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptCounteroffer_InvalidRating() {
        xChanger counterofferer = new xChanger("counterofferer", "counterofferer@example.com", SimpleCalendar.today(), "password", "location");
        xChanger counterofferee = new xChanger("counterofferee", "counterofferee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("counterofferer", "Offered Counter Item", "Description", null, "Good", null);
        Item requestedItem = new Item("counterofferee", "Requested Counter Item", "Description", null, "Fair", null);
        Request request = new Request(counterofferer, counterofferee, offeredItem, requestedItem, SimpleCalendar.today());
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, counterofferer, counterofferee);
        float invalidRating = -1.0f;

        presenter.acceptCounteroffer(counteroffer, invalidRating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                fail("Counteroffer acceptance should have failed with invalid rating.");
            }

            @Override
            public void onFailure(String message) {
                assertNotNull(message);
                System.out.println("Counteroffer acceptance failed as expected with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptRequest_ZeroRating() {
        xChanger requester = new xChanger("requester", "requester@example.com", SimpleCalendar.today(), "password", "location");
        xChanger requestee = new xChanger("requestee", "requestee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("requester", "Offered Item", "Description", null, "New", null);
        Item requestedItem = new Item("requestee", "Requested Item", "Description", null, "Used", null);
        Request request = new Request(requester, requestee, offeredItem, requestedItem, SimpleCalendar.today());
        float zeroRating = 0.0f;

        presenter.acceptRequest(request, zeroRating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                assertTrue(xChangeId > 0);
                System.out.println("Request accepted successfully with a zero rating and xChangeId: " + xChangeId);
            }

            @Override
            public void onFailure(String message) {
                fail("Request acceptance failed unexpectedly with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptCounteroffer_MaxRating() {
        xChanger counterofferer = new xChanger("counterofferer", "counterofferer@example.com", SimpleCalendar.today(), "password", "location");
        xChanger counterofferee = new xChanger("counterofferee", "counterofferee@example.com", SimpleCalendar.today(), "password", "location");
        Item offeredItem = new Item("counterofferer", "Offered Counter Item", "Description", null, "Good", null);
        Item requestedItem = new Item("counterofferee", "Requested Counter Item", "Description", null, "Fair", null);
        Request request = new Request(counterofferer, counterofferee, offeredItem, requestedItem, SimpleCalendar.today());
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, counterofferer, counterofferee);
        float maxRating = 5.0f;

        presenter.acceptCounteroffer(counteroffer, maxRating, new AcceptRequestViewModel.AcceptRequestCallback() {
            @Override
            public void onSuccess(long xChangeId) {
                assertTrue(xChangeId > 0);
                System.out.println("Counteroffer accepted successfully with a max rating and xChangeId: " + xChangeId);
            }

            @Override
            public void onFailure(String message) {
                fail("Counteroffer acceptance failed unexpectedly with message: " + message);
            }
        });

        // Wait for background execution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
