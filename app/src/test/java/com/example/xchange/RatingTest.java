package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class RatingTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-03");
    private xChanger rater;
    private xChanger ratee;
    private Request request;
    private xChange xChange;
    private Rating rating;

    @BeforeEach
    void setUp() {
        rater = new xChanger("rater", "rater@example.com", cal, "password", "Location1");
        ratee = new xChanger("ratee", "ratee@example.com", cal, "password", "Location2");

        Item offered = new Item(rater.getUsername(), "Offered", "desc", Category.FASHION, "Good", null);
        offered.setItemId(1L);
        Item requested = new Item(ratee.getUsername(), "Requested", "desc", Category.TECHNOLOGY, "New", null);
        requested.setItemId(2L);

        request = new Request(rater, ratee, offered, requested, cal);
        request.setRequestId(100L);

        xChange = new xChange(request, null, cal);

        rating = new Rating(3.5f, rater, ratee, request, xChange);
    }

    @Test
    void testSetAndGetRating() {
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
        Parcel parcel = Parcel.obtain();
        rating.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Rating created = Rating.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        // rater and ratee are transient – expect them null after parceling
        assertNull(created.getRater());
        assertNull(created.getRatee());
        assertEquals(rating.getRating(), created.getRating(), 0.001f);
        assertEquals(rating.getRequest().getRequestId(), created.getRequest().getRequestId());
        assertEquals(rating.getXChange().getFinalizedId(), created.getXChange().getFinalizedId());
    }
}
