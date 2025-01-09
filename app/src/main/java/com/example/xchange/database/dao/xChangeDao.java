package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Request;
import com.example.xchange.xChange;

import java.util.List;

@Dao
public interface xChangeDao {

    // Insert a single xChange
    @Insert
    long insertXChange(xChange xchange);

    // Insert multiple xChanges
    @Insert
    void insertXChanges(List<xChange> xchanges);

    // Update a xChange
    @Update
    void updateXChange(xChange xchange);

    // Delete a xChange
    @Delete
    void deleteXChange(xChange xchange);

    // Delete all xChanges
    @Query("DELETE FROM xchanges")
    void deleteAll();

    // Retrieve a xChange by ID
    @Query("SELECT * FROM xchanges WHERE xchange_id = :id")
    LiveData<xChange> getXChangeById(long id);

    // Retrieve xChanger by username
    @Query("SELECT * FROM xchanges WHERE offerer = :username OR offeree = :username")
    List<xChange> getXChangerByUser(String username);

    // Retrieve all xChanges linked to a specific Request
    @Query("SELECT * FROM xchanges WHERE request = :request")
    LiveData<List<xChange>> getXChangesByRequest(Request request);



    // Retrieve all xChanges
    @Query("SELECT * FROM xchanges")
    LiveData<List<xChange>> getAllXChanges();


    @Query("SELECT COUNT(*) FROM xchanges WHERE offerer = :username OR offeree = :username")
    int getUserXChanges(String username);

    // Retrieve all xChanges synchronously
    @Query("SELECT * FROM xchanges")
    List<xChange> getAllXChangesSync();

    // Mark a xChange as inactive by setting deal_status
    @Query("UPDATE xchanges SET deal_status = 'Inactive' WHERE xchange_id = :id")
    void markXChangeAsInactive(long id);

    // Retrieve active xChanges for a user (assuming active means deal_status is null or empty)
    @Query("SELECT * FROM xchanges WHERE (deal_status IS NULL OR deal_status = '') AND (offerer = :username OR offeree = :username)")
    LiveData<List<xChange>> getActiveXChangesForUser(String username);
}
