package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Request;

import java.util.List;

@Dao
public interface RequestDao {

    // Insert a new request
    @Insert
    long insertRequest(Request request);

    /**
     * Retrieves all active requests for a specific item.
     *
     * @param itemId The ID of the item.
     * @return LiveData list of active requests for the item.
     */
    @Query("SELECT * FROM requests WHERE requested_item = :itemId AND active = 1")
    LiveData<List<Request>> getActiveRequestsForItem(long itemId);

    /**
     * Counts the number of active requests for a given item.
     *
     * @param itemId The ID of the item.
     * @return The count of active requests.
     */
    @Query("SELECT COUNT(*) FROM requests WHERE requested_item = :itemId AND active = 1")
    int countActiveRequestsForItem(long itemId);

    // Retrieve all requests
    @Query("SELECT * FROM requests")
    List<Request> getAllRequests();

    // Retrieve a request by its ID
    @Query("SELECT * FROM requests WHERE requestId = :requestId LIMIT 1")
    LiveData<Request> getRequestById(long requestId);

    @Query("SELECT * FROM requests WHERE requester = :username OR requestee = :username")
    List<Request> findRequestsByUsername(String username);

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
    List<Request> getAllReceivedRequestsAdmin();

    @Query("SELECT * FROM requests")
    List<Request> getAllSentRequestsAdmin();

    // Retrieve all received active requests for a specific user
    @Query("SELECT * FROM requests WHERE requestee = :username AND active = 1")
    LiveData<List<Request>> getAllReceivedRequests(String username);

    // Retrieve all sent active requests for a specific user
    @Query("SELECT * FROM requests WHERE requester = :username AND active = 1")
    LiveData<List<Request>> getAllSentRequests(String username);

    @Query("SELECT COUNT(*) FROM requests")
    LiveData<Integer> getRequestsSentCount();

    @Query("SELECT COUNT(*) FROM requests")
    LiveData<Integer> getRequestsReceivedCount();
    @Delete
    void deleteRequest(Request request);

    // Update a request
    @Update
    void updateRequest(Request request);

}