package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class XChangerTest {

    private xChanger testXChanger;
    private xChanger otherXChanger;

    @BeforeEach
    public void setUp() {
        testXChanger = new xChanger("testXChanger", "test@example.com", new SimpleCalendar(2024, 12, 1), "password123", "TestLocation");
        otherXChanger = new xChanger("otherXChanger", "other@example.com", new SimpleCalendar(2024, 12, 1), "password123", "OtherLocation");

    }

    @Test
    public void testLoginSuccess() {

        assertTrue(testXChanger.login("testXChanger", "password123"));
        assertTrue(testXChanger.login("otherXChanger", "password123"));
    }

    @Test
    public void testLoginFailure() {
        assertFalse(testXChanger.login("wrongUsername", "password123"));
        assertFalse(testXChanger.login("testXChanger", "wrongPassword"));
    }

    @Test
    public void testRegisterSuccess() {
        xChanger newXChanger = new xChanger("newXChanger", "new@example.com", new SimpleCalendar(2024, 12, 2), "newPassword", "NewLocation");
        assertTrue(newXChanger.login(newXChanger.getUsername(),newXChanger.getPassword()));
        assertEquals(3, xChanger.getxChangers().size());
    }

    @Test
    public void testRegisterFailure_DuplicateUser() {
        xChanger duplicateUsername = new xChanger("testXChanger", "new@example.com", new SimpleCalendar(2024, 12, 2), "newPassword", "NewLocation");
        xChanger duplicateEmail = new xChanger("newXChanger", "test@example.com", new SimpleCalendar(2024, 12, 2), "newPassword", "NewLocation");

        assertFalse(testXChanger.register(duplicateUsername));
        assertFalse(testXChanger.register(duplicateEmail));
    }

    @Test
    public void testRating() {
        testXChanger.setRating(4.5f);
        assertEquals(4.5f, testXChanger.getRating());

        testXChanger.setRating(3.5f);
        assertEquals(4.0f, testXChanger.getRating()); // Average of 4.5 and 3.5
    }

    @Test
    public void testUploadItem() {
        ArrayList<Image> images = new ArrayList<>();
        testXChanger.UploadItem("Laptop", "Gaming laptop", "Electronics", "New", images);

        assertEquals(1, testXChanger.getItems().size());
        assertEquals("Laptop", testXChanger.getItems().get(0).getItemName());
    }

    @Test
    public void testDeleteItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item item = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);
        testXChanger.getItems().add(item);

        testXChanger.deleteItem(item);
        assertTrue(testXChanger.getItems().isEmpty());
    }

    @Test
    public void testRequestItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item("Phone", "Smartphone", "Electronics", "Used", images);
        Item requestedItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);

        testXChanger.RequestItem(otherXChanger, offeredItem, requestedItem);
        assertEquals(1, otherXChanger.getRequests().size());
        assertEquals(1, testXChanger.getRequests().size());
    }

    @Test
    public void testAcceptRequest() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item("Phone", "Smartphone", "Electronics", "Used", images);
        Item requestedItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        testXChanger.acceptRequest(request);

        assertEquals(1, testXChanger.getSucceed_Deals());
    }

    @Test
    public void testRejectRequest() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item("Phone", "Smartphone", "Electronics", "Used", images);
        Item requestedItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        testXChanger.rejectRequest(request);

        assertEquals(1, testXChanger.getFailed_Deals());
    }

    @Test
    public void testCounterOffer() {
        ArrayList<Image> images = new ArrayList<>();
        Item item = new Item("Tablet", "Android tablet", "Electronics", "New", images);

        // Ensure initial state
        System.out.println("CounterOffers before: " + testXChanger.getCounterOffers().size());

        Request request = new Request(testXChanger, otherXChanger, item, item, new SimpleCalendar(2024, 12, 1));
        testXChanger.counterOffer(item, "Counteroffer message", request);
        assertEquals(1, otherXChanger.getCounterOffers().size());
        assertEquals(1, testXChanger.getCounterOffers().size());
    }

    @Test
    public void testReport() {
        xChange finalizedDeal = new xChange(new Request(testXChanger, otherXChanger, null, null, new SimpleCalendar(2024, 12, 1)), new SimpleCalendar(2024, 12, 2));
        testXChanger.report(otherXChanger, "Inappropriate behavior", finalizedDeal);

        assertEquals(1, testXChanger.getReports().size());
    }
    @Test
    public void testAcceptCounteroffer() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item("Phone", "Smartphone", "Electronics", "Used", images);
        Item requestedItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);
        Item newrequestedItem=new Item("Shoes","Shoes","Fashion","Used",images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        Counteroffer counteroffer = new Counteroffer(request, "Let's exchange", newrequestedItem);

        String email = testXChanger.acceptCounteroffer(counteroffer);
        assertFalse(counteroffer.isActive());
        assertFalse(request.isActive());

        assertNotNull(email);
        assertEquals(1, testXChanger.getSucceed_Deals());
    }
    @Test
    public void testRejectCounteroffer() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item("Phone", "Smartphone", "Electronics", "Used", images);
        Item requestedItem = new Item("Laptop", "Gaming laptop", "Electronics", "New", images);
        Item newrequestedItem=new Item("Shoes","Shoes","Fashion","Used",images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        Counteroffer counteroffer = new Counteroffer(request, "Let's exchange", newrequestedItem);

        testXChanger.rejectCounteroffer(counteroffer);
        assertFalse(counteroffer.isActive());
        assertFalse(request.isActive());
        assertEquals(1, testXChanger.getFailed_Deals());
    }
    @Test
    public void testGetItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item item1 = new Item("Tablet", "Android tablet", "Electronics", "New", images);
        Item item2 = new Item("Phone", "Smartphone", "Electronics", "Used", images);

        testXChanger.getItems().add(item1);
        testXChanger.getItems().add(item2);

        Item fetchedItem = testXChanger.getItem(item1);

        assertNotNull(fetchedItem);
        assertEquals("Tablet", fetchedItem.getItemName());
    }


}
