package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class NotificationTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-06");
    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification("user1", "Test message", cal, 777L);
        notification.setId(55L);
    }

    @Test
    void testGettersSetters() {
        assertEquals(55L, notification.getId());
        assertEquals("user1", notification.getUsername());
        assertEquals("Test message", notification.getMessage());
        assertEquals(cal, notification.getTimestamp());
        assertEquals(777L, notification.getXChangeId());

        // Updating fields
        notification.setUsername("newUser");
        notification.setMessage("Updated message");
        notification.setXChangeId(999L);

        assertEquals("newUser", notification.getUsername());
        assertEquals("Updated message", notification.getMessage());
        assertEquals(999L, notification.getXChangeId());
    }

    @Test
    void testParcelable() {
        Parcel parcel = Parcel.obtain();
        notification.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Notification created = Notification.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(notification.getId(), created.getId());
        assertEquals(notification.getUsername(), created.getUsername());
        assertEquals(notification.getMessage(), created.getMessage());
        assertEquals(notification.getTimestamp().toString(), created.getTimestamp().toString());
        assertEquals(notification.getXChangeId(), created.getXChangeId());
    }
}
