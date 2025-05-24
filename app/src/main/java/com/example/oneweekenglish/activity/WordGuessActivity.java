package com.example.oneweekenglish.activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.LetterAdapter;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.model.FillBlank;
import com.example.oneweekenglish.model.LetterItem;
import com.example.oneweekenglish.model.Word;
import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordGuessActivity extends AppCompatActivity
        implements GreenNoticeFragment.OnContinueClickListener,
        RedNoticeFragment.OnTryAgainListener{

    private List<TextView> underlineTextViews = new ArrayList<>();
    private List<LetterItem> letterItems ;
    private LetterAdapter letterAdapter;
    // Từ cần đoán
    private Word wordToGuess;
    private int currentIndexWord = 0; //tu hien tai trong list
    private int currentUnderlineIndex = 0; // Theo dõi vị trí gạch dưới hiện tại
    private boolean isWordCorrect = false; // Cờ để kiểm tra từ đã đúng chưa
    private static final String TAG = "WordGuessActivity";
    private ImageView wordImage;

    private static List<Word> data_words;
    private TextView txtLesson;
    private Sound sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word_guess);
        Log.d(TAG, "onCreate called");
        sound = new Sound(getApplicationContext());
        wordImage = findViewById(R.id.wordImage);
        txtLesson = findViewById(R.id.numberLessonText);
        txtLesson.setText(GlobalVariable.currentLesson.getName());

        //khoi tao du lieu va su kien
        getData();

        wordToGuess = data_words.get(currentIndexWord);
        wordToGuess.setContent(wordToGuess.getContent().toUpperCase());
        initDataForWord();
    }

    private void getData() {
        FillBlank fillBlank = GlobalVariable.currentLesson.getFillBlankPractice();
        data_words = fillBlank.getWords();
    }

    private void initDataForWord(){
        // Khởi tạo dữ liệu: từ cần đoán và các chữ cái để chọn
        createLetterForGuess();
        // Tạo gạch dưới động
        taoDuLieuDauGachDuoi();
        // load hình
        Glide.with(this)
                .load(wordToGuess.getImageUrl())
                .into(wordImage);
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
          //  handleLetterClick(selectedLetter);
        });
        // Sự kiện nhấn cho nút đóng
        handleClickButtonClose();
        // Sự kiện nhấn cho nút loa
        handleClickButtonSpeaker();
        //su kien nhan nut check
        handleClickButtonCheck();
    }
    private void createLetterForGuess() {
        Random random = new Random();
        int length_word = wordToGuess.getContent().length();
        int length_letters = 5;
        List<Character> word_content = new ArrayList<>();
        letterItems = new ArrayList<>();

        //init data
        for (Character c : wordToGuess.getContent().toCharArray()) {
            word_content.add(c);
        }

        //random 4 letters
        for (int i = 0; i < length_letters; i++) {
            char letter = (char) ('A' + random.nextInt(26)); // random từ 'a' đến 'z'
            word_content.add(letter);
        }
        //random word and append to list
        Collections.shuffle(word_content);
        System.out.println(word_content);
        // Thêm vào letterItems
        for (Character c : word_content) {
            LetterItem letterItem = new LetterItem(c.toString().toUpperCase());
            letterItems.add(letterItem);
        }

    }
    private void taoDuLieuDauGachDuoi(){
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        for (int i = 0; i < wordToGuess.getContent().length(); i++) {
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
    }
    private void handleClickButtonClose(){
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button clicked");
            finish();
        });
    }
    private void handleClickButtonSpeaker(){
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();
            sound.readText(wordToGuess.getContent());
            Log.d(TAG, "Speaker button clicked");
        });
    }
    private void handleClickButtonCheck(){
        // Sự kiện nhấn cho nút CHECK
        ImageButton checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(v -> {
            StringBuilder guessedWord = new StringBuilder();
            for (
                    TextView textView : underlineTextViews) {
                guessedWord.append(textView.getText().toString());
            }
            Log.d(TAG, "Guessed word: " + guessedWord);
            if (guessedWord.toString().equalsIgnoreCase(wordToGuess.getContent())) {
                isWordCorrect = true; // Đặt cờ khi từ đúng
                // Chuyển chữ cái thành màu xanh lá
                for (TextView textView : underlineTextViews) {
                    textView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                showGreenNoticeFragment();
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

    private void handleLetterClick(LetterItem selectedLetter) {
        if (isWordCorrect) {
            return; // Không cho phép chọn thêm chữ nếu từ đã đúng
        }
        if (!selectedLetter.isSelected()) {
            if (currentUnderlineIndex < underlineTextViews.size()) {
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
        RedNoticeFragment fragment = new RedNoticeFragment();
        fragment.setAnswer("The correct answer is: "+wordToGuess.getContent());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
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

    @Override
    public void onContinueClicked() {
        // nếu từ cuối, chuyển sang activity khác
        if(currentIndexWord == (data_words.size() - 1)){
            Intent intent = new Intent(this, SentenceGuessActivity.class);
            startActivity(intent);
            return;
        }
        // Chọn từ mới (ví dụ word thứ 4 trong data_words)
        currentIndexWord++;
        wordToGuess = data_words.get(currentIndexWord);
        wordToGuess.setContent(wordToGuess.getContent().toUpperCase());

        // Reset trạng thái
        currentUnderlineIndex = 0;
        isWordCorrect = false;

        // Xóa dữ liệu cũ trong underline
        LinearLayout underlineContainer = findViewById(R.id.underlineContainer);
        underlineContainer.removeAllViews();
        underlineTextViews.clear();

        // Tạo lại dữ liệu và UI
        initDataForWord();

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
        // Ẩn fragment RedNotice
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fm.beginTransaction()
                    .remove(fragment)
                    .commit();
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        }

        // Clear những gì người dùng đã điền:
        // 1. Xóa text trong các TextView underline
        for (TextView tv : underlineTextViews) {
            tv.setText("");
            tv.setTextColor(getResources().getColor(android.R.color.black)); // reset màu chữ nếu bạn muốn
        }

        // 2. Đánh dấu lại các letterItems là chưa được chọn
        for (LetterItem item : letterItems) {
            item.setSelected(false);
        }

        // 3. Reset chỉ số underline hiện tại
        currentUnderlineIndex = 0;

        // 4. Update lại giao diện chữ cái (LetterAdapter)
        if (letterAdapter != null) {
            letterAdapter.notifyDataSetChanged();
        }

        // Reset cờ từ đúng
        isWordCorrect = false;
    }
}