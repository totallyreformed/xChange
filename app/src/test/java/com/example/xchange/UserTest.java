package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User testUser;

    // Concrete implementation of the abstract User class for testing purposes
    private static class TestUser extends User {
        public TestUser(Long user_id, String username, String email, SimpleCalendar join_date, String password, String location) {
            super(user_id, username, email, join_date, password, location);
        }

        @Override
        public boolean login(String username, String password) {
            return this.getUsername().equals(username) && this.getPassword().equals(password);
        }

        @Override
        public boolean register(User user) {
            return user.getUsername() != null && !user.getUsername().isEmpty() &&
                    user.getEmail() != null && !user.getEmail().isEmpty() &&
                    user.getPassword() != null && !user.getPassword().isEmpty();
        }
    }

    @BeforeEach
    public void setUp() {
        testUser = new TestUser(1L, "testUser", "test@example.com", new SimpleCalendar(2024, 12, 3), "password123", "TestLocation");
    }

    @Test
    public void testGetters() {
        assertEquals(1L, testUser.getUserId());
        assertEquals("testUser", testUser.getUsername());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("password123", testUser.getPassword());
        assertEquals("TestLocation", testUser.getLocation());
        assertEquals(new SimpleCalendar(2024, 12, 3).toString(), testUser.getJoinDate().toString());
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
        User newUser = new TestUser(2L, "newUser", "new@example.com", new SimpleCalendar(2024, 12, 4), "newPassword", "NewLocation");
        assertTrue(testUser.register(newUser));
    }

    @Test
    public void testRegisterFailure() {
        User invalidUser1 = new TestUser(3L, "", "new@example.com", new SimpleCalendar(2024, 12, 4), "newPassword", "NewLocation");
        User invalidUser2 = new TestUser(4L, "newUser", "", new SimpleCalendar(2024, 12, 4), "newPassword", "NewLocation");
        User invalidUser3 = new TestUser(5L, "newUser", "new@example.com", new SimpleCalendar(2024, 12, 4), "", "NewLocation");

        assertFalse(testUser.register(invalidUser1));
        assertFalse(testUser.register(invalidUser2));
        assertFalse(testUser.register(invalidUser3));
    }
}
