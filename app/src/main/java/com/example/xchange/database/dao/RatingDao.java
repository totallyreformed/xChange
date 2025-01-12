package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.xChange;
import com.example.xchange.Rating;

@Dao
public interface RatingDao {
    @Insert
    void insertRating(Rating rating);

    @Query("SELECT IFNULL(AVG(rating), 0) FROM ratings WHERE ratee = :username")
    float getAverageRating(String username);

    // Returns the count of ratings for a given user (as a ratee) identified by username.
    @Query("SELECT COUNT(*) FROM ratings WHERE ratee = :username")
    int getTotalRatings(String username);
}
