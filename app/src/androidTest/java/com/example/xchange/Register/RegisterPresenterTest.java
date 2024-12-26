package com.example.xchange.Register;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test for RegisterPresenter.
 * These tests run on an Android device or emulator.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterPresenterTest {

    private AppDatabase database;
    private UserDao userDao;
    private Context context;
    private RegisterPresenter presenter;
    private FakeRegisterView fakeView;

    @Before
    public void setUp() {
        // Initialize context
        context = ApplicationProvider.getApplicationContext();

        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // For testing purposes
                .build();

        userDao = database.userDao();

        // Create FakeRegisterView
        fakeView = new FakeRegisterView();

        // Create RegisterPresenter
        presenter = new RegisterPresenter(context);
    }

    @After
    public void tearDown() {
        database.close();
    }

    /**
     * Helper method to insert a test user into the database.
     */
    private void insertTestUser(String username, String email, String password, String userType) {
        User user = new User(username, email, null, password, "TestLocation", userType);
        userDao.insertUser(user);
    }

    /**
     * Test successful user registration.
     */
    @Test
    public void testRegisterUser_Success() throws InterruptedException {
        // Ensure the username is not already taken
        String username = "newUser";
        String email = "newuser@example.com";
        String password = "password123";

        User newUser = new User(username, email, null, password, "Default Location", "xChanger");

        CountDownLatch latch = new CountDownLatch(1);
        fakeView.setLatch(latch);

        presenter.registerUser(newUser, fakeView);

        boolean awaitSuccess = latch.await(2, TimeUnit.SECONDS);

        assertTrue("Latch should have counted down", awaitSuccess);
        assertNotNull("Success message should be set", fakeView.successMessage);
        assertEquals("Registration successful!", fakeView.successMessage);
        assertNull("Failure message should not be set on success", fakeView.failureMessage);

        // Verify that the user was inserted into the database
        User insertedUser = userDao.findByUsername_initial(username);
        assertNotNull("User should be inserted into the database", insertedUser);
        assertEquals("Inserted user should have correct username", username, insertedUser.getUsername());
    }

    /**
     * Test user registration failure due to existing username.
     */
    @Test
    public void testRegisterUser_Failure_ExistingUsername() throws InterruptedException {
        // Insert a user with the same username
        String username = "existingUser";
        String email = "existing@example.com";
        String password = "password123";

        insertTestUser(username, email, password, "xChanger");

        User duplicateUser = new User(username, "newemail@example.com", null, "newpassword", "New Location", "xChanger");

        CountDownLatch latch = new CountDownLatch(1);
        fakeView.setLatch(latch);

        presenter.registerUser(duplicateUser, fakeView);

        boolean awaitSuccess = latch.await(2, TimeUnit.SECONDS);

        assertTrue("Latch should have counted down", awaitSuccess);
        assertNotNull("Failure message should be set", fakeView.failureMessage);
        assertEquals("Username already exists", fakeView.failureMessage);
        assertNull("Success message should not be set on failure", fakeView.successMessage);
    }

    /**
     * Test user registration failure due to invalid data (e.g., empty username).
     */
    @Test
    public void testRegisterUser_Failure_InvalidData() throws InterruptedException {
        // Attempt to register a user with an empty username
        String username = "";
        String email = "invalid@example.com";
        String password = "password123";

        User invalidUser = new User(username, email, null, password, "Default Location", "xChanger");

        CountDownLatch latch = new CountDownLatch(1);
        fakeView.setLatch(latch);

        presenter.registerUser(invalidUser, fakeView);

        boolean awaitSuccess = latch.await(2, TimeUnit.SECONDS);

        assertTrue("Latch should have counted down", awaitSuccess);
        assertNotNull("Failure message should be set", fakeView.failureMessage);
        assertEquals("Invalid user data", fakeView.failureMessage); // Ανάλογα με το μήνυμα αποτυχίας
        assertNull("Success message should not be set on failure", fakeView.successMessage);
    }

    /**
     * Fake implementation of RegisterView for testing.
     */
    private static class FakeRegisterView implements RegisterPresenter.RegisterView {

        String successMessage = null;
        String failureMessage = null;
        CountDownLatch latch = null;

        void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onRegisterSuccess(String message) {
            this.successMessage = message;
            if (latch != null) {
                latch.countDown();
            }
        }

        @Override
        public void onRegisterFailure(String message) {
            this.failureMessage = message;
            if (latch != null) {
                latch.countDown();
            }
        }
    }
}