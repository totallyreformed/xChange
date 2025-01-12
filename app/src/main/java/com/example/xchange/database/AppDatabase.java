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
import com.example.xchange.Notification;
import com.example.xchange.Rating;
import com.example.xchange.Request;
import com.example.xchange.R;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;

import com.example.xchange.database.dao.CounterofferDao;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.NotificationDao;
import com.example.xchange.database.dao.RatingDao;
import com.example.xchange.database.dao.RequestDao;
import com.example.xchange.database.dao.UserDao;
import com.example.xchange.database.dao.xChangeDao;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * Database class for managing the xChange application's database.
 * Uses Room to manage entities and DAOs for interacting with the database.
 * Includes a prepopulate callback to initialize the database with default data.
 */
@Database(entities = {User.class, Item.class, Request.class, Counteroffer.class, xChange.class, Notification.class, Rating.class}, version = 2, exportSchema = false)
@TypeConverters({CalendarConverter.class, ImageConverter.class, CategoryConverter.class, RequestConverter.class, CounterofferConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    /**
     * DAO for managing user-related operations.
     *
     * @return UserDao instance.
     */
    public abstract UserDao userDao();

    /**
     * DAO for managing item-related operations.
     *
     * @return ItemDao instance.
     */
    public abstract ItemDao itemDao();

    /**
     * DAO for managing request-related operations.
     *
     * @return RequestDao instance.
     */
    public abstract RequestDao requestDao();

    /**
     * DAO for managing counteroffer-related operations.
     *
     * @return CounterofferDao instance.
     */
    public abstract CounterofferDao CounteofferDao();

    /**
     * DAO for managing xChange-related operations.
     *
     * @return xChangeDao instance.
     */
    public abstract xChangeDao xChangeDao();

    /**
     * DAO for managing notification-related operations.
     *
     * @return NotificationDao instance.
     */
    public abstract NotificationDao notificationDao();

    /**
     * DAO for managing rating-related operations.
     *
     * @return RatingDao instance.
     */
    public abstract RatingDao ratingDao();

    /**
     * Singleton instance for accessing the database.
     *
     * @param context Application context.
     * @return The singleton instance of AppDatabase.
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "xchange_database_v14.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(prepopulateCallback) // Add prepopulate callback
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback for prepopulating the database with initial data.
     */
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

    /**
     * Static helper method for retrieving the ItemDao instance.
     *
     * @return ItemDao instance.
     */
    public static ItemDao getItemDao() {
        return INSTANCE.itemDao();
    }

    /**
     * Static helper method for retrieving the UserDao instance.
     *
     * @return UserDao instance.
     */
    public static UserDao getUserDao() {
        return INSTANCE.userDao();
    }

    /**
     * Static helper method for retrieving the xChangeDao instance.
     *
     * @return xChangeDao instance.
     */
    public static xChangeDao getxChangeDao() { return INSTANCE.xChangeDao(); }

    /**
     * Static helper method for retrieving the RequestDao instance.
     *
     * @return RequestDao instance.
     */
    public static RequestDao getRequestDao() {
        return INSTANCE.requestDao();
    }

    /**
     * Static helper method for retrieving the CounterofferDao instance.
     *
     * @return CounterofferDao instance.
     */
    public static CounterofferDao getCounterofferDao(){return INSTANCE.CounteofferDao();}

    /**
     * Static helper method for retrieving the NotificationDao instance.
     *
     * @return NotificationDao instance.
     */
    public static NotificationDao getNotificationDao() { return INSTANCE.notificationDao(); }

    /**
     * Static helper method for retrieving the RatingDao instance.
     *
     * @return RatingDao instance.
     */
    public static RatingDao getRatingDao() { return INSTANCE.ratingDao(); }
}