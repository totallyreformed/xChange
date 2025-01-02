package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.xchange.Item;
import com.example.xchange.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT COUNT(*) FROM items WHERE xchanger = :username")
    int getItemCountByUsername(String username);

    @Query("SELECT * FROM items WHERE xchanger = :username")
    List<Item> getItemsByUsername(String username);

    @Query("SELECT * FROM users WHERE user_type = 'xChanger'")
    List<User> getAllXChangers();
    //test

    @Query("SELECT * FROM users WHERE user_type = 'admin'")
    List<User> getAllAdmins();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User loginxChanger(String username, String password);

    // Login for admin using username and password
    @Query("SELECT * FROM users WHERE username = :username AND password = :password AND user_type = 'IamtheAdmin'")
    User loginadmin(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    LiveData<User> findByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername_initial(String username);
}
