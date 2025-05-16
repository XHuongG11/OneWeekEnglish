package com.example.oneweekenglish.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oneweekenglish.R;

public class RepeatActivity  extends AppCompatActivity {
    private ImageButton buttonRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repeat);
        buttonRecord = findViewById(R.id.buttonRecord);
    }
}
