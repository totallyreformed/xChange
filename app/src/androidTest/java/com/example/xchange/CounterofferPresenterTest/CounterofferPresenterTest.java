package com.example.xchange.CounterofferPresenterTest;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.example.xchange.CounterOffer.CounterofferPresenter;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CounterofferPresenterTest {

    private CounterofferPresenter presenter;
    private UserRepository userRepository;
    private Context context;

    private xChanger counterofferer;
    private xChanger counterofferee;
    private Item offeredItem;
    private Item requestedItem;
    private Request request;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        userRepository = new UserRepository(context);
        presenter = new CounterofferPresenter(null, null, null, null, null, null, context);

        counterofferer = new xChanger("counterofferer", "counterofferer@example.com", SimpleCalendar.today(), "password", "location");
        counterofferee = new xChanger("counterofferee", "counterofferee@example.com", SimpleCalendar.today(), "password", "location");
        offeredItem = new Item("counterofferer", "Offered Item", "Description", null, "New", null);
        requestedItem = new Item("counterofferee", "Requested Item", "Description", null, "Used", null);
        request = new Request(counterofferer, counterofferee, offeredItem, requestedItem, SimpleCalendar.today());
    }

    @After
    public void tearDown() {
        presenter = null;
        userRepository.shutdownExecutor();
    }

    @Test
    public void testCreateCounteroffer_ValidData() {
        presenter.createCounterOffer(request, offeredItem, counterofferer);

        assertTrue(counterofferer.getCounterOffers().stream().anyMatch(co -> co.getRequest().equals(request)));
    }

    @Test
    public void testCreateCounteroffer_InvalidRequest() {
        try {
            presenter.createCounterOffer(null, offeredItem, counterofferer);
            fail("Should have thrown IllegalArgumentException for null request.");
        } catch (IllegalArgumentException e) {
            // Pass: Exception expected
        }
    }

    @Test
    public void testCreateCounteroffer_InvalidItem() {
        try {
            presenter.createCounterOffer(request, null, counterofferer);
            fail("Should have thrown IllegalArgumentException for null item.");
        } catch (IllegalArgumentException e) {
            // Pass: Exception expected
        }
    }

    @Test
    public void testCreateCounteroffer_DuplicateCounteroffer() {
        presenter.createCounterOffer(request, offeredItem, counterofferer);

        try {
            presenter.createCounterOffer(request, offeredItem, counterofferer);
            fail("Should have thrown IllegalArgumentException for duplicate counteroffer.");
        } catch (IllegalArgumentException e) {
            // Pass: Exception expected
        }
    }

    @Test
    public void testCreateCounteroffer_ValidatesLiveData() {
        presenter.createCounterOffer(request, offeredItem, counterofferer);

        // Ensure no exception is thrown, and counteroffer is added
        assertTrue(counterofferer.getCounterOffers().stream().anyMatch(co -> co.getRequest().equals(request)));
    }

    @Test
    public void testCreateCounteroffer_InvalidUser() {
        try {
            presenter.createCounterOffer(request, offeredItem, null);
            fail("Should have thrown IllegalArgumentException for null xChanger.");
        } catch (IllegalArgumentException e) {
            // Pass: Exception expected
        }
    }
}
