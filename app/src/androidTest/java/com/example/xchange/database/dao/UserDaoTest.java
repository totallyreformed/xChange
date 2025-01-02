package com.example.xchange.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    private AppDatabase database;
    private UserDao userDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // For testing purposes
                .build();
        userDao = database.userDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testUser", "test@example.com", null, "password123", "TestLocation", "xChanger");
        userDao.insertUser(user);

        List<User> xChangers = userDao.getAllXChangers();
        assertEquals(1, xChangers.size());
        assertEquals("testUser", xChangers.get(0).getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user = new User("uniqueUser", "unique@example.com", null, "password123", "TestLocation", "xChanger");
        userDao.insertUser(user);

        User foundUser = userDao.findByUsername("uniqueUser").getValue();
        assertNotNull(foundUser);
        assertEquals("unique@example.com", foundUser.getEmail());
    }

    @Test
    public void testLoginXChanger_Success() {
        User user = new User("testXChanger", "xchanger@example.com", null, "xPass", "TestLocation", "xChanger");
        userDao.insertUser(user);

        User loginUser = userDao.loginxChanger("testXChanger", "xPass");
        assertNotNull(loginUser);
        assertEquals("xchanger@example.com", loginUser.getEmail());
    }

    @Test
    public void testLoginXChanger_Failure() {
        User user = new User("testXChanger", "xchanger@example.com", null, "xPass", "TestLocation", "xChanger");
        userDao.insertUser(user);

        User loginUser = userDao.loginxChanger("testXChanger", "wrongPass");
        assertNull(loginUser);
    }

    @Test
    public void testLoginAdmin_Success() {
        User admin = new User("adminUser", "admin@example.com", null, "adminPass", "AdminLocation", "IamtheAdmin");
        userDao.insertUser(admin);

        User loginAdmin = userDao.loginadmin("adminUser", "adminPass");
        assertNotNull(loginAdmin);
        assertEquals("admin@example.com", loginAdmin.getEmail());
    }

    @Test
    public void testLoginAdmin_Failure() {
        User admin = new User("adminUser", "admin@example.com", null, "adminPass", "AdminLocation", "IamtheAdmin");
        userDao.insertUser(admin);

        User loginAdmin = userDao.loginadmin("adminUser", "wrongPass");
        assertNull(loginAdmin);
    }

    @Test
    public void testGetAllXChangers() {
        User user1 = new User("xChanger1", "user1@example.com", null, "password1", "Location1", "xChanger");
        User user2 = new User("xChanger2", "user2@example.com", null, "password2", "Location2", "xChanger");
        userDao.insertUser(user1);
        userDao.insertUser(user2);

        List<User> xChangers = userDao.getAllXChangers();
        assertEquals(2, xChangers.size());
    }

    @Test
    public void testGetAllAdmins() {
        User admin1 = new User("admin1", "admin1@example.com", null, "adminPass1", "Location1", "admin");
        User admin2 = new User("admin2", "admin2@example.com", null, "adminPass2", "Location2", "admin");
        userDao.insertUser(admin1);
        userDao.insertUser(admin2);

        List<User> admins = userDao.getAllAdmins();
        assertEquals(2, admins.size());
    }
}
