package com.example.xchange.Login;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Context context;
    private LoginPresenter presenter;
    private UserRepository userRepository;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        userRepository = new UserRepository(context);
        presenter = new LoginPresenter(context);
        executorService = Executors.newSingleThreadExecutor();

        // Prepopulate the database with a test user
        executorService.execute(() -> {
            User testUser = new User("testUser", "test@example.com", null, "password", "Test Location", "xChanger");
            AppDatabase.getInstance(context).userDao().insertUser(testUser);
        });
    }

    @After
    public void tearDown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    @Test
    public void testLoginSuccess() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        presenter.loginUser("testUser", "password", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                assertNotNull(user);
                assertEquals("testUser", user.getUsername());
                latch.countDown();
            }

            @Override
            public void onLoginFailure(String message) {
                fail("Login should succeed, but failed with message: " + message);
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testLoginFailureInvalidCredentials() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        presenter.loginUser("testUser", "wrongPassword", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                fail("Login should fail, but succeeded with user: " + user.getUsername());
            }

            @Override
            public void onLoginFailure(String message) {
                assertEquals("Invalid credentials", message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testLoginFailureNonexistentUser() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        presenter.loginUser("nonexistentUser", "password", new LoginPresenter.LoginView() {
            @Override
            public void onLoginSuccess(User user) {
                fail("Login should fail, but succeeded with user: " + user.getUsername());
            }

            @Override
            public void onLoginFailure(String message) {
                assertEquals("Invalid credentials", message);
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testGetUserByUsername() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                assertNotNull(user);
                assertEquals("testUser", user.getUsername());
                latch.countDown();
            }
        };

        userRepository.getUserByUsername("testUser").observeForever(observer);

        assertTrue(latch.await(5, TimeUnit.SECONDS));

        // Remove the observer after the test
        userRepository.getUserByUsername("testUser").removeObserver(observer);
    }

}