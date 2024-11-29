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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> categories=initialize_Categories();
    public static ArrayList<User> xChangers=new ArrayList<>();
    public static ArrayList<User> admins=new ArrayList<>();
    public static ArrayList<String> reports=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    }

    public static ArrayList<String> initialize_Categories() {
        categories=new ArrayList<>();
        categories.add("Fashion");
        categories.add("Gadgets");
        categories.add("Housing");
        return categories;
    }


}