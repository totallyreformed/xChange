package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-02");
    private xChanger requester;
    private xChanger requestee;
    private Item offeredItem;
    private Item requestedItem;
    private Request request;

    @BeforeEach
    void setUp() {
        requester = new xChanger("requester", "requester@example.com", cal, "pass", "Loc1");
        requestee = new xChanger("requestee", "requestee@example.com", cal, "pass", "Loc2");
        offeredItem = new Item(requester.getUsername(), "Offered", "desc", Category.BOOKS, "New", null);
        offeredItem.setItemId(1L);
        requestedItem = new Item(requestee.getUsername(), "Requested", "desc", Category.FASHION, "New", null);
        requestedItem.setItemId(2L);
        request = new Request(requester, requestee, offeredItem, requestedItem, cal);
        request.setRequestId(50L);
    }

    @Test
    void testGettersSettersAndStatus() {
        assertEquals(50L, request.getRequestId());
        assertEquals("Active", request.getStatus());

        request.make_unactive();
        assertFalse(request.isActive());
        assertEquals("Inactive", request.getStatus());

        Item newRequested = new Item(requestee.getUsername(), "NewRequested", "desc", Category.HOME, "New", null);
        newRequested.setItemId(3L);
        request.setRequestedItem(newRequested);
        assertEquals(newRequested, request.getRequestedItem());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Request req2 = new Request(requester, requestee, offeredItem, requestedItem, cal);
        req2.setRequestId(50L);

        assertEquals(request, req2);
        assertEquals(request.hashCode(), req2.hashCode());

        String str = request.toString();
        assertTrue(str.contains("50"));
        assertTrue(str.contains(request.getRequester().getUsername()));
        assertTrue(str.contains(request.getRequestee().getUsername()));
    }
}
