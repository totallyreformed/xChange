package com.example.xchange.Login;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.SimpleCalendar;
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
 * Instrumented test for LoginPresenter.
 * These tests run on an Android device or emulator.
 */
@RunWith(AndroidJUnit4.class)
public class LoginPresenterTest {

    private AppDatabase database;
    private UserDao userDao;
    private LoginPresenter presenter;
    private FakeLoginView fakeView;

    @Before
    public void setUp() {
        // Δημιουργία in-memory database για testing
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // Επιτρέπει τις ερωτήσεις στον κύριο νήμα για testing
                .build();

        userDao = database.userDao();

        // Δημιουργία της fake υλοποίησης του LoginView
        fakeView = new FakeLoginView();

        // Δημιουργία του LoginPresenter με το πραγματικό Context
        presenter = new LoginPresenter(context);
    }

    @After
    public void tearDown() {
        database.close();
    }

    /**
     * Βοηθητική μέθοδος για την εισαγωγή ενός χρήστη στη βάση δεδομένων.
     *
     * @param username Το username του χρήστη.
     * @param email    Το email του χρήστη.
     * @param password Το password του χρήστη.
     * @param userType Ο τύπος του χρήστη ("xChanger" ή "admin").
     */
    private void insertTestUser(String username, String email, String password, String userType) {
        User user = new User(username, email, new SimpleCalendar(2024,18,12), password, "TestLocation", userType);
        userDao.insertUser(user);
    }

    @Test
    public void testLoginAsXChanger_Success() {
        // Εισαγωγή ενός test xChanger χρήστη στη βάση δεδομένων
        insertTestUser("testXChanger", "xchanger@example.com", "password123", "xChanger");

        // Εκτέλεση της μεθόδου loginAsXChanger
        presenter.loginUser("testXChanger", "password123", fakeView);

        // Επιβεβαίωση ότι το onLoginSuccess καλείται με τον σωστό χρήστη
        assertNotNull("User should have been set on success", fakeView.loggedInUser);
        assertEquals("testXChanger", fakeView.loggedInUser.getUsername());
        assertNull("Failure message should not be set on success", fakeView.failureMessage);
    }

    @Test
    public void testLoginAsXChanger_Failure() {
        // Δεν εισάγουμε κανέναν χρήστη στη βάση δεδομένων

        // Εκτέλεση της μεθόδου loginAsXChanger με μη έγκυρα credentials
        presenter.loginUser("nonexistentUser", "wrongPassword", fakeView);

        // Επιβεβαίωση ότι το onLoginFailure καλείται με το σωστό μήνυμα
        assertNull("User should not be set on failure", fakeView.loggedInUser);
        assertNotNull("Failure message should be set on failure", fakeView.failureMessage);
        assertEquals("Invalid username or password", fakeView.failureMessage);
    }

    @Test
    public void testLoginAsAdmin_Success() {
        // Εισαγωγή ενός test admin χρήστη στη βάση δεδομένων
        insertTestUser("adminUser", "admin@example.com", "adminPass", "admin");

        // Εκτέλεση της μεθόδου loginAsAdmin
        presenter.loginUser("adminUser", "adminPass", fakeView);

        // Επιβεβαίωση ότι το onLoginSuccess καλείται με τον σωστό χρήστη
        assertNotNull("User should have been set on success", fakeView.loggedInUser);
        assertEquals("adminUser", fakeView.loggedInUser.getUsername());
        assertNull("Failure message should not be set on success", fakeView.failureMessage);
    }

    @Test
    public void testLoginAsAdmin_Failure() {
        // Δεν εισάγουμε κανέναν admin χρήστη στη βάση δεδομένων

        // Εκτέλεση της μεθόδου loginAsAdmin με μη έγκυρα credentials
        presenter.loginUser("adminUser", "wrongPass", fakeView);

        // Επιβεβαίωση ότι το onLoginFailure καλείται με το σωστό μήνυμα
        assertNull("User should not be set on failure", fakeView.loggedInUser);
        assertNotNull("Failure message should be set on failure", fakeView.failureMessage);
        assertEquals("Invalid username or password", fakeView.failureMessage);
    }

    /**
     * Fake υλοποίηση του LoginView για testing.
     */
    private static class FakeLoginView implements LoginPresenter.LoginView {
        User loggedInUser = null;
        String failureMessage = null;

        @Override
        public void onLoginSuccess(User user) {
            loggedInUser = user;
        }

        @Override
        public void onLoginFailure(String message) {
            failureMessage = message;
        }
    }
}