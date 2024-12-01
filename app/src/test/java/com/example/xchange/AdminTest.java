package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminTest {
    private admin testAdmin;

    @BeforeEach
    public void setUp() {
        MainActivity.admins = new ArrayList<>();
        MainActivity.categories = new ArrayList<>();
        MainActivity.reports = new ArrayList<>();
        MainActivity.statistics = new HashMap<>();

        testAdmin = new admin("admin1", "admin1@example.com", LocalDate.now(), "IamtheAdmin", "Location1");
        MainActivity.admins.add(testAdmin);
    }

    @Test
    public void testLoginSuccess() {
        assertTrue(testAdmin.login("admin1", "IamtheAdmin"));
    }

    @Test
    public void testLoginFailure() {
        assertFalse(testAdmin.login("admin1", "wrongPassword"));
        assertFalse(testAdmin.login("nonexistent", "IamtheAdmin"));
    }

    @Test
    public void testRegisterSuccess() {
        assertTrue(testAdmin.register("admin2", "admin2@example.com", "IamtheAdmin", "Location2"));
        assertEquals(2, MainActivity.admins.size());
    }

    @Test
    public void testRegisterFailure_UserExists() {
        assertFalse(testAdmin.register("admin1", "newemail@example.com", "IamtheAdmin", "Location3"));
        assertFalse(testAdmin.register("newuser", "admin1@example.com", "IamtheAdmin", "Location4"));
    }

    @Test
    public void testManageCategories_AddCategory() {
        testAdmin.manageCategories("Electronics", "add");
        assertTrue(MainActivity.categories.contains("Electronics"));
    }

    @Test
    public void testManageCategories_RemoveCategory() {
        MainActivity.categories.add("Electronics");
        testAdmin.manageCategories("Electronics", "remove");
        assertFalse(MainActivity.categories.contains("Electronics"));
    }

    @Test
    public void testManageCategories_RemoveNonExistentCategory() {
        int initialSize = MainActivity.categories.size();
        testAdmin.manageCategories("NonExistent", "remove");
        assertEquals(initialSize, MainActivity.categories.size());
    }

    @Test
    public void testViewReports() {
        MainActivity.reports.add("Report 1");
        MainActivity.reports.add("Report 2");
        testAdmin.viewReports();
        // This test will only verify that the method runs without exceptions
    }

    @Test
    public void testViewStatistics() {
        MainActivity.statistics.put("Total Users", 10);
        MainActivity.statistics.put("Total Trades", 5);
        testAdmin.viewStatistics();
        // This test will only verify that the method runs without exceptions
    }

    @Test
    public void testGetSpecificStatistic() {
        MainActivity.statistics.put("Total Users", 10);
        assertEquals(10, testAdmin.getSpecificStatistic("Total Users"));
    }
}
