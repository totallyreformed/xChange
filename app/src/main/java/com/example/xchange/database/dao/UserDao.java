package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.xChange;
import com.example.xchange.Rating;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT COUNT() FROM items WHERE xchanger = :username")
    int getItemCountByUsername(String username);

    @Query("SELECT * FROM items WHERE xchanger = :username")
    List<Item> getItemsByUsername(String username);

    @Query("SELECT * FROM users WHERE user_type = 'xChanger'")
    List<User> getAllXChangers();

    @Query("SELECT * FROM users WHERE user_type = 'admin'")
    List<User> getAllAdmins();

    // Add a universal login query
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User loginUser(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    LiveData<User> findByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername_initial(String username);

    @Query("SELECT COUNT() FROM requests")
    int getTotalRequests();

    @Query("SELECT COUNT() FROM requests WHERE active = 0")
    int getTotalExchanges();

    @Query("SELECT COUNT() FROM items")
    int getTotalItems();

    // Modify the getTotalCategories query to use the correct column name 'itemCategory'
    @Query("SELECT COUNT(DISTINCT item_category) FROM items")
    int getTotalCategories();


    @Update
    void updateUser(User user);

    @Query("SELECT COUNT() FROM users")
    int getTotalUsers();
    @Delete
    void deleteUser(User user);
}