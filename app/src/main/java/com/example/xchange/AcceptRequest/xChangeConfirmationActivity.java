// File: com/example/xchange/ExchangeConfirmation/ExchangeConfirmationActivity.java

package com.example.xchange.AcceptRequest;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;

public class xChangeConfirmationActivity extends AppCompatActivity {

    private TextView contactInfoTextView;
    private Request request;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xchange_confirmation);

        contactInfoTextView = findViewById(R.id.contactInfoTextView);

        // Retrieve data from Intent
        request = getIntent().getParcelableExtra("REQUEST");
        currentUser = getIntent().getParcelableExtra("USER");

        if (request == null || currentUser == null) {
            Toast.makeText(this, "Error loading exchange details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayContactInfo();
    }

    private void displayContactInfo() {
        StringBuilder contactInfo = new StringBuilder();

        contactInfo.append("Exchange Accepted!\n\n");

        contactInfo.append("Your Contact Information:\n");
        contactInfo.append("Name: ").append(currentUser.getUsername()).append("\n");
        contactInfo.append("Email: ").append(currentUser.getEmail()).append("\n");
        contactInfo.append("Location: ").append(currentUser.getLocation()).append("\n\n");

        contactInfo.append("Counterparty's Contact Information:\n");
        contactInfo.append("Name: ").append(request.getRequester().getUsername()).append("\n");
        contactInfo.append("Email: ").append(request.getRequester().getEmail()).append("\n");
        contactInfo.append("Location: ").append(request.getRequester().getLocation()).append("\n");

        contactInfoTextView.setText(contactInfo.toString());
    }
}
