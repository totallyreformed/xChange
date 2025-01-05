package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Counteroffer;
import com.example.xchange.Request;
import com.example.xchange.xChanger;

import java.util.List;

@Dao
public interface CounterofferDao {

    // Insert a single Counteroffer
    @Insert
    long insertCounteroffer(Counteroffer counteroffer);

    // Insert multiple Counteroffers
    @Insert
    void insertCounteroffers(List<Counteroffer> counteroffers);

    // Update a Counteroffer
    @Update
    void updateCounteroffer(Counteroffer counteroffer);

    // Delete a Counteroffer
    @Delete
    void deleteCounteroffer(Counteroffer counteroffer);
    @Query("DELETE FROM counteroffer")
    void deleteAll();

    // Retrieve a Counteroffer by ID
    @Query("SELECT * FROM counteroffer WHERE counteroffer_id = :id")
    LiveData<Counteroffer> getCounterofferById(long id);

    @Query("SELECT * FROM counteroffer WHERE counterofferee = :counteroffereeUsername AND active = 1")
    LiveData<List<Counteroffer>> getActiveCounteroffersForUser(String counteroffereeUsername);


    // Retrieve all Counteroffers linked to a specific Request
    @Query("SELECT * FROM counteroffer WHERE request = :request")
    LiveData<List<Counteroffer>> getCounteroffersByRequest(Request request);

    // Retrieve all Counteroffers
    @Query("SELECT * FROM counteroffer")
    LiveData<List<Counteroffer>> getAllCounteroffers();

    // Mark a Counteroffer as inactive
    @Query("UPDATE counteroffer SET active = 0 WHERE counteroffer_id = :id")
    void markCounterofferAsInactive(long id);
}
