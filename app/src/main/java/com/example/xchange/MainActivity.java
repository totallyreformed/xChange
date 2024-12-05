package com.example.xchange;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> categories = initialize_Categories(); // Initialize categories first
    public static ArrayList<User> xChangers = new ArrayList<>();
    public static ArrayList<User> admins = new ArrayList<>();
    public static ArrayList<String> reports = new ArrayList<>();
    public static HashMap<String, Integer> statistics = initialize_Statistics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);
    }

    public static HashMap<String, Integer> initialize_Statistics() {
        HashMap<String, Integer> hs = new HashMap<>();
        hs.put("NUMBER OF ALL DEALS", 0);
        hs.put("NUMBER OF SUCCEED DEALS", 0);
        hs.put("NUMBER OF FAILED DEALS", 0);
        hs.put("NUMBER OF REPORTS", 0);
        hs.put("NUMBER OF CATEGORIES", MainActivity.categories.size());
        for (String cat : categories) {
            hs.put(cat, 0);
        }
        hs.put("NUMBER OF XCHANGERS", MainActivity.xChangers.size());
        return hs;
    }

    public static ArrayList<String> initialize_Categories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Fashion");
        categories.add("Gadgets");
        categories.add("Housing");
        categories.add("Electronics");
        return categories;
    }
}
