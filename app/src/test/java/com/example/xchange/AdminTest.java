package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class AdminTest {

    private SimpleCalendar cal;
    private admin adminUser;

    @BeforeEach
    void setUp() {
        cal = new SimpleCalendar("2025-01-09");
        adminUser = new admin("adminUser", "admin@example.com", cal, "adminpass", "HQ");
    }

    @Test
    void testAdminCreationAndGetters() {
        assertEquals("adminUser", adminUser.getUsername());
        assertEquals("admin@example.com", adminUser.getEmail());
        assertEquals(cal, adminUser.getJoin_Date());
        assertEquals("adminpass", adminUser.getPassword());
        assertEquals("HQ", adminUser.getLocation());
        assertEquals("admin", adminUser.getUser_type());
    }
}
