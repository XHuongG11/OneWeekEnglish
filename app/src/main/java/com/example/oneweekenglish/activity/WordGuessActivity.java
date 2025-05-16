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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.LetterAdapter;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.model.LetterItem;

import java.util.ArrayList;
import java.util.List;

public class WordGuessActivity extends AppCompatActivity {

    private List<TextView> underlineTextViews = new ArrayList<>();
    private List<LetterItem> letterItems;
    private LetterAdapter letterAdapter;
    private String wordToGuess = "DOG"; // Từ cần đoán
    private int currentUnderlineIndex = 0; // Theo dõi vị trí gạch dưới hiện tại
    private boolean isWordCorrect = false; // Cờ để kiểm tra từ đã đúng chưa
    private static final String TAG = "WordGuessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word_guess);
        Log.d(TAG, "onCreate called");

        // Khởi tạo dữ liệu: từ cần đoán và các chữ cái để chọn
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
                if (isWordCorrect) {
                    return; // Không cho phép xóa nếu từ đã đúng
                }
                TextView textView = (TextView) v;
                String letter = textView.getText().toString();
                if (!letter.isEmpty()) {
                    textView.setText("");
                    for (LetterItem item : letterItems) {
                        if (item.getLetter().equals(letter) && item.isSelected()) {
                            item.setSelected(false);
                            break;
                        }
                    }
                    letterAdapter.notifyDataSetChanged();
                    currentUnderlineIndex--;
                    Log.d(TAG, "Letter removed: " + letter + ", background reverted to letter_background");
                }
            });
            underlineTextViews.add(letterText);
            underlineContainer.addView(underlineView);
        }

        // Thiết lập GridView cho các chữ cái
        GridView letterGridView = findViewById(R.id.letterGridView);
        letterAdapter = new LetterAdapter(letterItems, position -> {
            LetterItem selectedLetter = letterItems.get(position);
            Log.d(TAG, "Fallback: Letter button clicked: " + selectedLetter.getLetter());
            handleLetterClick(selectedLetter);
        });
        letterGridView.setAdapter(letterAdapter);
        letterGridView.setOnItemClickListener((parent, view, position, id) -> {
            LetterItem selectedLetter = letterItems.get(position);
            Log.d(TAG, "GridView: Letter button clicked: " + selectedLetter.getLetter());
            handleLetterClick(selectedLetter);
        });

        // Sự kiện nhấn cho nút đóng
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button clicked");
            finish();
        });

        // Sự kiện nhấn cho nút loa
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Speaker button clicked");
        });

        // Sự kiện nhấn cho nút CHECK
        ImageButton checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(v -> {
            StringBuilder guessedWord = new StringBuilder();
            for (

                    TextView textView : underlineTextViews) {
                guessedWord.append(textView.getText().toString());
            }
            Log.d(TAG, "Guessed word: " + guessedWord);
            if (guessedWord.toString().equals(wordToGuess)) {
                isWordCorrect = true; // Đặt cờ khi từ đúng
                // Chuyển chữ cái thành màu xanh lá
                for (TextView textView : underlineTextViews) {
                    textView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                showGreenNoticeFragment();
            } else {
                for (TextView textView : underlineTextViews) {
                    textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                showRedNoticeFragment();
            }
        });
    }

    private void handleLetterClick(LetterItem selectedLetter) {
        if (isWordCorrect) {
            return; // Không cho phép chọn thêm chữ nếu từ đã đúng
        }
        if (!selectedLetter.isSelected()) {
            if (currentUnderlineIndex < underlineTextViews.size()) {
                underlineTextViews.get(currentUnderlineIndex).setText(selectedLetter.getLetter());
                selectedLetter.setSelected(true);
                letterAdapter.notifyDataSetChanged();
                currentUnderlineIndex++;
                Log.d(TAG, "Letter selected: " + selectedLetter.getLetter() + ", background set to button_letter_red");
            } else {
                Toast.makeText(this, "Đã đủ chữ cái!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "Letter already selected: " + selectedLetter.getLetter());
        }
    }

    private void showGreenNoticeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GreenNoticeFragment fragment = new GreenNoticeFragment();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
        Log.d(TAG, "GreenNoticeFragment displayed");
    }

    private void showRedNoticeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        RedNoticeFragment fragment = new RedNoticeFragment();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
        Log.d(TAG, "RedNoticeFragment displayed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        letterItems.clear();
        underlineTextViews.clear();
        letterAdapter.notifyDataSetChanged();
        Log.d(TAG, "onDestroy called, resources cleared");
    }
}