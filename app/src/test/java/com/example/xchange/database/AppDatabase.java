package com.example.xchange.database;

import com.example.xchange.Item;

public class AppDatabase {
    // Create a singleton instance for tests.
    private static final AppDatabase INSTANCE = new AppDatabase();

    // Provide a dummy implementation of ItemDao.
    private final ItemDao dummyItemDao = new ItemDao() {
        @Override
        public long insertItem(Item item) {
            // Return a dummy id.
            return 1L;
        }
    };

    private AppDatabase() {}

    public static AppDatabase getInstance() {
        return INSTANCE;
    }

    public static ItemDao getItemDao() {
        return INSTANCE.dummyItemDao;
    }
}
