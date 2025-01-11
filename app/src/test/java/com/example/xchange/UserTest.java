package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
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

    @Test
    void testParcelable() {
        User original = new User("alice", "alice@example.com", cal, "alicepass", "Town", "admin");
        original.setUser_id(101L);

        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        User created = User.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getUser_id(), created.getUser_id());
        assertEquals(original.getUsername(), created.getUsername());
        assertEquals(original.getEmail(), created.getEmail());
        assertEquals(original.getJoin_Date().toString(), created.getJoin_Date().toString());
        assertEquals(original.getPassword(), created.getPassword());
        assertEquals(original.getLocation(), created.getLocation());
        assertEquals(original.getUser_type(), created.getUser_type());
    }
}
