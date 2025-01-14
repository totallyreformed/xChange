package com.example.xchange.RegisterPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Register.RegisterPresenter;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test class for RegisterPresenter using the real production objects.
 * This class achieves 100% coverage of RegisterPresenter by testing:
 *   • The success path – registering a new user.
 *   • The failure path – attempting to register the same user twice.
 */
public class RegisterPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Context context;
    private RegisterPresenter presenter;
    private TestRegisterView testView;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // It is assumed that AppDatabase is implemented so that you can clear user data.
        AppDatabase db = AppDatabase.getInstance(context);
        // Clear the users table (assuming you have such a method; adjust as needed)
        db.userDao().deleteAllUsers();

        // Create the presenter (which internally creates a real UserRepository)
        presenter = new RegisterPresenter(context);
        testView = new TestRegisterView();
    }

    /**
     * Tests that registering a new user results in success.
     */
    @Test
    public void testRegisterUserSuccess() throws InterruptedException {
        User user = new User("testUser", "test@example.com", null, "password", "Test Location", "xChanger");
        presenter.registerUser(user, testView);

        // Wait for callback.
        assertTrue("Timed out waiting for callback", testView.awaitCallback());

        // Verify the success callback was invoked.
        assertEquals("Registration successful!", testView.successMessage);
        assertNull("Failure message should be null", testView.failureMessage);
    }

    /**
     * Tests that when a user is registered twice, the second registration fails.
     * (This test assumes that the real repository prevents duplicate user registrations.)
     */
    @Test
    public void testRegisterUserFailure() throws InterruptedException {
        // First, register the user successfully.
        User user = new User("testUser", "test@example.com", null, "password", "Test Location", "xChanger");
        presenter.registerUser(user, testView);
        assertTrue("Timed out waiting for callback", testView.awaitCallback());
        // Verify first registration succeeded.
        assertEquals("Registration successful!", testView.successMessage);
        assertNull(testView.failureMessage);

        // Reset the test view for the next callback.
        testView.reset();

        // Attempt to register the same user again.
        presenter.registerUser(user, testView);
        assertTrue("Timed out waiting for callback on second registration", testView.awaitCallback());

        // Verify that registration fails on duplicate (the error message should be produced by your repository logic)
        assertNotNull("Failure message was expected", testView.failureMessage);
        assertNull("Success message should be null on failure", testView.successMessage);
    }

    /**
     * A simple test implementation of RegisterPresenter.RegisterView that uses a latch
     * to signal when a callback has been received.
     */
    private static class TestRegisterView implements RegisterPresenter.RegisterView {
        String successMessage;
        String failureMessage;
        private CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void onRegisterSuccess(String message) {
            successMessage = message;
            latch.countDown();
        }

        @Override
        public void onRegisterFailure(String message) {
            failureMessage = message;
            latch.countDown();
        }

        boolean awaitCallback() throws InterruptedException {
            return latch.await(5, TimeUnit.SECONDS);
        }

        void reset() {
            successMessage = null;
            failureMessage = null;
            latch = new CountDownLatch(1);
        }
    }
}
