package com.example.xchange.database.dao;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.*;
import com.example.xchange.database.AppDatabase;

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
public class xChangeDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private xChangeDao xChangeDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        xChangeDao = database.xChangeDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveXChange() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "A gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Item requestedItem = new Item("OffereeUser", "Phone", "A smartphone", Category.TECHNOLOGY, "Good", new ArrayList<>());
        Request request = new Request(offerer, offeree, offeredItem, requestedItem, null);
        xChange xchange = new xChange(request, null);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                xChangeDao.insertXChange(xchange);
                latch.countDown();
            } catch (Exception e) {
                fail("Insert failed: " + e.getMessage());
            }
        });

        latch.await();

        List<xChange> retrievedXChanges = xChangeDao.getAllXChangesSync();
        assertNotNull(retrievedXChanges);
        assertEquals(1, retrievedXChanges.size());
        assertEquals(xchange.getFinalizedId(), retrievedXChanges.get(0).getFinalizedId());

        executor.shutdown();
    }

    @Test
    public void testUpdateXChange() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "A gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Item requestedItem = new Item("OffereeUser", "Phone", "A smartphone", Category.TECHNOLOGY, "Good", new ArrayList<>());
        Request request = new Request(offerer, offeree, offeredItem, requestedItem, null);
        xChange xchange = new xChange(request, null);

        xChangeDao.insertXChange(xchange);
        List<xChange> xChanges = xChangeDao.getAllXChangesSync();
        assertFalse(xChanges.isEmpty());

        xChange retrievedXChange = xChanges.get(0);
        retrievedXChange.setDealStatus("Completed");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                xChangeDao.updateXChange(retrievedXChange);
                latch.countDown();
            } catch (Exception e) {
                fail("Update failed: " + e.getMessage());
            }
        });

        latch.await();

        xChange updatedXChange = xChangeDao.getAllXChangesSync().get(0);
        assertEquals("Completed", updatedXChange.getDealStatus());

        executor.shutdown();
    }

    @Test
    public void testDeleteXChange() throws InterruptedException {
        xChanger offerer = new xChanger("OffererUser", "offerer@example.com", null, "password", "Location1");
        xChanger offeree = new xChanger("OffereeUser", "offeree@example.com", null, "password", "Location2");
        Item offeredItem = new Item("OffererUser", "Laptop", "A gaming laptop", Category.TECHNOLOGY, "Like new", new ArrayList<>());
        Item requestedItem = new Item("OffereeUser", "Phone", "A smartphone", Category.TECHNOLOGY, "Good", new ArrayList<>());
        Request request = new Request(offerer, offeree, offeredItem, requestedItem, null);
        xChange xchange = new xChange(request, null);

        xChangeDao.insertXChange(xchange);
        List<xChange> xChanges = xChangeDao.getAllXChangesSync();
        assertEquals(1, xChanges.size());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                xChangeDao.deleteXChange(xChanges.get(0));
                latch.countDown();
            } catch (Exception e) {
                fail("Delete failed: " + e.getMessage());
            }
        });

        latch.await();

        assertTrue(xChangeDao.getAllXChangesSync().isEmpty());

        executor.shutdown();
    }
}
