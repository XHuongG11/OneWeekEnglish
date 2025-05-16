package com.example.oneweekenglish.activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.LetterAdapter;
import com.example.oneweekenglish.model.LetterItem;

import java.util.ArrayList;
import java.util.List;

public class WordGuessActivity extends AppCompatActivity {

    private List<TextView> underlineTextViews = new ArrayList<>();
    private List<LetterItem> letterItems;
    private LetterAdapter letterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_guess);

        // Dữ liệu thủ công: từ cần đoán và các chữ cái để chọn
        String wordToGuess = "DOG"; // Từ cần đoán
        letterItems = new ArrayList<>();
        letterItems.add(new LetterItem("D"));
        letterItems.add(new LetterItem("E"));
        letterItems.add(new LetterItem("H"));
        letterItems.add(new LetterItem("F"));
        letterItems.add(new LetterItem("O"));
        letterItems.add(new LetterItem("X"));
        letterItems.add(new LetterItem("G"));

        // Tạo gạch dưới động
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        for (int i = 0; i < wordToGuess.length(); i++) {
            View underlineView = LayoutInflater.from(this).inflate(R.layout.underline_view, underlineContainer, false);
            TextView letterText = underlineView.findViewById(R.id.letterText);
            letterText.setOnClickListener(v -> {
                //
            });
            underlineTextViews.add(letterText);
            underlineContainer.addView(underlineView);
        }

        // Thiết lập GridView cho các chữ cái
        GridView letterGridView = findViewById(R.id.letterGridView);
        letterAdapter = new LetterAdapter(letterItems);
        letterGridView.setAdapter(letterAdapter);
        // Sự kiện nhấn cho GridView
        letterGridView.setOnItemClickListener((parent, view, position, id) -> {
            //
        });

        // Sự kiện nhấn cho nút đóng
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            //
        });

        // Sự kiện nhấn cho nút loa
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(v -> {
            //
        });

        // Sự kiện nhấn cho nút NEXT
        ImageButton checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(v -> {
            //
        });
    }
}