package com.example.xchange;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.R;
import com.example.xchange.User;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve User object from Intent
        User user = getIntent().getParcelableExtra("USER");
//        xChanger xchanger = (xChanger) user;
        if (user != null) {
            // Display username and email
            TextView usernameTextView = findViewById(R.id.usernameTextView);
            TextView emailTextView = findViewById(R.id.emailTextView);

            usernameTextView.setText("Username: " + user.getUsername());
//            emailTextView.setText(xchanger.getItems().get(0).getItemCategory());



            Log.d("MainActivity", "User: " + user.getUsername() + ", Email: " + user.getEmail());
        } else {
            Log.e("MainActivity", "No user data received");
        }
    }
}
