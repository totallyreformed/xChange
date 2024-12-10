package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class AdminTest {
    private admin testAdmin;

    @BeforeEach
    public void setUp() {
        admin.resetNextId();
        admin.getadmins().clear(); // Clear the admins list
        testAdmin = new admin("admin1", "admin1@example.com", new SimpleCalendar(2024, 12, 3), "IamtheAdmin", "Location1");
    }


    @Test
    public void testAdminInitialization() {
        assertEquals(1L, testAdmin.getUserId()); // Verify ID initialization
        assertEquals("admin1", testAdmin.getUsername());
        assertEquals("admin1@example.com", testAdmin.getEmail());
        assertEquals("IamtheAdmin", testAdmin.getPassword());
        assertEquals("Location1", testAdmin.getLocation());
    }

    @Test
    public void testNewAdminIdIncrement() {
        admin newAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location2");
        assertEquals(2L, newAdmin.getUserId()); // Verify unique ID increment
    }

    @Test
    public void testLoginSuccess() {
        // Set up the admin instance
        admin testAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2023, 12, 4), "IamtheAdmin", "Location1");

        // Simulate a login attempt
        assertTrue(testAdmin.login("admin2", "IamtheAdmin")); // Verify successful login
    }


    @Test
    public void testLoginFailure() {
        assertFalse(testAdmin.login("admin2", "WrongPassword")); // Incorrect password
        assertFalse(testAdmin.login("nonexistent", "Password2")); // Non-existent user
    }

    @Test
    public void testRegisterSuccess() {
        admin newAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location2");
        assertTrue(testAdmin.login(newAdmin.getUsername(),newAdmin.getPassword())); // Verify registration success
        assertEquals(2, admin.getadmins().size()); // Verify admin list size
    }

    @Test
    public void testRegisterFailure_UserExists() {
        admin duplicateUsernameAdmin = new admin("admin1", "newemail@example.com", new SimpleCalendar(2024, 12, 5), "Password3", "Location3");
        admin duplicateEmailAdmin = new admin("newuser", "admin1@example.com", new SimpleCalendar(2024, 12, 5), "Password3", "Location4");

        assertFalse(testAdmin.register(duplicateUsernameAdmin)); // Duplicate username
        assertFalse(testAdmin.register(duplicateEmailAdmin)); // Duplicate email
        assertEquals(1, admin.getadmins().size()); // Ensure no duplicates added
    }

    @Test
    public void testAdminsListAfterRegistration() {
        admin newAdmin1 = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "Password2", "Location2");
        admin newAdmin2 = new admin("admin3", "admin3@example.com", new SimpleCalendar(2024, 12, 5), "Password3", "Location3");

        assertEquals(3, admin.getadmins().size()); // Ensure all admins are registered
        assertTrue(admin.getadmins().contains(newAdmin1));
        assertTrue(admin.getadmins().contains(newAdmin2));
    }

    @Test
    public void testResetNextId() {
        admin.resetNextId();
        admin newAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "Password2", "Location2");
        assertEquals(1L, newAdmin.getUserId()); // Verify reset of ID generator
    }
}
