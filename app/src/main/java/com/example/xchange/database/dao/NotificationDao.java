package com.example.xchange.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;

import com.example.xchange.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insertNotification(Notification notification);

    @Query("SELECT * FROM notifications WHERE username = :username")
    List<Notification> getNotificationsForUser(String username);

    @Query("DELETE FROM notifications WHERE username = :username")
    void deleteNotificationsForUser(String username);
}
