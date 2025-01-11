package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
// import mockito
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class CounterofferTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-04");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    private Item createItem(Long id, String name, Category cat) {
        Item item = new Item(createXChanger("owner").getUsername(), name, "desc", cat, "Good", null);
        item.setItemId(id);
        return item;
    }

    private Request createRequest() {
        xChanger requester = createXChanger("requester");
        xChanger requestee = createXChanger("requestee");
        Item offered = createItem(10L, "Offered", Category.FASHION);
        Item requested = createItem(20L, "Requested", Category.BOOKS);
        Request req = new Request(requester, requestee, offered, requested, cal);
        req.setRequestId(50L);
        return req;
    }

    @Test
    void testConstructorAndSetters() {
        Request req = createRequest();
        Item offered = createItem(30L, "Offered2", Category.TECHNOLOGY);
        xChanger counterofferer = createXChanger("counter1");
        xChanger counterofferee = createXChanger("counter2");

        Counteroffer co = new Counteroffer(req, offered, counterofferer, counterofferee);
        assertEquals(req.getRequestedItem(), co.getRequestedItem());
        assertEquals(req, co.getRequest());
        assertEquals(offered, co.getOfferedItem());
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
        xChanger x1 = createXChanger("x1");
        xChanger x2 = createXChanger("x2");
        Item offered = createItem(1L, "Any", Category.HOME);
        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> new Counteroffer(null, offered, x1, x2));
        assertEquals("Request cannot be null.", ex1.getMessage());
        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> new Counteroffer(createRequest(), null, x1, x2));
        assertEquals("Offered item cannot be null.", ex2.getMessage());
    }

    @Test
    void testToString() {
        Counteroffer co = new Counteroffer(createRequest(), createItem(40L, "Offered", Category.SPORTS),
                createXChanger("offerer"), createXChanger("offeree"));
        co.setCounterofferId(200L);
        String str = co.toString();
        assertTrue(str.contains("counterofferId=200"));
        assertTrue(str.contains("requestId=50"));
        assertTrue(str.contains("offeredItem=40"));
    }

    @Test
    void testParcelable() {
        Counteroffer original = new Counteroffer(createRequest(), createItem(55L, "Offered", Category.TOYS),
                createXChanger("counter1"), createXChanger("counter2"));
        original.setCounterofferId(300L);
        original.setActive(true);

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Counteroffer created = Counteroffer.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getCounterofferId(), created.getCounterofferId());
        assertEquals(original.getRequest().getRequestId(), created.getRequest().getRequestId());
        assertEquals(original.getOfferedItem().getItemId(), created.getOfferedItem().getItemId());
        assertEquals(original.getRequestedItem().getItemId(), created.getRequestedItem().getItemId());
        assertEquals(original.getCounterofferer().getUsername(), created.getCounterofferer().getUsername());
        assertEquals(original.getCounterofferee().getUsername(), created.getCounterofferee().getUsername());
        assertEquals(original.isActive(), created.isActive());
    }
}
