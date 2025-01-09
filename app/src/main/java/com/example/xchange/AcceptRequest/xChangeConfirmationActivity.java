package com.example.xchange.AcceptRequest;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.R;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;

public class xChangeConfirmationActivity extends AppCompatActivity {

    private TextView contactInfoTextView;
    private UserRepository userRepository;
    private xChanger currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xchange_confirmation);

        contactInfoTextView = findViewById(R.id.contactInfoTextView);

        long xChangeId = getIntent().getLongExtra("XCHANGE_ID", -1);
        currentUser = getIntent().getParcelableExtra("USER");

        if (xChangeId == -1 || currentUser == null) {
            Toast.makeText(this, "Error loading exchange details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch xChange details
        UserRepository userRepository = new UserRepository(getApplicationContext());
        userRepository.getXChangeById(xChangeId).observe(this, xChange -> {
            if (xChange != null) {
                displayContactInfo(xChange);
            } else {
                Toast.makeText(this, "Error loading exchange details.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayContactInfo(xChange xchange) {
        StringBuilder contactInfo = new StringBuilder();

        contactInfo.append("Exchange Accepted!\n\n");

        contactInfo.append("Your Contact Information:\n");
        contactInfo.append("Name: ").append(xchange.getOfferer().getUsername()).append("\n");
        contactInfo.append("Email: ").append(xchange.getOfferer().getEmail()).append("\n");
        contactInfo.append("Location: ").append(xchange.getOfferer().getLocation()).append("\n\n");

        contactInfo.append("Counterparty's Contact Information:\n");
        contactInfo.append("Name: ").append(xchange.getOfferee().getUsername()).append("\n");
        contactInfo.append("Email: ").append(xchange.getOfferee().getEmail()).append("\n");
        contactInfo.append("Location: ").append(xchange.getOfferee().getLocation()).append("\n");

        contactInfoTextView.setText(contactInfo.toString());
    }
}
