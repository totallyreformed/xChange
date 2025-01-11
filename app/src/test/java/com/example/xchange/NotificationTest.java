package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class NotificationTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-06");

    @Test
    void testGettersSetters() {
        Notification notif = new Notification("user1", "Test message", cal, 777L);
        notif.setId(55L);

        assertEquals(55L, notif.getId());
        assertEquals("user1", notif.getUsername());
        assertEquals("Test message", notif.getMessage());
        assertEquals(cal, notif.getTimestamp());
        assertEquals(777L, notif.getXChangeId());
    }

    @Test
    void testParcelable() {
        Notification original = new Notification("user2", "Parcel message", cal, 888L);
        original.setId(66L);
        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Notification created = Notification.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getId(), created.getId());
        assertEquals(original.getUsername(), created.getUsername());
        assertEquals(original.getMessage(), created.getMessage());
        assertEquals(original.getTimestamp().toString(), created.getTimestamp().toString());
        assertEquals(original.getXChangeId(), created.getXChangeId());
    }
}
