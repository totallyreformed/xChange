package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
public class xChangerTest {

    private SimpleCalendar cal;
    private xChanger testXChanger;

    @BeforeEach
    void setUp() {
        cal = new SimpleCalendar("2025-01-08");
        testXChanger = createXChanger("testUser");
    }

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    @Test
    void testInitialValuesAndParcelable() {
        assertEquals(0f, testXChanger.getAverageRating(), 0.001f);
        assertEquals(0, testXChanger.getTotalRatings());
        assertEquals(0, testXChanger.getSucceedDeals());
        assertEquals(0, testXChanger.getFailedDeals());
        assertNotNull(testXChanger.getRatings());
        assertNotNull(testXChanger.getReports());
        assertNotNull(testXChanger.getItems());
        assertNotNull(testXChanger.getRequests());
        assertNotNull(testXChanger.getCounterOffers());
        assertNotNull(testXChanger.getFinalized());

        Parcel parcel = Parcel.obtain();
        testXChanger.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        xChanger created = xChanger.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(testXChanger.getAverageRating(), created.getAverageRating(), 0.001f);
        assertEquals(testXChanger.getTotalRatings(), created.getTotalRatings());
        assertTrue(created.getRatings().isEmpty());
    }

    @Test
    void testAddRating() {
        Rating rating1 = new Rating(4.0f, null, testXChanger, null, null);
        Rating rating2 = new Rating(2.0f, null, testXChanger, null, null);
        testXChanger.addRating(rating1);
        assertEquals(1, testXChanger.getTotalRatings());
        assertEquals(4.0f, testXChanger.getAverageRating(), 0.001f);
        testXChanger.addRating(rating2);
        assertEquals(2, testXChanger.getTotalRatings());
        assertEquals((4.0f + 2.0f) / 2, testXChanger.getAverageRating(), 0.001f);
    }

    @Test
    void testReport() {
        xChanger reporter = createXChanger("reporter");
        xChanger reported = createXChanger("reported");
        xChange dummyXchg = new xChange(new Request(), (Counteroffer) null, cal);
        dummyXchg.setDealStatus("Accepted");
        reporter.report(reported, "message", dummyXchg);
        String lastReport = reporter.getReports().get(reporter.getReports().size() - 1);
        assertTrue(lastReport.contains("reporter reported user reported"));
    }

    @Test
    void testDealStatisticsAndItemMethods() {
        int origSucceed = testXChanger.getSucceedDeals();
        int origFailed = testXChanger.getFailedDeals();

        testXChanger.plusOneSucceedDeal();
        testXChanger.plusOneFailedDeal();
        assertEquals(origSucceed + 1, testXChanger.getSucceedDeals());
        assertEquals(origFailed + 1, testXChanger.getFailedDeals());

        Item item1 = new Item("testUser", "ItemA", "desc", Category.ALL, "New", null);
        item1.setItemId(1L);
        Item item2 = new Item("testUser", "ItemB", "desc", Category.ALL, "Used", null);
        item2.setItemId(2L);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        testXChanger.setItems(items);
        assertEquals(item1, testXChanger.getItem(item1));
        testXChanger.deleteItem(item1);
        assertNull(testXChanger.getItem(item1));
    }

    @Test
    void testUploadRequestAndCounterOffer() throws InterruptedException {
        // UploadItem() now uses the AppDatabase stub.
        testXChanger.UploadItem("NewItem", "Desc", Category.FASHION, "New", null);
        Thread.sleep(100);
        assertFalse(testXChanger.getItems().isEmpty());

        xChanger target = createXChanger("target");
        Item offered = new Item(testXChanger.getUsername(), "Offered", "Desc", Category.BOOKS, "New", null);
        Item requested = new Item(target.getUsername(), "Requested", "Desc", Category.BOOKS, "New", null);
        testXChanger.RequestItem(target, offered, requested);
        assertFalse(testXChanger.getRequests().isEmpty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> testXChanger.counterOffer(null, null));
        assertTrue(ex.getMessage().contains("cannot be null"));

        Request req = new Request(testXChanger, target, offered, requested, cal);
        testXChanger.counterOffer(offered, req);
        Thread.sleep(100);
        assertFalse(testXChanger.getCounterOffers().isEmpty());
    }

    @Test
    void testAcceptRejectRequestCounteroffer() {
        Request req = new Request(testXChanger, testXChanger, null, null, cal);
        assertDoesNotThrow(() -> testXChanger.acceptRequest(req, 4.0f));
        assertDoesNotThrow(() -> testXChanger.rejectRequest(req, 3.0f));
        Counteroffer co = new Counteroffer(req, new Item("actionUser", "Item", "desc", Category.HOME, "New", null), testXChanger, testXChanger);
        assertDoesNotThrow(() -> testXChanger.acceptCounteroffer(co, 5.0f));
        assertDoesNotThrow(() -> testXChanger.rejectCounteroffer(co, 2.0f));
    }
}
