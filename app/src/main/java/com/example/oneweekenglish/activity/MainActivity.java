package com.example.oneweekenglish.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.dao.UserDAO;
import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView fullName = findViewById(R.id.textViewNameOfUser);
        fullName.setText(getNameOfUser());
    }
    private String getNameOfUser(){
        SharedPreferences prefs = getSharedPreferences("CurentUser", MODE_PRIVATE);
        String fullName = prefs.getString("fullName", null);
        return fullName != null ? fullName : "";
    }
}