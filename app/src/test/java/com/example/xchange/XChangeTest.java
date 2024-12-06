package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class XChangeTest {
    private xChanger offerer;
    private xChanger offeree;
    private Item offeredItem;
    private Item requestedItem;
    private Request request;
    private xChange exchange;

    @BeforeEach
    public void setUp() {
        // Set up test objects
        offerer = new xChanger("offerer", "offerer@example.com", new SimpleCalendar(2024, 12, 1), "password123", "Location1");
        offeree = new xChanger("offeree", "offeree@example.com", new SimpleCalendar(2024, 12, 1), "password456", "Location2");

        offeredItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", new ArrayList<>());
        requestedItem = new Item("Phone", "Smartphone", "Electronics", "Used", new ArrayList<>());

        offerer.getItems().add(offeredItem);
        offeree.getItems().add(requestedItem);

        request = new Request(offerer, offeree, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        exchange = new xChange(request, new SimpleCalendar(2024, 12, 2));
    }

    @Test
    public void testAcceptOffer() {
        // Accept the offer
        String offereeEmail = exchange.acceptOffer();

        // Assertions
        assertEquals("Accepted", exchange.getStatus());
        assertFalse(offerer.getItems().contains(requestedItem)); // Item removed from offerer's list
        assertFalse(offeree.getItems().contains(offeredItem)); // Item removed from offeree's list
        assertTrue(offeree.getFinalized().contains(exchange)); // Exchange added to offeree's finalized list
        assertTrue(offerer.getFinalized().contains(exchange)); // Exchange added to offerer's finalized list
        assertEquals("offeree@example.com", offereeEmail);
        assertEquals(1, offerer.getFinalized().size());
        assertEquals(1, offeree.getFinalized().size());
    }

    @Test
    public void testRejectOffer() {
        // Reject the offer
        exchange.rejectOffer();

        // Assertions
        assertEquals("Rejected", exchange.getStatus());
        assertTrue(offeree.getFinalized().contains(exchange)); // Exchange added to offeree's finalized list
        assertTrue(offerer.getFinalized().contains(exchange)); // Exchange added to offerer's finalized list
        assertEquals(1, offeree.getFinalized().size());
        assertEquals(1, offerer.getFinalized().size());
    }

    @Test
    public void testInitializationWithNullRequest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new xChange(null, new SimpleCalendar(2024, 12, 1));
        });

        assertEquals("Request and date_finalized cannot be null.", exception.getMessage());
    }

    @Test
    public void testInitializationWithNullDateFinalized() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new xChange(request, null);
        });

        assertEquals("Request and date_finalized cannot be null.", exception.getMessage());
    }

    @Test
    public void testInitializationWithCounteroffer() {
        Counteroffer counteroffer = new Counteroffer(request, "Counteroffer message", offeredItem);
        xChange exchangeWithCounteroffer = new xChange(request, counteroffer, new SimpleCalendar(2024, 12, 2));

        // Assertions
        assertNotNull(exchangeWithCounteroffer.getCounterOffer());
        assertEquals(counteroffer, exchangeWithCounteroffer.getCounterOffer());
        assertEquals("Counteroffer message", counteroffer.getMessage());
    }

    @Test
    public void testSetDealStatus() {
        exchange.setDealStatus("Completed");
        assertEquals("Completed", exchange.getStatus());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> exchange.setDealStatus(null));
        assertEquals("Deal status cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> exchange.setDealStatus(""));
        assertEquals("Deal status cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testGetFinalizedID() {
        // Assertions
        assertEquals(request.getRequestID(), exchange.getFinalizedID());
    }

    @Test
    public void testGetDateFinalized() {
        SimpleCalendar dateFinalized = exchange.getDateFinalized();

        // Assertions
        assertNotNull(dateFinalized);
        assertEquals(2024, dateFinalized.getYear());
        assertEquals(12, dateFinalized.getMonth());
        assertEquals(2, dateFinalized.getDay());
    }
}
