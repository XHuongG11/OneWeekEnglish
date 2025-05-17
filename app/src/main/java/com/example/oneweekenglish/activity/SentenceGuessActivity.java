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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.WordAdapter;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SentenceGuessActivity extends AppCompatActivity {

    private List<TextView> underlineTextViews = new ArrayList<>();
    private List<Word> wordItems;
    private WordAdapter wordAdapter;
    private String sentenceToGuess; // Câu cần đoán, xây dựng từ Word objects
    private int currentUnderlineIndex = 0; // Theo dõi vị trí gạch dưới hiện tại
    private boolean isSentenceCorrect = false; // Cờ để kiểm tra câu đã đúng chưa
    private static final String TAG = "SentenceGuessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sentence_guess);
        Log.d(TAG, "onCreate called");

        // Khởi tạo dữ liệu: danh sách các Word objects
        wordItems = new ArrayList<>();
        wordItems.add(new Word("1", "I", Arrays.asList("tôi"), Arrays.asList("đại từ nhân xưng"), "/aɪ/", "", "pronoun"));
        wordItems.add(new Word("2", "like", Arrays.asList("mèo"), Arrays.asList("bày tỏ tình cảm"), "/lʌv/", "", "verb"));
        wordItems.add(new Word("3", "dog", Arrays.asList("thích"), Arrays.asList("đại từ nhân xưng"), "/juː/", "", "pronoun"));
        wordItems.add(new Word("4", "He", Arrays.asList("anh ấy"), Arrays.asList("cảm xúc tiêu cực"), "/heɪt/", "", "verb"));
        wordItems.add(new Word("5", "cat", Arrays.asList("chó"), Arrays.asList("đại từ nhân xưng"), "/wiː/", "", "pronoun"));
        wordItems.add(new Word("6", "pig", Arrays.asList("heo"), Arrays.asList("di chuyển"), "/ɡoʊ/", "", "verb"));
        wordItems.add(new Word("7", "hate", Arrays.asList("ghét"), Arrays.asList("giới từ chỉ hướng"), "/tuː/", "", "preposition"));

        // Tạo câu cần đoán từ một số Word objects (ví dụ: "I LOVE YOU")
        List<Word> sentenceWords = Arrays.asList(
                wordItems.get(0), // I
                wordItems.get(1), // LOVE
                wordItems.get(2)  // YOU
        );
        sentenceToGuess = String.join(" ", Arrays.asList(
                sentenceWords.get(0).getContent(),
                sentenceWords.get(1).getContent(),
                sentenceWords.get(2).getContent()
        ));
        Log.d(TAG, "Sentence to guess: " + sentenceToGuess);

        // Tạo gạch dưới động
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        String[] wordsInSentence = sentenceToGuess.split(" ");
        for (int i = 0; i < wordsInSentence.length; i++) {
            View underlineView = LayoutInflater.from(this).inflate(R.layout.underline_view, underlineContainer, false);
            TextView wordText = underlineView.findViewById(R.id.letterText);
            wordText.setOnClickListener(v -> {
                if (isSentenceCorrect) {
                    return; // Không cho phép xóa nếu câu đã đúng
                }
                TextView textView = (TextView) v;
                String word = textView.getText().toString();
                if (!word.isEmpty()) {
                    textView.setText("");
                    for (Word item : wordItems) {
                        if (item.getContent().equals(word) && wordAdapter.isSelected(item)) {
                            wordAdapter.setSelected(item, false);
                            break;
                        }
                    }
                    currentUnderlineIndex--;
                    Log.d(TAG, "Word removed: " + word + ", background reverted to word_background");
                }
            });
            underlineTextViews.add(wordText);
            underlineContainer.addView(underlineView);
        }

        // Trộn danh sách từ để hiển thị ngẫu nhiên
        Collections.shuffle(wordItems);

        // Thiết lập GridView cho các từ
        GridView wordGridView = findViewById(R.id.letterGridView);
        wordAdapter = new WordAdapter(wordItems, position -> {
            Word selectedWord = wordItems.get(position);
            Log.d(TAG, "Fallback: Word button clicked: " + selectedWord.getContent());
            handleWordClick(selectedWord);
        });
        wordGridView.setAdapter(wordAdapter);
        wordGridView.setOnItemClickListener((parent, view, position, id) -> {
            Word selectedWord = wordItems.get(position);
            Log.d(TAG, "GridView: Word button clicked: " + selectedWord.getContent());
            handleWordClick(selectedWord);
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
            Toast.makeText(this, "Phát âm thanh: " + sentenceToGuess, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Speaker button clicked");
        });

        // Sự kiện nhấn cho nút CHECK
        ImageButton checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(v -> {
            StringBuilder guessedSentence = new StringBuilder();
            for (TextView textView : underlineTextViews) {
                guessedSentence.append(textView.getText().toString()).append(" ");
            }
            String finalGuessedSentence = guessedSentence.toString().trim();
            Log.d(TAG, "Guessed sentence: " + finalGuessedSentence);
            if (finalGuessedSentence.equals(sentenceToGuess)) {
                isSentenceCorrect = true;
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

    private void handleWordClick(Word selectedWord) {
        if (isSentenceCorrect) {
            return; // Không cho phép chọn thêm từ nếu câu đã đúng
        }
        if (!wordAdapter.isSelected(selectedWord)) {
            if (currentUnderlineIndex < underlineTextViews.size()) {
                underlineTextViews.get(currentUnderlineIndex).setText(selectedWord. getContent());
                wordAdapter.setSelected(selectedWord, true);
                currentUnderlineIndex++;
                Log.d(TAG, "Word selected: " + selectedWord.getContent() + ", background set to button_word_red");
            } else {
                Toast.makeText(this, "Đã đủ từ!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "Word already selected: " + selectedWord.getContent());
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
        wordItems.clear();
        underlineTextViews.clear();
        wordAdapter.notifyDataSetChanged();
        Log.d(TAG, "onDestroy called, resources cleared");
    }
}