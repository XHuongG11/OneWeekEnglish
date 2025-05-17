package com.example.oneweekenglish.activity;

import android.content.Intent;
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

public class SentenceGuessActivity extends AppCompatActivity
        implements GreenNoticeFragment.OnContinueClickListener,
        RedNoticeFragment.OnTryAgainListener {

    private List<TextView> underlineTextViews = new ArrayList<>();
    private List<Word> wordItems;
    private WordAdapter wordAdapter;
    private String sentenceToGuess;
    private int currentUnderlineIndex = 0;
    private boolean isSentenceCorrect = false;
    private static final String TAG = "SentenceGuessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sentence_guess);
        Log.d(TAG, "onCreate called");

        // Khởi tạo dữ liệu
        wordItems = new ArrayList<>();
        wordItems.add(new Word("1", "I", Arrays.asList("tôi"), Arrays.asList("đại từ nhân xưng"), "/aɪ/", "", "pronoun"));
        wordItems.add(new Word("2", "like", Arrays.asList("mèo"), Arrays.asList("bày tỏ tình cảm"), "/lʌv/", "", "verb"));
        wordItems.add(new Word("3", "dog", Arrays.asList("thích"), Arrays.asList("đại từ nhân xưng"), "/juː/", "", "pronoun"));
        wordItems.add(new Word("4", "He", Arrays.asList("anh ấy"), Arrays.asList("cảm xúc tiêu cực"), "/heɪt/", "", "verb"));
        wordItems.add(new Word("5", "cat", Arrays.asList("chó"), Arrays.asList("đại từ nhân xưng"), "/wiː/", "", "pronoun"));
        wordItems.add(new Word("6", "pig", Arrays.asList("heo"), Arrays.asList("di chuyển"), "/ɡoʊ/", "", "verb"));
        wordItems.add(new Word("7", "hate", Arrays.asList("ghét"), Arrays.asList("giới từ chỉ hướng"), "/tuː/", "", "preposition"));

        // Tạo câu cần đoán
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
                if (isSentenceCorrect) return;
                TextView textView = (TextView) v;
                String word = textView.getText().toString();
                if (word.isEmpty()) return;
                textView.setText("");
                for (Word item : wordItems) {
                    if (item.getContent().equals(word) && wordAdapter.isSelected(item)) {
                        wordAdapter.setSelected(item, false);
                        break;
                    }
                }
                currentUnderlineIndex--;
                Log.d(TAG, "Word removed: " + word);
            });
            underlineTextViews.add(wordText);
            underlineContainer.addView(underlineView);
        }

        // Trộn danh sách từ
        Collections.shuffle(wordItems);

        // Thiết lập GridView
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

        // Nút đóng
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button Lucia button clicked");
            finish();
        });

        // Nút loa
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Phát âm thanh: " + sentenceToGuess, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Speaker button clicked");
        });

        // Nút kiểm tra
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
        if (isSentenceCorrect) return;
        if (!wordAdapter.isSelected(selectedWord)) {
            if (currentUnderlineIndex < underlineTextViews.size()) {
                underlineTextViews.get(currentUnderlineIndex).setText(selectedWord.getContent());
                wordAdapter.setSelected(selectedWord, true);
                currentUnderlineIndex++;
                Log.d(TAG, "Word selected: " + selectedWord.getContent());
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
        fragment.setAnswer(sentenceToGuess); // Truyền câu trả lời đúng
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
        Log.d(TAG, "RedNoticeFragment displayed with answer: " + sentenceToGuess);
    }

    @Override
    public void onContinueClicked() {
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        getSupportFragmentManager().popBackStack();
        Intent intent = new Intent(this, WordGuessActivity.class); // Thay NextActivity bằng activity thật
        startActivity(intent);
        finish();
        Log.d(TAG, "Continue button clicked, navigating to NextActivity");
    }

    @Override
    public void onTryAgainClicked() {
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        getSupportFragmentManager().popBackStack();
        resetGame();
        Log.d(TAG, "Try Again button clicked, game reset");
    }

    private void resetGame() {
        for (TextView textView : underlineTextViews) {
            textView.setText("");
            textView.setTextColor(getResources().getColor(android.R.color.black)); // Reset màu
        }
        for (Word word : wordItems) {
            wordAdapter.setSelected(word, false);
        }
        currentUnderlineIndex = 0;
        isSentenceCorrect = false;
        Collections.shuffle(wordItems);
        wordAdapter.notifyDataSetChanged();
        Log.d(TAG, "Game reset completed");
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