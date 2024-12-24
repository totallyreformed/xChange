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
        Request.resetId();
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
    public void testGetRequestID() {
        long requestId = request.getRequestID();
        assertEquals(1L, requestId);
    }

    @Test
    public void testNewRequestWithNewUser2() {
        xChanger newUser2 = new xChanger("NewUser2", "newuser2@example.com", new SimpleCalendar(2024, 12, 2), "password2", "Location2");

        Request newRequest = new Request(
                newUser2,
                requestee,
                offeredItem,
                requestedItem,
                new SimpleCalendar(2024, 12, 2)
        );

        assertEquals(newUser2, newRequest.getRequester());
        assertEquals(requestee, newRequest.getRequestee());
        assertEquals(offeredItem, newRequest.getOfferedItem());
        assertEquals(requestedItem, newRequest.getRequestedItem());
        assertTrue(newRequest.isActive());

        long newRequestId = newRequest.getRequestID();
        assertEquals(2L, newRequestId); // The ID should increment from 1 to 2
    }

    @Test
    public void testAddToList() {
        assertTrue(requestee.getRequests().contains(request));
    }

    @Test
    public void testMakeUnactive() {
        request.make_unactive();
        assertFalse(request.isActive());
    }

    @Test
    public void testAddToRequesteeList() {
        assertTrue(requestee.getRequests().contains(request)); // Request should be in the requestee's list
    }

    @Test
    public void testAddToRequesterList() {
        assertTrue(requester.getRequests().contains(request)); // Request should be in the requester's list
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
