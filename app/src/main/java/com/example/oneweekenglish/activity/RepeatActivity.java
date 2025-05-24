package com.example.oneweekenglish.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.fragment.YellowNoticeFragment;
import com.example.oneweekenglish.model.Word;
import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.PronunciationChecker;
import com.example.oneweekenglish.util.Sound;

import java.util.Arrays;
import java.util.List;

public class RepeatActivity  extends AppCompatActivity
        implements GreenNoticeFragment.OnContinueClickListener,
        RedNoticeFragment.OnTryAgainListener,
        YellowNoticeFragment.OnTryAgainListenerYellow,
        YellowNoticeFragment.OnNextListener{
    private static  List<Word> data_words ;
    private ImageButton buttonRecord;
    private TextView textViewCurrentWord;
    private static int currentIndexWord = 0;
    private ImageView wordImage;
    private Sound sound;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repeat);
        sound = new Sound(getApplicationContext());
        wordImage = findViewById(R.id.wordImage);

        // lấy dữ liệu
        data_words = GlobalVariable.currentLesson.getMatchWordPractice().getWords();
        //set text
        textViewCurrentWord = findViewById(R.id.wordText);
        textViewCurrentWord.setText(data_words.get(currentIndexWord).getContent());

        handleClickButtonClose();
        handleClickButtonSpeaker();


        buttonRecord = findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(v -> {
            handleClickRecord();
        });
    }
    private void handleClickRecord(){
        initRecognizerChecker(data_words.get(currentIndexWord).getContent());
        if (pronunciationChecker.hasRecordAudioPermission()) {
            pronunciationChecker.startListening();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            Log.e("RECORD","This app doesn't have permission to record");
        }
    }
    private void handleClickButtonSpeaker(){
        ImageButton speakerButton = findViewById(R.id.speakerButton);
        String word = data_words.get(currentIndexWord).getContent();
        speakerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();
            sound.readText(word);
            Log.d(TAG, "Speaker button clicked");
        });
    }
    private void handleClickButtonClose(){
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button clicked");
            finish();
        });
    }
    private void showGreenNoticeFragment() {
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        int soundId = soundPool.load(getApplicationContext(), R.raw.win_game_guess_word, 1);
        soundPool.setOnLoadCompleteListener((sp, id, status) -> {
            if (status == 0) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GreenNoticeFragment fragment = new GreenNoticeFragment();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
    }
    private void showYellowNoticeFragment(String message) {
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        int soundId = soundPool.load(getApplicationContext(), R.raw.lose_game_guess_word, 1);
        soundPool.setOnLoadCompleteListener((sp, id, status) -> {
            if (status == 0) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        YellowNoticeFragment fragment = new YellowNoticeFragment();
        fragment.setAnswer(message+"\n Please try again");
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
    }

    private void showRedNoticeFragment(String message) {
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        int soundId = soundPool.load(getApplicationContext(), R.raw.lose_game_guess_word, 1);
        soundPool.setOnLoadCompleteListener((sp, id, status) -> {
            if (status == 0) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
        RedNoticeFragment fragment = new RedNoticeFragment();
        fragment.setAnswer(message);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
    }

    PronunciationChecker pronunciationChecker;
    private void initRecognizerChecker(String targetText){
        pronunciationChecker = new PronunciationChecker(this, targetText);
        // Set result listener
        pronunciationChecker.setOnPronunciationResultListener(new PronunciationChecker.OnPronunciationResultListener() {
            @Override
            public void onPronunciationResult(String targetText, String spokenText, int accuracyPercentage, String formattedFeedback) {
                System.out.println(spokenText);
                if(accuracyPercentage >= 80){
                    showGreenNoticeFragment();
                } else if (accuracyPercentage < 50) {
                    showRedNoticeFragment("Your spoken: "+ spokenText);
                }else if(accuracyPercentage >= 50 && accuracyPercentage < 80){
                    showYellowNoticeFragment("Your spoken: "+ spokenText);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error: " + errorMessage,"Spoken test");
                showRedNoticeFragment("Please speak again ...");
            }

            @Override
            public void onListeningStarted() {
                Log.i("Listening...","Spoken test");
            }

            @Override
            public void onListeningFinished() {
                Log.i("Finish ...","Spoken test");
            }
        });
    }
    @Override
    public void onContinueClicked() {
        nextToNewWord();
    }

    @Override
    public void onTryAgainClicked() {
        nextToNewWord();
    }
    private void nextToNewWord(){
        try {
            if(currentIndexWord == (data_words.size() - 1)){
                Intent intent = new Intent(this, LessonCompleteActivity.class);
                startActivity(intent);
                // kết thúc bài học (đáng lẻ làm ở trang tổng kết, để tạm ở đây)
                GlobalVariable.currentLesson = null;
                return;
            }
            currentIndexWord++;
            textViewCurrentWord = findViewById(R.id.wordText);
            String word = data_words.get(currentIndexWord).getContent();
            textViewCurrentWord.setText(word);
            sound.readText(word);
            // load hình anh
            Glide.with(this)
                    .load(data_words.get(currentIndexWord).getImageUrl())
                    .into(wordImage);
            buttonRecord.setOnClickListener(v -> {
                initRecognizerChecker(data_words.get(currentIndexWord).getContent());
                if (pronunciationChecker.hasRecordAudioPermission()) {
                    pronunciationChecker.startListening();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                    Log.e("RECORD", "This app doesn't have permission to record");
                }
            });
            hideFragment();
        }catch (Exception ex){
            Log.e(ex.getMessage(),"Bug from repeat activity");
        }
    }

    @Override
    public void onNextClicked() {
        nextToNewWord();
    }

    @Override
    public void onTryAgainYellowClicked() {
        hideFragment();
    }
    private void hideFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fm.beginTransaction()
                    .remove(fragment)
                    .commit();
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        }
    }
}
