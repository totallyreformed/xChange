package com.example.xchange.database.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.Category;
import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.xChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CounterofferDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private CounterofferDao counterofferDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        counterofferDao = database.CounteofferDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveCounteroffer() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "Gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Request request = new Request(offeree, offerer, null, offeredItem, new SimpleCalendar(2025, 1, 1));
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, offerer, offeree);

        // Insert the counteroffer and retrieve the generated ID
        long generatedId = counterofferDao.insertCounteroffer(counteroffer);

        // Assign the generated ID to the counteroffer object
        counteroffer.setCounterofferId(generatedId);

        // Retrieve the counteroffers from the database
        List<Counteroffer> retrieved = counterofferDao.getAllCounteroffersSync();

        // Perform assertions
        assertNotNull(retrieved);
        assertEquals(1, retrieved.size());
        assertEquals(counteroffer.getCounterofferId(), retrieved.get(0).getCounterofferId());
    }


    @Test
    public void testUpdateCounteroffer() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "Gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Request request = new Request(offeree, offerer, null, offeredItem, new SimpleCalendar(2025, 1, 1));
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, offerer, offeree);

        counterofferDao.insertCounteroffer(counteroffer);

        Counteroffer retrieved = counterofferDao.getAllCounteroffersSync().get(0);
        assertNotNull(retrieved);
        retrieved.setActive(false);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                counterofferDao.updateCounteroffer(retrieved);
                latch.countDown();
            } catch (Exception e) {
                fail("Update failed: " + e.getMessage());
            }
        });

        latch.await();

        Counteroffer updated = counterofferDao.getAllCounteroffersSync().get(0);
        assertNotNull(updated);
        assertFalse(updated.isActive());

        executor.shutdown();
    }

    @Test
    public void testDeleteCounteroffer() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "Gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Request request = new Request(offeree, offerer, null, offeredItem, new SimpleCalendar(2025, 1, 1));
        Counteroffer counteroffer = new Counteroffer(request, offeredItem, offerer, offeree);

        counterofferDao.insertCounteroffer(counteroffer);
        List<Counteroffer> counteroffers = counterofferDao.getAllCounteroffersSync();
        assertEquals(1, counteroffers.size());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                counterofferDao.deleteCounteroffer(counteroffers.get(0));
                latch.countDown();
            } catch (Exception e) {
                fail("Delete failed: " + e.getMessage());
            }
        });

        latch.await();

        assertTrue(counterofferDao.getAllCounteroffersSync().isEmpty());

        executor.shutdown();
    }
}
