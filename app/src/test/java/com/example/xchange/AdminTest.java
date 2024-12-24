package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    private admin testAdmin;

    @BeforeEach
    public void setUp() {
        testAdmin = new admin("admin1", "admin1@example.com", new SimpleCalendar(2024, 12, 3), "IamtheAdmin", "HQ");
    }

    @Test
    public void testAdminInitialization() {
        // Ensure that all fields are correctly initialized
        assertNull(testAdmin.getUser_id());
        assertEquals("admin1", testAdmin.getUsername());
        assertEquals("admin1@example.com", testAdmin.getEmail());
        assertEquals("IamtheAdmin", testAdmin.getPassword());
        assertEquals("HQ", testAdmin.getLocation());
        assertEquals("admin", testAdmin.getUser_type()); // Ensure user_type is "admin"
    }

    @Test
    public void testSettersAndGetters() {
        // Modify the admin fields and ensure they update correctly
        testAdmin.setUsername("newAdmin");
        testAdmin.setEmail("newAdmin@example.com");
        testAdmin.setPassword("newPassword");
        testAdmin.setLocation("NewHQ");

        assertEquals("newAdmin", testAdmin.getUsername());
        assertEquals("newAdmin@example.com", testAdmin.getEmail());
        assertEquals("newPassword", testAdmin.getPassword());
        assertEquals("NewHQ", testAdmin.getLocation());
    }

    @Test
    public void testInheritance() {
        // Verify that admin is an instance of User
        assertNotNull(testAdmin);

        // Ensure admin-specific behavior doesn't override User behavior
        assertEquals("admin", testAdmin.getUser_type());
    }
}
