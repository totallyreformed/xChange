package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import android.graphics.Bitmap;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    private xChanger requester;
    private xChanger requestee;
    private Item offeredItem;
    private Item requestedItem;
    private LocalDate dateInitiated;
    private Request request;

    @BeforeEach
    public void setUp() {
        requester = new xChanger("Requester", "requester@example.com", LocalDate.now(), "password", "Location");
        requestee = new xChanger("Requestee", "requestee@example.com", LocalDate.now(), "password", "Location");
        ArrayList<Image> images = new ArrayList<>();
        offeredItem = new Item("Offered Item", "Description", "Category", "Condition", images);
        requestedItem = new Item("Requested Item", "Description", "Category", "Condition", images);
        dateInitiated = LocalDate.now();
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
    public void testAddToList() {
        assertTrue(requestee.getRequests().contains(request));
    }

    @Test
    public void testMakeUnactive() {
        request.make_unactive();
        assertFalse(request.isActive());
    }

    @Test
    public void testRequesterCannotBeNull() {
        assertThrows(NullPointerException.class, () -> {
            new Request(null, requestee, offeredItem, requestedItem, dateInitiated);
        });
    }

    @Test
    public void testRequesteeCannotBeNull() {
        assertThrows(NullPointerException.class, () -> {
            new Request(requester, null, offeredItem, requestedItem, dateInitiated);
        });
    }

    @Test
    public void testOfferedItemCannotBeNull() {
        assertThrows(NullPointerException.class, () -> {
            new Request(requester, requestee, null, requestedItem, dateInitiated);
        });
    }

    @Test
    public void testRequestedItemCannotBeNull() {
        assertThrows(NullPointerException.class, () -> {
            new Request(requester, requestee, offeredItem, null, dateInitiated);
        });
    }

    @Test
    public void testDeactivateRequestMultipleTimes() {
        request.make_unactive();
        assertFalse(request.isActive());
        request.make_unactive(); // Should still be inactive
        assertFalse(request.isActive());
    }

    @Test
    public void testRequestIDIncrement() {
        Request secondRequest = new Request(requester, requestee, offeredItem, requestedItem, dateInitiated);
        assertEquals(request.getRequestID() + 1, secondRequest.getRequestID());
    }

    @Test
    public void testRequestsListSizeAfterAddingRequest() {
        int initialSize = requestee.getRequests().size();
        new Request(requester, requestee, offeredItem, requestedItem, dateInitiated);
        assertEquals(initialSize + 1, requestee.getRequests().size());
    }

    @Test
    public void testRequestActiveStatusAfterInitialization() {
        assertTrue(request.isActive());
    }
}
