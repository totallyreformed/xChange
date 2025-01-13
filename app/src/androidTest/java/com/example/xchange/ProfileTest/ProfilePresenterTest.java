package com.example.xchange.ProfileTest;

import static org.junit.Assert.*;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;

import com.example.xchange.Counteroffer;
import com.example.xchange.Item;
import com.example.xchange.Profile.ProfilePresenter;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProfilePresenterTest {

    private ProfilePresenter presenter;
    private MockProfileView mockView;
    private User testUser;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        testUser = new User("testUser", "test@example.com", null, "password", "Test City", "xChanger");
        mockView = new MockProfileView();
        userRepository = new UserRepository(context);
        presenter = new ProfilePresenter(context, testUser, mockView);
    }

    @After
    public void tearDown() {
        userRepository.shutdownExecutor();
    }

    @Test
    public void testLoadProfileData() {
        presenter.loadProfileData();
        // Wait for async calls
        sleepThread();
        assertNotNull(mockView.loadedUser);
        assertNotNull(mockView.loadedStats);
    }

    @Test
    public void testLoadUserItems() {
        presenter.loadUserItems();
        sleepThread();
        assertNotNull(mockView.loadedItems);
    }

    @Test
    public void testLoadRequestsCount() {
        presenter.loadRequestsCount();
        sleepThread();
        assertNotNull(mockView.sentRequestsCount);
        assertNotNull(mockView.receivedRequestsCount);
    }

    @Test
    public void testLoadSentRequests() {
        presenter.loadSentRequests();
        sleepThread();
        assertNotNull(mockView.sentRequests);
    }

    @Test
    public void testLoadReceivedRequests() {
        presenter.loadReceivedRequests();
        sleepThread();
        assertNotNull(mockView.receivedRequests);
    }

    @Test
    public void testLoadCounterOffersCount() {
        presenter.loadCounterOffersCount();
        sleepThread();
        assertNotNull(mockView.sentCounterOffersCount);
        assertNotNull(mockView.receivedCounterOffersCount);
    }

    @Test
    public void testLoadUserXChanges() {
        presenter.loadUserXChanges();
        sleepThread();
        assertNotNull(mockView.loadedXChanges);
    }

    @Test
    public void testLoadTotalExchanges() {
        presenter.loadTotalExchanges();
        sleepThread();
        assertNotNull(mockView.totalExchangesCount);
    }

    @Test
    public void testLoadUserRating() {
        presenter.loadUserRating();
        sleepThread();
        assertNotNull(mockView.userRating);
    }

    private void sleepThread() {
        try {
            Thread.sleep(1000); // Allow time for background threads to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class MockProfileView implements ProfilePresenter.ProfileView {

        User loadedUser;
        String loadedStats;
        List<Item> loadedItems;
        int sentRequestsCount;
        int receivedRequestsCount;
        List<Request> sentRequests;
        List<Request> receivedRequests;
        int sentCounterOffersCount;
        int receivedCounterOffersCount;
        List<Counteroffer> sentCounterOffers;
        List<Counteroffer> receivedCounterOffers;
        List<xChange> loadedXChanges;
        int totalExchangesCount;
        String userRating;

        @Override
        public void onProfileDataLoaded(User user, String stats) {
            this.loadedUser = user;
            this.loadedStats = stats;
        }

        @Override
        public void onProfileDataFailed(String message) {
            fail("Profile data loading failed: " + message);
        }

        @Override
        public void onUserItemsLoaded(List<Item> items) {
            this.loadedItems = items;
        }

        @Override
        public void onUserRatingLoaded(String rating) {
            this.userRating = rating;
        }

        @Override
        public void onUserItemsFailed(String message) {
            fail("User items loading failed: " + message);
        }

        @Override
        public void onSentRequestsCountLoaded(int count) {
            this.sentRequestsCount = count;
        }

        @Override
        public void onReceivedRequestsCountLoaded(int count) {
            this.receivedRequestsCount = count;
        }

        @Override
        public void onRequestsCountFailed(String message) {
            fail("Requests count loading failed: " + message);
        }

        @Override
        public void onReceivedRequestsLoaded(List<Request> requests) {
            this.receivedRequests = requests;
        }

        @Override
        public void onSentRequestsLoaded(List<Request> requests) {
            this.sentRequests = requests;
        }

        @Override
        public void onCounterOffersSentCountLoaded(int count) {
            this.sentCounterOffersCount = count;
        }

        @Override
        public void onCounterOffersReceivedCountLoaded(int count) {
            this.receivedCounterOffersCount = count;
        }

        @Override
        public void onCounterOffersCountFailed(String message) {
            fail("Counter-offers count loading failed: " + message);
        }

        @Override
        public void onSentCounterOffersLoaded(List<Counteroffer> counterOffers) {
            this.sentCounterOffers = counterOffers;
        }

        @Override
        public void onReceivedCounterOffersLoaded(List<Counteroffer> counterOffers) {
            this.receivedCounterOffers = counterOffers;
        }

        @Override
        public void onCounterOffersFailed(String message) {
            fail("Counter-offers loading failed: " + message);
        }

        @Override
        public void onTotalExchangesLoaded(int count) {
            this.totalExchangesCount = count;
        }

        @Override
        public void onTotalExchangesFailed(String message) {
            fail("Total exchanges loading failed: " + message);
        }

        @Override
        public void onXChangesLoaded(List<xChange> xChanges) {
            this.loadedXChanges = xChanges;
        }

        @Override
        public void onXChangesFailed(String message) {
            fail("xChanges loading failed: " + message);
        }
    }
}
