package com.example.xchange.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.CalendarConverter;

import com.example.xchange.database.dao.UserDao;

import java.util.concurrent.Executors;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters({CalendarConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "xChange_db")
                            .addCallback(prepopulateCallback) // Add prepopulate callback
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback to prepopulate the database
    private static final Callback prepopulateCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insert default users using the DAO
            Executors.newSingleThreadExecutor().execute(() -> {
                UserDao dao = INSTANCE.userDao();

                // Add default users
                dao.insertUser(new User("admin", "admin@example.com", new SimpleCalendar(2024,18,12), "IamtheAdmin", "HQ", "admin"));
                dao.insertUser(new User("testXChanger", "xchanger@example.com", new SimpleCalendar(2024,18,12), "password123", "NY", "xChanger"));
            });
        }
    };
}
