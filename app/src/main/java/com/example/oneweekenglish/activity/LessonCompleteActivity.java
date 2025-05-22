package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oneweekenglish.R;

public class LessonCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_complete);

        // ImageButton "NEXT"
        ImageButton nextButton = findViewById(R.id.nextButton);
        // "BACK TO HOME"
        TextView backToHomeText = findViewById(R.id.txtHome);

        // Sự kiện click cho nút NEXT
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chưa có chức năng
            }
        });

        // Sự kiện click cho TextView BACK TO HOME
        backToHomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonCompleteActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}