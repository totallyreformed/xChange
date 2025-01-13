package com.example.xchange.database.dao;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.xchange.Rating;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.RatingDao;
import com.example.xchange.xChanger;
import org.junit.*;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RatingDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private RatingDao ratingDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        ratingDao = database.ratingDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveRating() {
        Rating rating = new Rating(4.5f, new xChanger("rater1", "rater1@example.com", null, "password", "Location"),
                new xChanger("ratee1", "ratee1@example.com", null, "password", "Location"), null, null);
        ratingDao.insertRating(rating);

        float averageRating = ratingDao.getAverageRating("ratee1");
        int totalRatings = ratingDao.getTotalRatings("ratee1");

        assertEquals(4.5f, averageRating, 0.01);
        assertEquals(1, totalRatings);
    }

    @Test
    public void testConcurrentInserts() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    Rating rating = new Rating(3.0f + finalI, new xChanger("rater" + finalI, "rater" + finalI + "@example.com", null, "password", "Location"),
                            new xChanger("ratee", "ratee@example.com", null, "password", "Location"), null, null);
                    ratingDao.insertRating(rating);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // Corrected assertion
        float averageRating = ratingDao.getAverageRating("ratee");
        int totalRatings = ratingDao.getTotalRatings("ratee");

        assertEquals(5.0f, averageRating, 0.01); // Corrected expected value
        assertEquals(5, totalRatings);
    }

    @Test
    public void testReadAndWriteConcurrency() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // Write operation
        executorService.execute(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Rating rating = new Rating(4.0f, new xChanger("rater", "rater@example.com", null, "password", "Location"),
                            new xChanger("ratee", "ratee@example.com", null, "password", "Location"), null, null);
                    ratingDao.insertRating(rating);
                }
            } finally {
                latch.countDown();
            }
        });

        // Read operation
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                try {
                    float avgRating = ratingDao.getAverageRating("ratee");
                    assertTrue(avgRating >= 0);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(15, TimeUnit.SECONDS);
        executorService.shutdown();

        int totalRatings = ratingDao.getTotalRatings("ratee");
        assertEquals(5, totalRatings);
    }
}
