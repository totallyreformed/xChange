package com.example.xchange.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.xchange.Item;
import com.example.xchange.User;

import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.database.dao.UserDao;
import com.example.xchange.xChanger;

import java.util.concurrent.Executors;

@Database(entities = {User.class,Item.class}, version = 2, exportSchema = false)
@TypeConverters({CalendarConverter.class, ImageConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ItemDao ItemDao();

    public static AppDatabase getInstance(final Context context) {
        context.deleteDatabase("xChange_db");
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
            Executors.newSingleThreadExecutor().execute(() -> {
                UserDao dao = INSTANCE.userDao();
                ItemDao dao_item= (ItemDao) INSTANCE.ItemDao();

                // Add default users
                dao.insertUser(new User("admin", "admin@example.com", null, "IamtheAdmin", "HQ", "admin"));
                xChanger testXchanger=new xChanger("testXChanger", "xchanger@example.com", null, "password123", "NY");
                dao.insertUser(testXchanger);
                xChanger swkratis=new xChanger("swkratis","swkratis@example.com",null,"swk","Peiraeus");
                dao.insertUser(swkratis);


               swkratis.UploadItem("iphone11","Iphone 11 bought back in 2022, it works perfectly","Technology","Like new",null);


            });
        }
    };
}
