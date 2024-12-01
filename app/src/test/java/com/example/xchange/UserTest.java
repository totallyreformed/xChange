package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserTest {
    private User testUser;

    private static class TestUser extends User {
        public TestUser(Long user_id, String username, String email, LocalDate join_date, String password, String location) {
            super(user_id, username, email, join_date, password, location);
        }

        @Override
        public boolean login(String username, String password) {
            return this.getUsername().equals(username) && this.getPassword().equals(password);
        }

        @Override
        public boolean register(String username, String email, String password, String location) {
            return !username.isEmpty() && !email.isEmpty() && !password.isEmpty();
        }
    }

    @BeforeEach
    public void setUp() {
        testUser = new TestUser(1L, "testUser", "test@example.com", LocalDate.now(), "password123", "TestLocation");
    }

    @Test
    public void testGetters() {
        assertEquals(1L, testUser.getUserId());
        assertEquals("testUser", testUser.getUsername());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("password123", testUser.getPassword());
        assertEquals("TestLocation", testUser.getLocation());
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
    public void testLoginSuccess() {
        assertTrue(testUser.login("testUser", "password123"));
    }

    @Test
    public void testLoginFailure() {
        assertFalse(testUser.login("wrongUser", "password123"));
        assertFalse(testUser.login("testUser", "wrongPassword"));
    }

    @Test
    public void testRegisterSuccess() {
        assertTrue(testUser.register("newUser", "new@example.com", "newPassword", "NewLocation"));
    }

    @Test
    public void testRegisterFailure() {
        assertFalse(testUser.register("", "new@example.com", "newPassword", "NewLocation"));
        assertFalse(testUser.register("newUser", "", "newPassword", "NewLocation"));
        assertFalse(testUser.register("newUser", "new@example.com", "", "NewLocation"));
    }
}
