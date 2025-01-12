package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-06");
    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification("user1", "Test message", cal, 777L, 123L);
        notification.setId(55L);
    }

    @Test
    void testGettersSetters() {
        assertEquals(55L, notification.getId());
        assertEquals("user1", notification.getUsername());
        assertEquals("Test message", notification.getMessage());
        assertEquals(cal, notification.getTimestamp());
        assertEquals(777L, notification.getXChangeId());
        assertEquals(123L, notification.getItemId());

        // Updating fields
        notification.setUsername("newUser");
        notification.setMessage("Updated message");
        notification.setXChangeId(999L);
        notification.setItemId(456L);

        assertEquals("newUser", notification.getUsername());
        assertEquals("Updated message", notification.getMessage());
        assertEquals(999L, notification.getXChangeId());
        assertEquals(456L, notification.getItemId());
    }
}
