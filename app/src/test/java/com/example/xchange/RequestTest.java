package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class RequestTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-02");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    private Item createItem(Long id, String name, Category cat) {
        Item item = new Item(createXChanger("owner").getUsername(), name, "desc", cat, "New", null);
        item.setItemId(id);
        return item;
    }

    private Request createRequest() {
        xChanger requester = createXChanger("requester");
        xChanger requestee = createXChanger("requestee");
        Item offered = createItem(1L, "Offered", Category.BOOKS);
        Item requested = createItem(2L, "Requested", Category.FASHION);
        Request req = new Request(requester, requestee, offered, requested, cal);
        req.setRequestId(50L);
        return req;
    }

    @Test
    void testGettersSettersAndStatus() {
        Request req = createRequest();

        assertEquals(50L, req.getRequestId());
        assertEquals("Active", req.getStatus());
        req.make_unactive();
        assertFalse(req.isActive());
        assertEquals("Inactive", req.getStatus());

        Item newRequested = createItem(3L, "NewRequested", Category.HOME);
        req.setRequestedItem(newRequested);
        assertEquals(newRequested, req.getRequestedItem());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Request req1 = createRequest();
        Request req2 = createRequest();
        req1.setRequestId(100L);
        req2.setRequestId(100L);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());

        String str = req1.toString();
        assertTrue(str.contains("100"));
        assertTrue(str.contains(req1.getRequester().getUsername()));
        assertTrue(str.contains(req1.getRequestee().getUsername()));
    }

    @Test
    void testParcelable() {
        Request original = createRequest();
        original.setActive(true);

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Request created = Request.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getRequestId(), created.getRequestId());
        assertEquals(original.getRequester().getUsername(), created.getRequester().getUsername());
        assertEquals(original.getRequestee().getUsername(), created.getRequestee().getUsername());
        assertEquals(original.getOfferedItem().getItemId(), created.getOfferedItem().getItemId());
        assertEquals(original.getRequestedItem().getItemId(), created.getRequestedItem().getItemId());
        assertEquals(original.getDateInitiated().toString(), created.getDateInitiated().toString());
        assertEquals(original.isActive(), created.isActive());
    }
}
