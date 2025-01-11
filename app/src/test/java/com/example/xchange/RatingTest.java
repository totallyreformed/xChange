package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class RatingTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-03");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    private Request createRequest() {
        xChanger requester = createXChanger("rater");
        xChanger requestee = createXChanger("ratee");
        Item offered = new Item(requester.getUsername(), "Offered", "desc", Category.FASHION, "Good", null);
        offered.setItemId(1L);
        Item requested = new Item(requestee.getUsername(), "Requested", "desc", Category.TECHNOLOGY, "New", null);
        requested.setItemId(2L);
        Request req = new Request(requester, requestee, offered, requested, cal);
        req.setRequestId(100L);
        return req;
    }

    private xChange createXChange(Request req) {
        return new xChange(req, (Counteroffer) null, cal);
    }

    @Test
    void testSetAndGetRating() {
        xChanger rater = createXChanger("rater");
        xChanger ratee = createXChanger("ratee");
        Request req = createRequest();
        xChange xchg = createXChange(req);
        Rating rating = new Rating(3.5f, rater, ratee, req, xchg);

        assertEquals(3.5f, rating.getRating(), 0.001f);
        rating.setRating(4.0f);
        assertEquals(4.0f, rating.getRating(), 0.001f);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> rating.setRating(6.0f));
        assertEquals("Rating must be between 0 and 5.", ex.getMessage());

        String str = rating.toString();
        assertTrue(str.contains("rating=4.0"));
        assertTrue(str.contains("rater="));
        assertTrue(str.contains("ratee="));
        assertTrue(str.contains("request="));
        assertTrue(str.contains("xChange="));
    }

    @Test
    void testParcelable() {
        xChanger rater = createXChanger("rater");
        xChanger ratee = createXChanger("ratee");
        Request req = createRequest();
        xChange xchg = createXChange(req);
        Rating original = new Rating(2.5f, rater, ratee, req, xchg);

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Rating created = Rating.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        // rater and ratee are transient – expect them null after parceling
        assertNull(created.getRater());
        assertNull(created.getRatee());
        assertEquals(original.getRating(), created.getRating(), 0.001f);
        assertEquals(original.getRequest().getRequestId(), created.getRequest().getRequestId());
        assertEquals(original.getXChange().getFinalizedId(), created.getXChange().getFinalizedId());
    }
}
