package com.example.xchange.database.dao;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.xchange.Notification;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.NotificationDao;
import com.example.xchange.SimpleCalendar;
import org.junit.*;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NotificationDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private NotificationDao notificationDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        notificationDao = database.notificationDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveNotifications() {
        Notification notification = new Notification("user1", "Message 1", new SimpleCalendar(2025, 1, 1), 1L, null);
        notificationDao.insertNotification(notification);

        List<Notification> notifications = notificationDao.getNotificationsForUser("user1");
        assertEquals(1, notifications.size());
        assertEquals("Message 1", notifications.get(0).getMessage());
    }

    @Test
    public void testConcurrentInserts() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    Notification notification = new Notification("user1", "Message " + finalI, new SimpleCalendar(2025, 1, 1), (long) finalI, null);
                    notificationDao.insertNotification(notification);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        List<Notification> notifications = notificationDao.getNotificationsForUser("user1");
        assertEquals(5, notifications.size());
    }

    @Test
    public void testReadAndWriteConcurrency() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // Write operation
        executorService.execute(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Notification notification = new Notification("user2", "Message " + i, new SimpleCalendar(2025, 1, 1), (long) i, null);
                    notificationDao.insertNotification(notification);
                }
            } finally {
                latch.countDown();
            }
        });

        // Read operation
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                try {
                    List<Notification> notifications = notificationDao.getNotificationsForUser("user2");
                    assertNotNull(notifications);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(15, TimeUnit.SECONDS);
        executorService.shutdown();

        List<Notification> notifications = notificationDao.getNotificationsForUser("user2");
        assertEquals(5, notifications.size());
    }
}
