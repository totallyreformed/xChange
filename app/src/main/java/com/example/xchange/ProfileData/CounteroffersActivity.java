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

        initializeUI();
        adjustSystemBars();
        setupRecyclerView();
        handleIntentData();
        setupBackButton();
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.requestsRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void adjustSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CounteroffersAdapter(counterofferList, this::onCounterofferClicked);
        recyclerView.setAdapter(adapter);
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        String counterofferType = intent.getStringExtra("REQUEST_TYPE");
        ArrayList<Counteroffer> counteroffersFromIntent = intent.getParcelableArrayListExtra("COUNTEROFFERS");

        if (currentUser == null || counterofferType == null) {
            showToast("Invalid user or counteroffer type");
            finish();
            return;
        }

        populateCounteroffers(counteroffersFromIntent);
        displayTypeMessage(counterofferType);
    }

    private void populateCounteroffers(ArrayList<Counteroffer> counteroffersFromIntent) {
        if (counteroffersFromIntent != null && !counteroffersFromIntent.isEmpty()) {
            counterofferList.addAll(counteroffersFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            showToast("No counteroffers found");
        }
    }

    private void displayTypeMessage(String counterofferType) {
        if ("COUNTER_OFFERS_SENT".equals(counterofferType)) {
            showToast("Displaying sent counteroffers...");
        } else if ("COUNTER_OFFERS_RECEIVED".equals(counterofferType)) {
            showToast("Displaying received counteroffers...");
        }
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void onCounterofferClicked(Counteroffer counteroffer) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("ITEM_ID", counteroffer.getRequestedItem().getItemId());
        intent.putExtra("USER", currentUser);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}