package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.xchange.Request;

import java.util.List;

@Dao
public interface RequestDao {

    // Insert a new request
    @Insert
    long insertRequest(Request request);

    // Retrieve all requests
    @Query("SELECT * FROM requests")
    List<Request> getAllRequests();

    // Retrieve a request by its ID
    @Query("SELECT * FROM requests WHERE requestId = :requestId LIMIT 1")
    LiveData<Request> getRequestById(long requestId);

    // Retrieve active requests
    @Query("SELECT * FROM requests WHERE active = 1")
    LiveData<List<Request>> getActiveRequests();

    // Delete a request by its ID
    @Query("DELETE FROM requests WHERE requestId = :requestId")
    void deleteRequestById(long requestId);

    // Delete all requests
    @Query("DELETE FROM requests")
    void deleteAllRequests();

    @Query("SELECT * FROM requests")
    List<Request> getAllReceivedRequests();

    @Query("SELECT * FROM requests")
    List<Request> getAllSentRequests();

    @Query("SELECT COUNT(*) FROM requests")
    LiveData<Integer> getRequestsSentCount();

    @Query("SELECT COUNT(*) FROM requests")
    LiveData<Integer> getRequestsReceivedCount();

}