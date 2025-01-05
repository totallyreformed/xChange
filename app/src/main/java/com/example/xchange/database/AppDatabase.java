package com.example.xchange.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.xchange.Category;
import com.example.xchange.Counteroffer;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.R;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;

import com.example.xchange.database.dao.CounterofferDao;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.RequestDao;
import com.example.xchange.database.dao.UserDao;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Item.class, Request.class, Counteroffer.class}, version = 2, exportSchema = false)
@TypeConverters({CalendarConverter.class, ImageConverter.class, CategoryConverter.class, RequestConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ItemDao itemDao();
    public abstract RequestDao requestDao();
    public abstract CounterofferDao counterofferDao(); // Add this line


    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "xchange_database_v6.db")
                            .fallbackToDestructiveMigration()
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
            Executors.newSingleThreadExecutor().execute(() -> {
                UserDao userDao = INSTANCE.userDao();
                ItemDao itemDao = INSTANCE.itemDao();
                RequestDao requestDao = INSTANCE.requestDao();

                // Admin user
                userDao.insertUser(new User("admin", "admin@example.com", null, "admin", "HQ", "admin"));

                // xChangers
                xChanger testXchanger = new xChanger("testXChanger", "xchanger@example.com", null, "password123", "NY");
                userDao.insertUser(testXchanger);
                xChanger swkratis = new xChanger("swkratis", "swkratis@example.com", null, "swk", "Piraeus");
                userDao.insertUser(swkratis);

                // Items
                swkratis.UploadItem("iphone11", "Iphone 11 bought back in 2022, it works perfectly", Category.TECHNOLOGY, "Like new", null);
                Image airforce = new Image(String.valueOf(R.drawable.testimage), "test");
                ArrayList<Image> images = new ArrayList<>();
                images.add(airforce);

                swkratis.UploadItem(
                        "Airforce1",
                        "White nike's airforce 1, bought 2024",
                        Category.FASHION,
                        "Used",
                        images
                );

                // Requests (Optional prepopulation)
                Request request = new Request(
                        testXchanger,
                        swkratis,
                        new Item(), // Replace with a valid Item
                        new Item(), // Replace with a valid Item
                        new SimpleCalendar(2024, 12, 1)
                );
                requestDao.insertRequest(request);
            });
        }
    };

    public static ItemDao getItemDao() {
        return INSTANCE.itemDao();
    }

    public static UserDao getUserDao() {
        return INSTANCE.userDao();
    }

    public static RequestDao getRequestDao() {
        return INSTANCE.requestDao();
    }
    public static CounterofferDao getCounterofferDao(){return INSTANCE.counterofferDao();}
}