package com.example.xchange;

/**
 * Represents an admin user in the xChange application.
 * <p>
 * The {@code admin} class extends the {@link User} class and sets the user type to "admin".
 * </p>
 */
public class admin extends User {
    /**
     * Constructs an {@code admin} instance with the specified user details.
     *
     * @param username  The username of the admin.
     * @param email     The email address of the admin.
     * @param join_Date The date the admin joined, represented by {@link SimpleCalendar}.
     * @param password  The password of the admin.
     * @param location  The location of the admin.
     */
    public admin(String username, String email, SimpleCalendar join_Date, String password, String location) {
        super(username, email, join_Date, password, location, "admin");
    }
}
