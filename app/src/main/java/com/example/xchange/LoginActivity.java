package com.example.xchange;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.setValue("ta qi perendi");
    }
//    public void handle_inputs(View v){
//        EditText text = findViewById(R.id.Text);
//        String input = text.getText().toString();
//
//        EditText pass = findViewById(R.id.Password);
//        String password = pass.getText().toString();
//        Log.d("info",input);
//        Log.d("info",password);
//    }
}