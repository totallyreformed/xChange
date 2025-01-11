package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class xChangeTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-07");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    private Request createRequest() {
        xChanger requester = createXChanger("reqUser");
        xChanger requestee = createXChanger("reqUser2");
        Item offered = new Item(requester.getUsername(), "Offered", "desc", Category.FASHION, "New", null);
        offered.setItemId(10L);
        Item requested = new Item(requestee.getUsername(), "Requested", "desc", Category.BOOKS, "Used", null);
        requested.setItemId(20L);
        Request req = new Request(requester, requestee, offered, requested, cal);
        req.setRequestId(100L);
        return req;
    }

    private Counteroffer createCounteroffer() {
        Request req = createRequest();
        Item offered = new Item(req.getRequester().getUsername(), "CounterItem", "desc", Category.TECHNOLOGY, "New", null);
        offered.setItemId(30L);
        return new Counteroffer(req, offered, req.getRequestee(), req.getRequester());
    }

    @Test
    void testConstructorWithRequestOnly() {
        Request req = createRequest();
        xChange xchg = new xChange(req, cal);
        assertNull(xchg.getDealStatus());
        assertEquals(req.getRequester().getUsername(), xchg.getOfferer().getUsername());
        assertEquals(req.getRequestee().getUsername(), xchg.getOfferee().getUsername());
        assertEquals(req.getOfferedItem().getItemId(), xchg.getOfferedItem().getItemId());
        assertEquals(req.getRequestedItem().getItemId(), xchg.getRequestedItem().getItemId());
        assertEquals(req.getRequestId(), xchg.getFinalizedId());
    }

    @Test
    void testConstructorWithCounteroffer() {
        Request req = createRequest();
        Counteroffer co = createCounteroffer();
        xChange xchg = new xChange(req, co, cal);
        assertEquals(co.getCounterofferId(), xchg.getFinalizedId());
        assertEquals(co.getCounterofferer().getUsername(), xchg.getOfferer().getUsername());
        assertEquals(co.getCounterofferee().getUsername(), xchg.getOfferee().getUsername());
    }

    @Test
    void testAcceptOffer() {
        Request req = createRequest();
        Counteroffer co = createCounteroffer();
        xChange xchg = new xChange(req, co, cal);
        xchg.getOfferee().getFinalized().clear();
        xchg.getOfferer().getFinalized().clear();
        xchg.getOfferee().setEmail("offeree@example.com");
        int origSucceedOfferer = xchg.getOfferer().getSucceedDeals();
        int origSucceedOfferee = xchg.getOfferee().getSucceedDeals();

        String returnedEmail = xchg.acceptOffer(4.5f);

        assertEquals("Accepted", xchg.getDealStatus());
        assertTrue(xchg.getOfferee().getFinalized().contains(xchg));
        assertTrue(xchg.getOfferer().getFinalized().contains(xchg));
        assertFalse(req.isActive());
        assertFalse(co.isActive());
        assertEquals(origSucceedOfferer + 1, xchg.getOfferer().getSucceedDeals());
        assertEquals(origSucceedOfferee + 1, xchg.getOfferee().getSucceedDeals());
        assertFalse(xchg.getOfferee().getRatings().isEmpty());
        assertEquals("offeree@example.com", returnedEmail);
    }

    @Test
    void testRejectOffer() {
        Request req = createRequest();
        Counteroffer co = createCounteroffer();
        xChange xchg = new xChange(req, co, cal);
        xchg.getOfferee().getFinalized().clear();
        xchg.getOfferer().getFinalized().clear();
        int origFailedOfferer = xchg.getOfferer().getFailedDeals();
        int origFailedOfferee = xchg.getOfferee().getFailedDeals();

        xchg.rejectOffer(3.0f);
        assertEquals("Rejected", xchg.getDealStatus());
        assertTrue(xchg.getOfferee().getFinalized().contains(xchg));
        assertTrue(xchg.getOfferer().getFinalized().contains(xchg));
        assertFalse(req.isActive());
        assertFalse(co.isActive());
        assertEquals(origFailedOfferer + 1, xchg.getOfferer().getFailedDeals());
        assertEquals(origFailedOfferee + 1, xchg.getOfferee().getFailedDeals());
        assertFalse(xchg.getOfferee().getRatings().isEmpty());
    }

    @Test
    void testParcelable() {
        Request req = createRequest();
        Counteroffer co = createCounteroffer();
        xChange original = new xChange(req, co, cal);
        original.setXChangeId(999L);
        original.setDealStatus("InProgress");

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        xChange created = xChange.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getXChangeId(), created.getXChangeId());
        assertEquals(original.getDealStatus(), created.getDealStatus());
        assertEquals(original.getFinalizedId(), created.getFinalizedId());
        assertEquals(original.getRequest().getRequestId(), created.getRequest().getRequestId());
        if (original.getCounteroffer() != null) {
            assertEquals(original.getCounteroffer().getCounterofferId(), created.getCounteroffer().getCounterofferId());
        }
    }
}
