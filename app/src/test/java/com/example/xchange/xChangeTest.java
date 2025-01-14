package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class xChangeTest {

    private SimpleCalendar cal;
    private xChanger requester;
    private xChanger requestee;
    private Item offeredItem;
    private Item requestedItem;
    private Request request;
    private Counteroffer counteroffer;

    @BeforeEach
    void setUp() {
        cal = new SimpleCalendar("2025-01-07");
        requester = createXChanger("reqUser");
        requestee = createXChanger("reqUser2");
        offeredItem = new Item(requester.getUsername(), "Offered", "desc", Category.FASHION, "New", null);
        offeredItem.setItemId(10L);
        requestedItem = new Item(requestee.getUsername(), "Requested", "desc", Category.BOOKS, "Used", null);
        requestedItem.setItemId(20L);
        request = new Request(requester, requestee, offeredItem, requestedItem, cal);
        request.setRequestId(100L);

        Item counterItem = new Item(request.getRequester().getUsername(), "CounterItem", "desc", Category.TECHNOLOGY, "New", null);
        counterItem.setItemId(30L);
        counteroffer = new Counteroffer(request, counterItem, request.getRequestee(), request.getRequester());
    }

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    @Test
    void testConstructorWithRequestOnly() {
        xChange xchg = new xChange(request, cal);
        assertNull(xchg.getDealStatus());
        assertEquals(request.getRequester().getUsername(), xchg.getOfferer().getUsername());
        assertEquals(request.getRequestee().getUsername(), xchg.getOfferee().getUsername());
        assertEquals(request.getOfferedItem().getItemId(), xchg.getOfferedItem().getItemId());
        assertEquals(request.getRequestedItem().getItemId(), xchg.getRequestedItem().getItemId());
        assertEquals(request.getRequestId(), xchg.getFinalizedId());
    }

    @Test
    void testConstructorWithCounteroffer() {
        xChange xchg = new xChange(request, counteroffer, cal);
        assertEquals(counteroffer.getCounterofferId(), xchg.getFinalizedId());
        assertEquals(counteroffer.getCounterofferer().getUsername(), xchg.getOfferer().getUsername());
        assertEquals(counteroffer.getCounterofferee().getUsername(), xchg.getOfferee().getUsername());
    }

    @Test
    void testAcceptOffer() {
        xChange xchg = new xChange(request, counteroffer, cal);
        xchg.getOfferee().getFinalized().clear();
        xchg.getOfferer().getFinalized().clear();
        xchg.getOfferee().setEmail("offeree@example.com");
        int origSucceedOfferer = xchg.getOfferer().getSucceedDeals();
        int origSucceedOfferee = xchg.getOfferee().getSucceedDeals();

        String returnedEmail = xchg.acceptOffer(4.5f);

        assertEquals("Accepted", xchg.getDealStatus());
        assertTrue(xchg.getOfferee().getFinalized().contains(xchg));
        assertTrue(xchg.getOfferer().getFinalized().contains(xchg));
        assertFalse(request.isActive());
        assertFalse(counteroffer.isActive());
        assertEquals(origSucceedOfferer + 1, xchg.getOfferer().getSucceedDeals());
        assertEquals(origSucceedOfferee + 1, xchg.getOfferee().getSucceedDeals());
        assertFalse(xchg.getOfferer().getRatings().isEmpty());
        assertEquals("offeree@example.com", returnedEmail);
    }

    @Test
    void testRejectOffer() {
        xChange xchg = new xChange(request, counteroffer, cal);
        xchg.getOfferee().getFinalized().clear();
        xchg.getOfferer().getFinalized().clear();
        int origFailedOfferer = xchg.getOfferer().getFailedDeals();
        int origFailedOfferee = xchg.getOfferee().getFailedDeals();

        xchg.rejectOffer(3.0f);
        assertEquals("Rejected", xchg.getDealStatus());
        assertTrue(xchg.getOfferee().getFinalized().contains(xchg));
        assertTrue(xchg.getOfferer().getFinalized().contains(xchg));
        assertFalse(request.isActive());
        assertFalse(counteroffer.isActive());
        assertEquals(origFailedOfferer + 1, xchg.getOfferer().getFailedDeals());
        assertEquals(origFailedOfferee + 1, xchg.getOfferee().getFailedDeals());
        assertFalse(xchg.getOfferer().getRatings().isEmpty());
    }
}
