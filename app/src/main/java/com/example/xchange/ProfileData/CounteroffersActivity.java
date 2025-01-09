package com.example.xchange.ProfileData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Counteroffer;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.CounteroffersAdapter;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;

public class CounteroffersActivity extends AppCompatActivity {

    private CounteroffersAdapter adapter;
    private RecyclerView recyclerView;
    private List<Counteroffer> counterofferList = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counteroffers);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from Intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        String counterofferType = intent.getStringExtra("REQUEST_TYPE"); // Expecting "COUNTER_OFFERS_SENT" or "COUNTER_OFFERS_RECEIVED"
        ArrayList<Counteroffer> counteroffersFromIntent = intent.getParcelableArrayListExtra("COUNTEROFFERS");

        if (currentUser == null || counterofferType == null) {
            Toast.makeText(this, "Invalid user or counteroffer type", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the adapter with click listener
        adapter = new CounteroffersAdapter(counterofferList, this::onCounterofferClicked);
        recyclerView.setAdapter(adapter);

        // Populate counteroffers
        if (counteroffersFromIntent != null && !counteroffersFromIntent.isEmpty()) {
            counterofferList.addAll(counteroffersFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No counteroffers found", Toast.LENGTH_SHORT).show();
        }

        // Display appropriate message based on type
        if ("COUNTER_OFFERS_SENT".equals(counterofferType)) {
            Toast.makeText(this, "Displaying sent counteroffers...", Toast.LENGTH_SHORT).show();
        } else if ("COUNTER_OFFERS_RECEIVED".equals(counterofferType)) {
            Toast.makeText(this, "Displaying received counteroffers...", Toast.LENGTH_SHORT).show();
        }
    }

    private void onCounterofferClicked(Counteroffer counteroffer) {
        Intent intent = new Intent(this, ItemDetailActivity.class);

        // Pass the required data to ItemDetailActivity
        intent.putExtra("ITEM_ID", counteroffer.getRequestedItem().getItemId());
        intent.putExtra("USER", currentUser);

        startActivity(intent);
    }
}
