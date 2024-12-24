package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("testUser", "test@example.com", new SimpleCalendar(2024,18,12), "password123", "TestLocation", "user");
    }

    @Test
    public void testGetters() {
        assertNull(testUser.getUser_id()); // Primary key is auto-generated
        assertEquals("testUser", testUser.getUsername());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("password123", testUser.getPassword());
        assertEquals("TestLocation", testUser.getLocation());
        assertEquals("2024-12-03", testUser.getJoin_Date());
        assertEquals("user", testUser.getUser_type());
    }

    @Test
    public void testSetters() {
        testUser.setUsername("newUsername");
        testUser.setEmail("newemail@example.com");
        testUser.setPassword("newPassword");
        testUser.setLocation("NewLocation");

        assertEquals("newUsername", testUser.getUsername());
        assertEquals("newemail@example.com", testUser.getEmail());
        assertEquals("newPassword", testUser.getPassword());
        assertEquals("NewLocation", testUser.getLocation());
    }

    @Test
    public void testUserType() {
        testUser.setUser_type("admin");
        assertEquals("admin", testUser.getUser_type());

        testUser.setUser_type("xChanger");
        assertEquals("xChanger", testUser.getUser_type());
    }
}
