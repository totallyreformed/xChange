package com.example.xchange.database;

import com.example.xchange.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseTest {

    @Test
    public void testItemDaoNotNull() {
        ItemDao dao = AppDatabase.getItemDao();
        assertNotNull(dao);
        // Our dummy DAO always returns 1L.
        assertEquals(1L, dao.insertItem(null));
    }
}
