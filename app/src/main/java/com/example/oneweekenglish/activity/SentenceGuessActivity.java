package com.example.oneweekenglish.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.SpacesItemDecoration;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;


import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.WordAdapter;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.model.Question;
import com.example.oneweekenglish.util.Sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SentenceGuessActivity extends AppCompatActivity
        implements GreenNoticeFragment.OnContinueClickListener,
        RedNoticeFragment.OnTryAgainListener {

    private List<TextView> underlineTextViews = new ArrayList<>();
    private WordAdapter wordAdapter;
    private int currentUnderlineIndex = 0;
    private ImageView wordImage;
    private boolean isSentenceCorrect = false;
    private static final String TAG = "SentenceGuessActivity";
    private List<Question> questions;
    private List<String> answers;
    private Question currentQuestion;
    private int currentIndex;
    private Sound sound;
    private RecyclerView wordRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sentence_guess);
        Log.d(TAG, "onCreate called");

        sound = new Sound(getApplicationContext());
        wordImage = findViewById(R.id.wordImage);

        wordRecyclerView = findViewById(R.id.letterRecyclerView);
        // Thêm khoảng cách giữa các item
        int spacingInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        wordRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        // Khởi tạo dữ liệu
        questions = GlobalVariable.currentLesson.getGrammarPractice().getQuestions();

        currentIndex = 0;
        currentQuestion = questions.get(currentIndex);
        loadQuestion();
    }

    private void loadQuestion(){
        // tạo dữ liệu câu hỏi
        createQuestion(currentQuestion);
        // gán hình ảnh
        Glide.with(this)
                .load(currentQuestion.getUrlImage())
                .into(wordImage);

        // Tạo gạch dưới động
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        String[] wordsInSentence = currentQuestion.getContent().split(" ");
        for (int i = 0; i < wordsInSentence.length; i++) {
            View underlineView = LayoutInflater.from(this).inflate(R.layout.underline_view, underlineContainer, false);
            TextView wordText = underlineView.findViewById(R.id.letterText);
            wordText.setOnClickListener(v -> {
                if (isSentenceCorrect) return;
                TextView textView = (TextView) v;
                String word = textView.getText().toString();
                if (word.isEmpty()) return;
                textView.setText("");
                for (String item : answers) {
                    if (item.equals(word) && wordAdapter.isSelected(item)) {
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

        // Thiết lập FlexboxLayoutManager answers
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        wordRecyclerView.setLayoutManager(flexboxLayoutManager);

        // Thiết lập adapter
        wordAdapter = new WordAdapter(answers, position -> {
            String selectedWord = answers.get(position);
            Log.d(TAG, "Fallback: Word button clicked: " + selectedWord);
            handleWordClick(selectedWord);
        });
        wordRecyclerView.setAdapter(wordAdapter);


        // Nút đóng
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button Lucia button clicked");
            finish();
        });

        // Nút loa
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(v -> {
            sound.readText(currentQuestion.getContent());
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
            if (finalGuessedSentence.equals(currentQuestion.getContent())) {
                isSentenceCorrect = true;
                for (TextView textView : underlineTextViews) {
                    textView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                showGreenNoticeFragment();

                // phát âm thanh đúng
                SoundPool soundPool = new SoundPool.Builder()
                        .setMaxStreams(5)
                        .build();
                int soundId = soundPool.load(getApplicationContext(), R.raw.win_game_guess_word, 1);

                soundPool.setOnLoadCompleteListener((sp, id, status) -> {
                    if (status == 0) {
                        soundPool.play(soundId, 1, 1, 0, 0, 1);
                    }
                });
            } else {
                for (TextView textView : underlineTextViews) {
                    textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                showRedNoticeFragment();

                // phát âm thanh sai
                SoundPool soundPool = new SoundPool.Builder()
                        .setMaxStreams(5)
                        .build();

                int soundId = soundPool.load(getApplicationContext(), R.raw.lose_game_guess_word, 1);

                soundPool.setOnLoadCompleteListener((sp, id, status) -> {
                    if (status == 0) {
                        soundPool.play(soundId, 1, 1, 0, 0, 1);
                    }
                });
            }
        });
    }
    private void handleWordClick(String selectedWord) {
        if (isSentenceCorrect) return;
        if (!wordAdapter.isSelected(selectedWord)) {
            //Phat am thanh
            SoundPool soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .build();

            int soundId = soundPool.load(getApplicationContext(), R.raw.click_button_letter, 1);
            soundPool.setOnLoadCompleteListener((sp, id, status) -> {
                if (status == 0) {
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                }
            });
            if (currentUnderlineIndex < underlineTextViews.size()) {
                underlineTextViews.get(currentUnderlineIndex).setText(selectedWord);
                wordAdapter.setSelected(selectedWord, true);
                currentUnderlineIndex++;
                Log.d(TAG, "Word selected: " + selectedWord);
            } else {
                Toast.makeText(this, "Đã đủ từ!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "Word already selected: " + selectedWord);
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
        fragment.setAnswer(currentQuestion.getContent()); // Truyền câu trả lời đúng
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
        Log.d(TAG, "RedNoticeFragment displayed with answer: " + currentQuestion.getContent());
    }

    @Override
    public void onContinueClicked() {
        // nếu câu cuối thì chuyển activity
        if(currentIndex == (questions.size() -1)){
            Intent intent = new Intent(this, RepeatActivity.class);
            startActivity(intent);
            return;
        }
        // set lại giá trị cho phép chọn
        isSentenceCorrect = false;
        currentUnderlineIndex = 0;

        // Xóa dữ liệu cũ trong underline
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        underlineContainer.removeAllViews();
        underlineTextViews.clear();

        // load tiếp câu tiếp theo
        currentIndex++;
        currentQuestion = questions.get(currentIndex);
        loadQuestion();

        // Ẩn fragment GreenNotice
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fm.beginTransaction()
                    .remove(fragment)
                    .commit();
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        }
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
        for (String word : answers) {
            wordAdapter.setSelected(word, false);
        }
        currentUnderlineIndex = 0;
        isSentenceCorrect = false;
        wordAdapter.notifyDataSetChanged();
        Log.d(TAG, "Game reset completed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        answers.clear();
        underlineTextViews.clear();
        wordAdapter.notifyDataSetChanged();
        Log.d(TAG, "onDestroy called, resources cleared");
    }
    private String getRandomWordFromAssets(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open("english_words_100.txt");
            Log.d("DEBUG", "File opened successfully");

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<String> words = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
                Log.d("DEBUG", "Read word: " + line.trim());
            }

            if (words.isEmpty()) {
                Log.d("DEBUG", "No words loaded from file");
                return null;
            }

            Random random = new Random();
            String word = words.get(random.nextInt(words.size()));
            Log.d("DEBUG", "Random word chosen: " + word);
            return word;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "Error reading words from assets: " + e.getMessage());
            return null;
        }
    }

    private void createQuestion(Question question){
        answers = new ArrayList<>(Arrays.asList(question.getContent().split(" ")));
        for(int i = 0; i < 3; i++){
            String randomWord = getRandomWordFromAssets(this);
            answers.add(randomWord);
        }
        // Xáo trộn
        Collections.shuffle(answers);

        for (String word : answers) {
            Log.d("TAG", word != null ? word : "message is null");
        }
    }

}