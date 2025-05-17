package com.example.oneweekenglish.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.fragment.GreenNoticeFragment;
import com.example.oneweekenglish.fragment.RedNoticeFragment;
import com.example.oneweekenglish.util.PronunciationChecker;

public class RepeatActivity  extends AppCompatActivity
        implements GreenNoticeFragment.OnContinueClickListener,
        RedNoticeFragment.OnTryAgainListener  {
    private ImageButton buttonRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repeat);
        buttonRecord = findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(v -> {
                initRecognizerChecker("monkey");
                if (pronunciationChecker.hasRecordAudioPermission()) {
                    pronunciationChecker.startListening();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                    Log.e("RECORD","This app doesn't have permission to record");
                }
        });
    }
    private void showGreenNoticeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GreenNoticeFragment fragment = new GreenNoticeFragment();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
        transaction.commit();
    }

    private void showRedNoticeFragment() {
        RedNoticeFragment fragment = new RedNoticeFragment();
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
                    showRedNoticeFragment();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error: " + errorMessage,"Spoken test");
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

    }

    @Override
    public void onTryAgainClicked() {
    }
}
