package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void testUploadItem() {
        ArrayList<Image> images = new ArrayList<>();
        testXChanger.UploadItem("Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);

        assertEquals(1, testXChanger.getItems().size());
        assertEquals("Laptop", testXChanger.getItems().get(0).getItemName());
    }

    @Test
    public void testDeleteItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item item = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);
        testXChanger.getItems().add(item);

        testXChanger.deleteItem(item);
        assertTrue(testXChanger.getItems().isEmpty());
    }

    @Test
    public void testRequestItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);
        Item requestedItem = new Item(otherXChanger.toString(), "Phone", "Smartphone", Category.TECHNOLOGY, "Used", images);

        testXChanger.RequestItem(otherXChanger, offeredItem, requestedItem);
        assertEquals(1, otherXChanger.getRequests().size());
        assertEquals(1, testXChanger.getRequests().size());
    }

    @Test
    public void testAcceptRequestWithRating() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);
        Item requestedItem = new Item(otherXChanger.toString(), "Phone", "Smartphone", Category.TECHNOLOGY, "Used", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));

        // Accept the request with a rating of 5.0
        testXChanger.acceptRequest(request, 5.0f);

        assertEquals(1, testXChanger.getSucceedDeals());
        assertEquals(5.0f, otherXChanger.getAverageRating(), 0.01f);
        assertEquals(1, otherXChanger.getTotalRatings());
    }

    @Test
    public void testRejectRequestWithRating() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);
        Item requestedItem = new Item(otherXChanger.toString(), "Phone", "Smartphone", Category.TECHNOLOGY, "Used", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));

        // Reject the request with a rating of 2.0
        testXChanger.rejectRequest(request, 2.0f);

        assertEquals(1, testXChanger.getFailedDeals());
        assertEquals(2.0f, otherXChanger.getAverageRating(), 0.01f);
        assertEquals(1, otherXChanger.getTotalRatings());
    }

    @Test
    public void testCounterOffer() {
        ArrayList<Image> images = new ArrayList<>();
        Item item = new Item(otherXChanger.toString(), "Tablet", "Android tablet", Category.TECHNOLOGY, "New", images);

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
    public void testAddRating() {
        // Create Ratings and add them to the testXChanger
        Rating rating1 = new Rating(4.0f, otherXChanger, testXChanger, null, null);
        Rating rating2 = new Rating(5.0f, otherXChanger, testXChanger, null, null);

        testXChanger.addRating(rating1);
        testXChanger.addRating(rating2);

        // Verify total ratings and average rating
        assertEquals(2, testXChanger.getTotalRatings());
        assertEquals(4.5f, testXChanger.getAverageRating(), 0.01f);
    }
    @Test
    public void testCounterOfferWithRating() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item(otherXChanger.toString(), "Tablet", "Android tablet", Category.TECHNOLOGY, "New", images);
        Item requestedItem = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        Counteroffer counteroffer = new Counteroffer(request, "Let's exchange", offeredItem);

        testXChanger.acceptCounteroffer(counteroffer, 4.5f);

        assertFalse(counteroffer.isActive());
        assertFalse(request.isActive());
        assertEquals(4.5f, otherXChanger.getAverageRating(), 0.01f);
    }
    @Test
    public void testRejectCounteroffer() {
        ArrayList<Image> images = new ArrayList<>();
        Item offeredItem = new Item(otherXChanger.toString(), "Tablet", "Android tablet", Category.TECHNOLOGY, "New", images);
        Item requestedItem = new Item(testXChanger.toString(), "Laptop", "Gaming laptop", Category.TECHNOLOGY, "New", images);
        Item newRequestedItem = new Item(testXChanger.toString(), "Shoes", "Shoes", Category.FASHION, "Used", images);

        Request request = new Request(testXChanger, otherXChanger, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));
        Counteroffer counteroffer = new Counteroffer(request, "Let's exchange", newRequestedItem);

        // Reject the counteroffer with a rating of 2.0
        testXChanger.rejectCounteroffer(counteroffer, 2.0f);

        // Assertions
        assertFalse(counteroffer.isActive()); // Counteroffer is deactivated
        assertFalse(request.isActive());     // Request is also deactivated
        assertEquals(1, testXChanger.getFailedDeals()); // Failed deals incremented

        // Verify rating updates
        assertEquals(1, otherXChanger.getTotalRatings());
        assertEquals(2.0f, otherXChanger.getAverageRating(), 0.01f); // Average reflects the single rating
    }

    @Test
    public void testGetItem() {
        ArrayList<Image> images = new ArrayList<>();
        Item item1 = new Item(testXChanger.toString(), "Tablet", "Android tablet", Category.TECHNOLOGY, "New", images);
        Item item2 = new Item(otherXChanger.toString(), "Phone", "Smartphone", Category.TECHNOLOGY, "Used", images);

        testXChanger.getItems().add(item1);
        testXChanger.getItems().add(item2);

        Item fetchedItem = testXChanger.getItem(item1);

        assertNotNull(fetchedItem);
        assertEquals("Tablet", fetchedItem.getItemName());
    }

    @Test
    public void testGetItem_NotFound() {
        // Try to fetch an item that is not in the list
        Item nonExistentItem = new Item(testXChanger.toString(), "NonExistent", "Description", Category.fromDisplayName("Category"), "Condition", new ArrayList<>());
        assertNull(testXChanger.getItem(nonExistentItem));
    }
    @Test
    public void testSetRatingDynamically() {
        // Add multiple ratings to dynamically update the average
        testXChanger.addRating(new Rating(3.0f, otherXChanger, testXChanger, null, null));
        testXChanger.addRating(new Rating(4.0f, otherXChanger, testXChanger, null, null));
        testXChanger.addRating(new Rating(5.0f, otherXChanger, testXChanger, null, null));

        assertEquals(4.0f, testXChanger.getAverageRating(), 0.01f);
        assertEquals(3, testXChanger.getTotalRatings());
    }
}
