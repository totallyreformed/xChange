package com.example.xchange.LoginPresenterTest;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Login.LoginPresenter;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LoginPresenterTest {

    private LoginPresenter presenter;
    private UserRepository repository;
    private Context context;

    @Before
    public void setUp() {
        CountDownLatch latch = new CountDownLatch(1);
        String uniqueUsername = "testUser_" + System.currentTimeMillis();
        context = ApplicationProvider.getApplicationContext();
        repository = new UserRepository(context);
        presenter = new LoginPresenter(context);

        // Insert a test user into the database
        User testUser = new User("testUser", "test@example.com", SimpleCalendar.today(),"password123","loc1","xChanger");
        repository.registerUser(testUser, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                // Registration success
            }

            @Override
            public void onFailure(String message) {
                fail("Failed to set up test user: " + message);
            }
        });
    }

    @After
    public void tearDown() {
        repository.loginUser("testUser", "password123", new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                AppDatabase.getUserDao().deleteUser(user);
            }

            @Override
            public void onFailure(String message) {
                fail("Failed to log in test user for deletion: " + message);
            }
        });
    }

    @Test
    public void testLoginSuccess() {
        presenter.loginUser("testUser", "password123", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                assertNotNull("User should not be null on successful login", user);
                assertEquals("testUser", user.getUsername());
                assertEquals("test@example.com", user.getEmail());
            }

            @Override
            public void onLoginFailure(String message) {
                fail("Login should not fail for valid credentials");
            }
        });
    }

    @Test
    public void testLoginFailure_InvalidCredentials() {
        presenter.loginUser("invalidUser", "wrongPassword", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                fail("Login should fail for invalid credentials");
            }

            @Override
            public void onLoginFailure(String message) {
                assertNotNull("Failure message should not be null", message);
                assertEquals("Invalid credentials", message);
            }
        });
    }

    @Test
    public void testLoginFailure_EmptyCredentials() {
        presenter.loginUser("", "", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                fail("Login should fail for empty credentials");
            }

            @Override
            public void onLoginFailure(String message) {
                assertNotNull("Failure message should not be null", message);
                assertEquals("Invalid credentials", message);
            }
        });
    }
}
