package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class xChangerTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-08");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    @Test
    void testInitialValuesAndParcelable() {
        xChanger xc = createXChanger("testUser");
        assertEquals(0f, xc.getAverageRating(), 0.001f);
        assertEquals(0, xc.getTotalRatings());
        assertEquals(0, xc.getSucceedDeals());
        assertEquals(0, xc.getFailedDeals());
        assertNotNull(xc.getRatings());
        assertNotNull(xc.getReports());
        assertNotNull(xc.getItems());
        assertNotNull(xc.getRequests());
        assertNotNull(xc.getCounterOffers());
        assertNotNull(xc.getFinalized());

        Parcel parcel = Parcel.obtain();
        xc.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        xChanger created = xChanger.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(xc.getAverageRating(), created.getAverageRating(), 0.001f);
        assertEquals(xc.getTotalRatings(), created.getTotalRatings());
        assertTrue(created.getRatings().isEmpty());
    }

    @Test
    void testAddRating() {
        xChanger xc = createXChanger("ratee");
        Rating rating1 = new Rating(4.0f, null, xc, null, null);
        Rating rating2 = new Rating(2.0f, null, xc, null, null);
        xc.addRating(rating1);
        assertEquals(1, xc.getTotalRatings());
        assertEquals(4.0f, xc.getAverageRating(), 0.001f);
        xc.addRating(rating2);
        assertEquals(2, xc.getTotalRatings());
        assertEquals((4.0f + 2.0f) / 2, xc.getAverageRating(), 0.001f);
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
        xChanger xc = createXChanger("dealer");
        int origSucceed = xc.getSucceedDeals();
        int origFailed = xc.getFailedDeals();

        xc.plusOneSucceedDeal();
        xc.plusOneFailedDeal();
        assertEquals(origSucceed + 1, xc.getSucceedDeals());
        assertEquals(origFailed + 1, xc.getFailedDeals());

        Item item1 = new Item("dealer", "ItemA", "desc", Category.ALL, "New", null);
        item1.setItemId(1L);
        Item item2 = new Item("dealer", "ItemB", "desc", Category.ALL, "Used", null);
        item2.setItemId(2L);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        xc.setItems(items);
        assertEquals(item1, xc.getItem(item1));
        xc.deleteItem(item1);
        assertNull(xc.getItem(item1));
    }

    @Test
    void testUploadRequestAndCounterOffer() throws InterruptedException {
        xChanger xc = createXChanger("uploader");
        // UploadItem() now uses the AppDatabase stub.
        xc.UploadItem("NewItem", "Desc", Category.FASHION, "New", null);
        Thread.sleep(100);
        assertFalse(xc.getItems().isEmpty());

        xChanger target = createXChanger("target");
        Item offered = new Item(xc.getUsername(), "Offered", "Desc", Category.BOOKS, "New", null);
        Item requested = new Item(target.getUsername(), "Requested", "Desc", Category.BOOKS, "New", null);
        xc.RequestItem(target, offered, requested);
        assertFalse(xc.getRequests().isEmpty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> xc.counterOffer(null, null));
        assertTrue(ex.getMessage().contains("cannot be null"));

        Request req = new Request(xc, target, offered, requested, cal);
        xc.counterOffer(offered, req);
        Thread.sleep(100);
        assertFalse(xc.getCounterOffers().isEmpty());
    }

    @Test
    void testAcceptRejectRequestCounteroffer() {
        xChanger xc = createXChanger("actionUser");
        Request req = new Request(xc, xc, null, null, cal);
        assertDoesNotThrow(() -> xc.acceptRequest(req, 4.0f));
        assertDoesNotThrow(() -> xc.rejectRequest(req, 3.0f));
        Counteroffer co = new Counteroffer(req, new Item("actionUser", "Item", "desc", Category.HOME, "New", null), xc, xc);
        assertDoesNotThrow(() -> xc.acceptCounteroffer(co, 5.0f));
        assertDoesNotThrow(() -> xc.rejectCounteroffer(co, 2.0f));
    }
}
