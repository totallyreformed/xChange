package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminTest {
    private admin testAdmin;

    @BeforeEach
    public void setUp() {
        // Initialize shared state in MainActivity
        MainActivity.admins = new ArrayList<>();
        MainActivity.categories = new ArrayList<>();
        MainActivity.reports = new ArrayList<>();
        MainActivity.statistics = new HashMap<>();

        admin.resetNextId(); // Reset the ID generator for consistency across tests

        testAdmin = new admin("admin1", "admin1@example.com", new SimpleCalendar(2024, 12, 3), "IamtheAdmin", "Location1");
        MainActivity.admins.add(testAdmin); // Add the admin to the shared admin list
    }

    @Test
    public void testNewAdminIdIncrement() {
        admin newAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location1");
        assertEquals(2L, newAdmin.getUserId()); // Verify unique ID generation
    }

    @Test
    public void testLoginSuccess() {
        assertTrue(testAdmin.login("admin1", "IamtheAdmin")); // Verify successful login
    }

    @Test
    public void testLoginFailure() {
        assertFalse(testAdmin.login("admin1", "wrongPassword")); // Incorrect password
        assertFalse(testAdmin.login("nonexistent", "IamtheAdmin")); // Non-existent user
    }

    @Test
    public void testRegisterSuccess() {
        admin newAdmin = new admin("admin2", "admin2@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location2");
        assertTrue(testAdmin.register(newAdmin)); // Register a new admin
        assertEquals(2, MainActivity.admins.size()); // Verify admin list size
    }

    @Test
    public void testRegisterFailure_UserExists() {
        admin duplicateUsernameAdmin = new admin("admin1", "newemail@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location3");
        admin duplicateEmailAdmin = new admin("newuser", "admin1@example.com", new SimpleCalendar(2024, 12, 4), "IamtheAdmin", "Location4");

        assertFalse(testAdmin.register(duplicateUsernameAdmin)); // Duplicate username
        assertFalse(testAdmin.register(duplicateEmailAdmin)); // Duplicate email
    }

    @Test
    public void testManageCategories_AddCategory() {
        testAdmin.manageCategories("Electronics", "add"); // Add a new category
        assertTrue(MainActivity.categories.contains("Electronics")); // Verify addition
    }

    @Test
    public void testManageCategories_RemoveCategory() {
        MainActivity.categories.add("Electronics"); // Pre-add category
        testAdmin.manageCategories("Electronics", "remove"); // Remove category
        assertFalse(MainActivity.categories.contains("Electronics")); // Verify removal
    }

    @Test
    public void testManageCategories_RemoveNonExistentCategory() {
        int initialSize = MainActivity.categories.size();
        testAdmin.manageCategories("NonExistent", "remove"); // Attempt to remove non-existent category
        assertEquals(initialSize, MainActivity.categories.size()); // Verify no changes
    }

    @Test
    public void testViewReports() {
        MainActivity.reports.add("Report 1");
        MainActivity.reports.add("Report 2");

        // Call the method and verify it executes without exceptions
        assertDoesNotThrow(() -> testAdmin.viewReports());
    }

    @Test
    public void testViewStatistics() {
        MainActivity.statistics.put("Total Users", 10);
        MainActivity.statistics.put("Total Trades", 5);

        // Call the method and verify it executes without exceptions
        assertDoesNotThrow(() -> testAdmin.viewStatistics());
    }

    @Test
    public void testGetSpecificStatistic() {
        MainActivity.statistics.put("Total Users", 10); // Add sample statistic
        assertEquals(10, testAdmin.getSpecificStatistic("Total Users")); // Verify retrieval
    }
}
