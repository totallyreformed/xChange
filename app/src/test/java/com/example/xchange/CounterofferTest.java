package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterofferTest {

    private SimpleCalendar cal;
    private xChanger counterofferer;
    private xChanger counterofferee;
    private Request request;
    private Item offeredItem;

    @BeforeEach
    void setUp() {
        cal = new SimpleCalendar("2025-01-04");

        counterofferer = new xChanger("counter1", "counter1@example.com", cal, "pass", "Loc");
        counterofferee = new xChanger("counter2", "counter2@example.com", cal, "pass", "Loc");

        xChanger requester = new xChanger("requester", "requester@example.com", cal, "pass", "Loc");
        xChanger requestee = new xChanger("requestee", "requestee@example.com", cal, "pass", "Loc");

        Item offered = new Item("requester", "Offered", "desc", Category.FASHION, "Good", null);
        offered.setItemId(10L);
        Item requested = new Item("requestee", "Requested", "desc", Category.BOOKS, "Good", null);
        requested.setItemId(20L);

        request = new Request(requester, requestee, offered, requested, cal);
        request.setRequestId(50L);

        offeredItem = new Item("counter1", "OfferedItem", "desc", Category.TECHNOLOGY, "Good", null);
        offeredItem.setItemId(30L);
    }

    @Test
    void testConstructorAndSetters() {
        Counteroffer co = new Counteroffer(request, offeredItem, counterofferer, counterofferee);
        assertEquals(request.getRequestedItem(), co.getRequestedItem());
        assertEquals(request, co.getRequest());
        assertEquals(offeredItem, co.getOfferedItem());
        assertEquals(counterofferer, co.getCounterofferer());
        assertEquals(counterofferee, co.getCounterofferee());
        assertTrue(co.isActive());

        co.setCounterofferId(100L);
        assertEquals(100L, co.getCounterofferId());
        co.setActive(false);
        assertFalse(co.isActive());
    }

    @Test
    void testConstructorNullParameters() {
        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> new Counteroffer(null, offeredItem, counterofferer, counterofferee));
        assertEquals("Request cannot be null.", ex1.getMessage());

        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> new Counteroffer(request, null, counterofferer, counterofferee));
        assertEquals("Offered item cannot be null.", ex2.getMessage());
    }

    @Test
    void testToString() {
        Counteroffer co = new Counteroffer(request, offeredItem, counterofferer, counterofferee);
        co.setCounterofferId(200L);
        String str = co.toString();
        assertTrue(str.contains("counterofferId=200"));
        assertTrue(str.contains("requestId=50"));
        assertTrue(str.contains("offeredItem=30"));
    }
}
