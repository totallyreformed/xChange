package com.example.xchange.ProfileData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.RequestsAdapter;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {

    private RequestsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Request> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the current user and request type from the intent
        Intent intent = getIntent();
        User currentUser = intent.getParcelableExtra("USER");
        String requestType = intent.getStringExtra("REQUEST_TYPE");
        ArrayList<Request> requestsFromIntent = intent.getParcelableArrayListExtra("REQUESTS");

        if (currentUser == null || requestType == null) {
            Toast.makeText(this, "Invalid user or request type", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new RequestsAdapter(requestList, currentUser);
        recyclerView.setAdapter(adapter);

        if (requestsFromIntent != null && !requestsFromIntent.isEmpty()) {
            requestList.addAll(requestsFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No requests found", Toast.LENGTH_SHORT).show();
        }

        loadRequests(requestType, currentUser);
    }

    private void loadRequests(String requestType, User currentUser) {
        if ("SENT".equals(requestType)) {
            Toast.makeText(this, "Displaying sent requests...", Toast.LENGTH_SHORT).show();
        } else if ("RECEIVED".equals(requestType)) {
            Toast.makeText(this, "Displaying received requests...", Toast.LENGTH_SHORT).show();
        }
    }
}
