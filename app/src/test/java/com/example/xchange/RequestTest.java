package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
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
}