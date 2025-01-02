package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    private xChanger requester;
    private xChanger requestee;
    private Item offeredItem;
    private Item requestedItem;
    private SimpleCalendar dateInitiated;
    private Request request;
    private Category category = Category.fromDisplayName("Fashion");

    @BeforeEach
    public void setUp() {
        requester = new xChanger("Requester", "requester@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location");
        requestee = new xChanger("Requestee", "requestee@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location");
        ArrayList<Image> images = new ArrayList<>();
        offeredItem = new Item(requester.toString(), "Offered Item", "Description", category, "Condition", images);
        requestedItem = new Item(requester.toString(), "Requested Item", "Description", category, "Condition", images);
        dateInitiated = new SimpleCalendar(2024, 12, 1);
        request = new Request(requester, requestee, offeredItem, requestedItem, dateInitiated);
    }

    @Test
    public void testRequestInitialization() {
        assertEquals(requester, request.getRequester());
        assertEquals(requestee, request.getRequestee());
        assertEquals(offeredItem, request.getOfferedItem());
        assertEquals(requestedItem, request.getRequestedItem());
        assertEquals(dateInitiated, request.getDateInitiated());
        assertTrue(request.isActive());
    }

    @Test
    public void testMakeUnactive() {
        request.make_unactive();
        assertFalse(request.isActive());
    }

    @Test
    public void testNullRequester() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Request(null, requestee, offeredItem, requestedItem, dateInitiated);
        });

        assertEquals("Requester cannot be null.", exception.getMessage());
    }

    @Test
    public void testNullRequestee() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Request(requester, null, offeredItem, requestedItem, dateInitiated);
        });

        assertEquals("Requestee cannot be null.", exception.getMessage());
    }

    @Test
    public void testFutureDateInitiation() {
        SimpleCalendar futureDate = new SimpleCalendar(2024, 12, 4);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Request(requester, requestee, offeredItem, requestedItem, futureDate);
        });

        String expectedMessage = "Date cannot be in the future.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testRemoveRequestFromUsers() {
        requester.getRequests().remove(request);
        requestee.getRequests().remove(request);

        assertFalse(requester.getRequests().contains(request));
        assertFalse(requestee.getRequests().contains(request));
    }
}
