package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CounterOfferTest {

    private xChanger requester;
    private xChanger requestee;
    private Item offeredItem;
    private Item requestedItem;
    private Request request;
    private Counteroffer counteroffer;
    private Category category = Category.fromDisplayName("Fashion");

    @BeforeEach
    public void setUp() {
        requester = new xChanger("Requester", "requester@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location");
        requestee = new xChanger("Requestee", "requestee@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location");
        ArrayList<Image> images = new ArrayList<>();
        offeredItem = new Item(requester.toString(), "Offered Item", "Description", category, "Condition", images);
        requestedItem = new Item(requester.toString(), "Requested Item", "Description", category, "Condition", images);

        // Initialize a Request object
        request = new Request(requester, requestee, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));

        // Initialize a Counteroffer object
        counteroffer = new Counteroffer(request, "Counteroffer message", offeredItem);
    }

    @Test
    public void testCounterofferInitialization() {
        assertNotNull(counteroffer);
        assertEquals(request, counteroffer.getRequest());
        assertEquals(offeredItem, counteroffer.getOfferedItem());
        assertEquals(requestedItem, counteroffer.getRequestedItem());
        assertEquals(requester, counteroffer.getCounterofferer());
        assertEquals(requestee, counteroffer.getCounterofferee());
        assertEquals("Counteroffer message", counteroffer.getMessage());
        assertTrue(counteroffer.isActive());
    }

    @Test
    public void testSetOfferedItem() {
        Item newOfferedItem = new Item(requester.toString(), "New Offered Item", "New Description", category, "Condition", new ArrayList<>());
        counteroffer.setOfferedItem(newOfferedItem);
        assertEquals(newOfferedItem, counteroffer.getOfferedItem());
    }

    @Test
    public void testSetRequestedItem() {
        Item newRequestedItem = new Item(requester.toString(), "New Requested Item", "New Description", category, "Condition", new ArrayList<>());
        counteroffer.setRequestedItem(newRequestedItem);
        assertEquals(newRequestedItem, counteroffer.getRequestedItem());
    }

    @Test
    public void testSetMessage() {
        counteroffer.setMessage("Updated message");
        assertEquals("Updated message", counteroffer.getMessage());
    }

    @Test
    public void testSetMessageWithNullOrEmpty() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> counteroffer.setMessage(null));
        assertEquals("Message cannot be null or empty.", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> counteroffer.setMessage(""));
        assertEquals("Message cannot be null or empty.", exception2.getMessage());
    }

    @Test
    public void testMakeUnactive() {
        counteroffer.make_unactive();
        assertFalse(counteroffer.isActive());
    }

    @Test
    public void testAddToRequestee() {
        assertTrue(requestee.getCounterOffers().contains(counteroffer));
    }

    @Test
    public void testAddToRequester() {
        assertTrue(requester.getCounterOffers().contains(counteroffer));
    }

    @Test
    public void testCounterofferWithNullRequest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Counteroffer(null, "Message", offeredItem);
        });

        assertEquals("Request cannot be null.", exception.getMessage());
    }

    @Test
    public void testCounterofferWithNullItem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Counteroffer(request, "Message", null);
        });

        assertEquals("Offered item cannot be null.", exception.getMessage());
    }

    @Test
    public void testDuplicateCounteroffer() {
        Counteroffer duplicateCounteroffer = new Counteroffer(request, "Another message", offeredItem);
        assertNotEquals(counteroffer, duplicateCounteroffer);
    }

    @Test
    public void testUpdateBothItems() {
        Item newOfferedItem = new Item(requester.toString(), "Updated Offered Item", "Description", category, "Condition", new ArrayList<>());
        Item newRequestedItem = new Item(requester.toString(),"Updated Requested Item", "Description", category, "Condition", new ArrayList<>());

        counteroffer.setOfferedItem(newOfferedItem);
        counteroffer.setRequestedItem(newRequestedItem);

        assertEquals(newOfferedItem, counteroffer.getOfferedItem());
        assertEquals(newRequestedItem, counteroffer.getRequestedItem());
    }
}
