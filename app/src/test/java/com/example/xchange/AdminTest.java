package com.example.xchange;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class AdminTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-09");

    @Test
    void testAdminCreationAndGetters() {
        admin adminUser = new admin("adminUser", "admin@example.com", cal, "adminpass", "HQ");

        assertEquals("adminUser", adminUser.getUsername());
        assertEquals("admin@example.com", adminUser.getEmail());
        assertEquals(cal, adminUser.getJoin_Date());
        assertEquals("adminpass", adminUser.getPassword());
        assertEquals("HQ", adminUser.getLocation());
        assertEquals("admin", adminUser.getUser_type());
    }
}
