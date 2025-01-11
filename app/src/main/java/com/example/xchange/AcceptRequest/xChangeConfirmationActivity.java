package com.example.xchange.AcceptRequest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.Counteroffer;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;
import com.example.xchange.MainActivity.MainActivity;

public class xChangeConfirmationActivity extends AppCompatActivity {

    private TextView exchangeStatusTextView;
    private TextView yourContactInfoHeader, yourContactInfoTextView;
    private TextView counterpartyContactInfoHeader, counterpartyContactInfoTextView;
    private Button backButton;
    private UserRepository userRepository;
    private User currentUser;
    private Request request;
    private Counteroffer counteroffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xchange_confirmation);

        // Map the new views based on the updated XML
        exchangeStatusTextView = findViewById(R.id.exchangeStatusTextView);
        yourContactInfoHeader = findViewById(R.id.yourContactInfoHeader);
        yourContactInfoTextView = findViewById(R.id.yourContactInfoTextView);
        counterpartyContactInfoHeader = findViewById(R.id.counterpartyContactInfoHeader);
        counterpartyContactInfoTextView = findViewById(R.id.counterpartyContactInfoTextView);
        backButton = findViewById(R.id.backButton);

        // Back button navigates to MainActivity
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(xChangeConfirmationActivity.this, MainActivity.class);
            intent.putExtra("USER", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Retrieve intent data
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
        // Set the exchange status
        exchangeStatusTextView.setText("Exchange Accepted!");

        // Populate your contact information
        if (currentUser.getUsername().equals(xchange.getOfferer().getUsername())) {
            yourContactInfoHeader.setText("Your Contact Information:");
            String yourContactInfo = "Name: " + xchange.getOfferer().getUsername() + "\n"
                    + "Email: " + xchange.getOfferer().getEmail() + "\n"
                    + "Location: " + xchange.getOfferer().getLocation();
            yourContactInfoTextView.setText(yourContactInfo);

            counterpartyContactInfoHeader.setText("Counterparty's Contact Information:");
            String counterpartyContactInfo = "Name: " + xchange.getOfferee().getUsername() + "\n"
                    + "Email: " + xchange.getOfferee().getEmail() + "\n"
                    + "Location: " + xchange.getOfferee().getLocation();
            counterpartyContactInfoTextView.setText(counterpartyContactInfo);
        } else {
            yourContactInfoHeader.setText("Your Contact Information:");
            String yourContactInfo = "Name: " + xchange.getOfferee().getUsername() + "\n"
                    + "Email: " + xchange.getOfferee().getEmail() + "\n"
                    + "Location: " + xchange.getOfferee().getLocation();
            yourContactInfoTextView.setText(yourContactInfo);

            counterpartyContactInfoHeader.setText("Counterparty's Contact Information:");
            String counterpartyContactInfo = "Name: " + xchange.getOfferer().getUsername() + "\n"
                    + "Email: " + xchange.getOfferer().getEmail() + "\n"
                    + "Location: " + xchange.getOfferer().getLocation();
            counterpartyContactInfoTextView.setText(counterpartyContactInfo);
        }
    }
}
