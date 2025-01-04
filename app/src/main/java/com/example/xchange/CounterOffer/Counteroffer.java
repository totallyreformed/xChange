package com.example.xchange.CounterOffer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.R;
import com.example.xchange.Item;
import com.example.xchange.Request;

import java.util.ArrayList;
import java.util.List;

public class Counteroffer extends AppCompatActivity {

    TextView Requester, Requestee, RequestedItem;
    Spinner OfferedItemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_offer);

        // Retrieve the request and list of items passed via the Intent
        Request request = getIntent().getParcelableExtra("REQUEST");
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra("XCHANGER_ITEMS");

        // Initialize TextViews
        Requester = findViewById(R.id.requesterTextView);
        Requestee = findViewById(R.id.requesteeTextView);
        RequestedItem = findViewById(R.id.requestedItemTextView);
        OfferedItemSpinner = findViewById(R.id.offeredItemSpinner);

        // Display request details
        if (request != null) {
            Requester.setText(request.getRequester().getUsername());
            Requestee.setText(request.getRequestee().getUsername());
            RequestedItem.setText(request.getRequestedItem().getItemName());
        }

        // Populate the Spinner with items
        if (items != null && !items.isEmpty()) {
            // Create a list of item names
            List<String> itemNames = new ArrayList<>();
            for (Item item : items) {
                itemNames.add(item.getItemName());
            }

            // Set up Spinner adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            OfferedItemSpinner.setAdapter(adapter);

            // Handle item selection
            OfferedItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                    String selectedItem = itemNames.get(position);
                    Log.d("Counteroffer", "Selected item: " + selectedItem);
                    // Handle the selected item (e.g., store it for further actions)
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case where no item is selected (optional)
                }
            });
        } else {
            Log.e("Counteroffer", "No items passed to activity.");
        }
    }
}
