package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private final SimpleCalendar cal = new SimpleCalendar("2025-01-01");

    @BeforeEach
    void setUp() {
        user = new User("john", "john@example.com", cal, "pass123", "City", "user");
        user.setUser_id(100L);
    }

    @Test
    void testGettersSetters() {
        assertEquals(100L, user.getUser_id());
        assertEquals("john", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(cal, user.getJoin_Date());
        assertEquals("pass123", user.getPassword());
        assertEquals("City", user.getLocation());
        assertEquals("user", user.getUser_type());

        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());
    }
}
