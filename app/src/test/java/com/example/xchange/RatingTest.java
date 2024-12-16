package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    private xChanger rater;
    private xChanger ratee;
    private Request request;
    private xChange xChange;

    @BeforeEach
    void setUp() {
        // Mock xChanger objects
        rater = new xChanger("RaterUser", "rater@example.com", new SimpleCalendar(2024, 1, 1), "password123", "Location1");
        ratee = new xChanger("RateeUser", "ratee@example.com", new SimpleCalendar(2024, 1, 1), "password123", "Location2");

        // Mock Request and xChange objects
        request = new Request(rater, ratee, null, null, new SimpleCalendar(2024, 1, 1));
        xChange = new xChange(request, new SimpleCalendar(2024, 1, 2));
    }

    @Test
    void testConstructorAndGetters() {
        Rating rating = new Rating(4.5f, rater, ratee, request, xChange);

        assertEquals(4.5f, rating.getRating());
        assertEquals(rater, rating.getRater());
        assertEquals(ratee, rating.getRatee());
        assertEquals(request, rating.getRequest());
        assertEquals(xChange, rating.getXChange());
    }

    @Test
    void testSetRatingValid() {
        Rating rating = new Rating(3.0f, rater, ratee, request, xChange);
        rating.setRating(4.0f);

        assertEquals(4.0f, rating.getRating());
    }

    @Test
    void testSetRatingInvalid() {
        Rating rating = new Rating(3.0f, rater, ratee, request, xChange);

        assertThrows(IllegalArgumentException.class, () -> rating.setRating(-1.0f));
        assertThrows(IllegalArgumentException.class, () -> rating.setRating(6.0f));
    }

    @Test
    void testSetRequestAndXChange() {
        Rating rating = new Rating(3.5f, rater, ratee, request, xChange);

        Request newRequest = new Request(rater, ratee, null, null, new SimpleCalendar(2024, 1, 3));
        xChange newXChange = new xChange(newRequest, new SimpleCalendar(2024, 1, 4));

        rating.setRequest(newRequest);
        rating.setXChange(newXChange);

        assertEquals(newRequest, rating.getRequest());
        assertEquals(newXChange, rating.getXChange());
    }

    @Test
    void testToString() {
        Rating rating = new Rating(4.0f, rater, ratee, request, xChange);
        String expected = "Rating{rating=4.0, rater=" + rater + ", ratee=" + ratee + ", request=" + request + ", xChange=" + xChange + "}";

        assertEquals(expected, rating.toString());
    }
}
