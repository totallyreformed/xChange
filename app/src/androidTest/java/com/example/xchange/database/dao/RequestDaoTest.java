package com.example.xchange.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Request;
import com.example.xchange.Item;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.xChanger;
import com.example.xchange.Category;
import com.example.xchange.Image;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RequestDaoTest {

    private AppDatabase database;
    private RequestDao requestDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        requestDao = database.requestDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieveRequest() throws InterruptedException {
        // Create valid Item instances
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("path/to/image", "Description"));
        Item offeredItem = new Item("Requester", "Laptop", "A powerful laptop", Category.TECHNOLOGY, "Like new", images);
        Item requestedItem = new Item("Requestee", "Smartphone", "A modern smartphone", Category.TECHNOLOGY, "Used", images);

        // Create a request
        xChanger requester = new xChanger("Requester", "requester@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location");
        xChanger requestee = new xChanger("Requestee", "requestee@example.com", new SimpleCalendar(2024, 12, 2), "password", "Location");
        Request request = new Request(requester, requestee, offeredItem, requestedItem, new SimpleCalendar(2024, 12, 1));

        long requestId = requestDao.insertRequest(request);
        assertNotNull(requestId);

        // Retrieve the request
        LiveData<Request> retrievedRequestLiveData = requestDao.getRequestById(requestId);
        Request retrievedRequest = getValue(retrievedRequestLiveData);
        assertNotNull(retrievedRequest);
        assertEquals(request.getRequester().getUsername(), retrievedRequest.getRequester().getUsername());
    }

    @Test
    public void testGetAllRequests() throws InterruptedException {
        // Create and insert multiple requests
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("path/to/image", "Description"));

        Item item1 = new Item("Requester1", "Laptop", "A powerful laptop", Category.TECHNOLOGY, "Like new", images);
        Item item2 = new Item("Requester2", "Tablet", "A modern tablet", Category.TECHNOLOGY, "Used", images);

        Request request1 = new Request(
                new xChanger("Requester1", "requester1@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location"),
                new xChanger("Requestee1", "requestee1@example.com", new SimpleCalendar(2024, 12, 2), "password", "Location"),
                item1,
                item2,
                new SimpleCalendar(2024, 12, 1)
        );

        Request request2 = new Request(
                new xChanger("Requester2", "requester2@example.com", new SimpleCalendar(2024, 12, 3), "password", "Location"),
                new xChanger("Requestee2", "requestee2@example.com", new SimpleCalendar(2024, 12, 4), "password", "Location"),
                item2,
                item1,
                new SimpleCalendar(2024, 12, 3)
        );

        requestDao.insertRequest(request1);
        requestDao.insertRequest(request2);

        // Retrieve all requests
        List<Request> requests = requestDao.getAllRequests();
        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testDeleteRequest() throws InterruptedException {
        // Create valid Item instances
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("path/to/image", "Description"));
        Item offeredItem = new Item("Requester", "Laptop", "A powerful laptop", Category.TECHNOLOGY, "Like new", images);
        Item requestedItem = new Item("Requestee", "Smartphone", "A modern smartphone", Category.TECHNOLOGY, "Used", images);

        // Create a request
        Request request = new Request(
                new xChanger("Requester", "requester@example.com", new SimpleCalendar(2024, 12, 1), "password", "Location"),
                new xChanger("Requestee", "requestee@example.com", new SimpleCalendar(2024, 12, 2), "password", "Location"),
                offeredItem,
                requestedItem,
                new SimpleCalendar(2024, 12, 1)
        );

        long requestId = requestDao.insertRequest(request);

        // Delete the request
        requestDao.deleteRequestById(requestId);

        // Verify deletion
        LiveData<Request> deletedRequestLiveData = requestDao.getRequestById(requestId);
        Request deletedRequest = getValue(deletedRequestLiveData);
        assertNull(deletedRequest);
    }

    // Helper to get LiveData value synchronously
    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] data = new Object[1];
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) data[0];
    }
}
