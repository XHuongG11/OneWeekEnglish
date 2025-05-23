package com.example.oneweekenglish.activity;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.CardAdapter;
import com.example.oneweekenglish.model.Word;
import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.MusicService;

import java.util.Arrays;
import java.util.List;

public class MatchPictureWithLetterActivity extends AppCompatActivity {
    private List<Word> wordList;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_picture_with_letter);
        wordList = GlobalVariable.currentLesson.getLearnWordPractice().getWords();
        //tải các thẻ
        adapter = new CardAdapter(wordList);
        GridView cardGridView = findViewById(R.id.cardGridView);
        cardGridView.setAdapter(adapter);

        ImageButton closeButton = findViewById(R.id.closeButton);
        ImageButton hintButton = findViewById(R.id.hintButton);

        //đóng lại
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        });
        //nút gợi ý
        hintButton.setOnClickListener(v -> {
            //
        });
        //nút tiếp tục, code chỉ hiện khi đã match được hết nha
        ImageButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WordGuessActivity.class);
            v.getContext().startActivity(intent);
        });

        //setup adapter
        setupAdapter();
    }
    private void setupAdapter(){
        adapter.setOnCardClickListener(new CardAdapter.OnCardClickListener() {
            private Word firstSelected = null;
            private int firstPosition = -1;

            @Override
            public void onCardClick(int position, Word word, int viewType) {
                MusicService.clickButtonSound(getApplicationContext());
                adapter.setSelectedPosition(position);

                if (firstSelected == null) {
                    firstSelected = word;
                    firstPosition = position;
                } else {
                    if (firstSelected.getContent().equals(word.getContent()) && position != firstPosition) {
                        Toast.makeText(getApplicationContext(), "Đúng rồi!", Toast.LENGTH_SHORT).show();
                        adapter.setMatched(firstPosition, position);  // Ẩn 2 card đúng
                    } else {
                        MusicService.loseSound(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Sai rồi!", Toast.LENGTH_SHORT).show();

                        // Delay để reset selectedPosition và cập nhật lại viền
                        new Handler().postDelayed(() -> {
                            adapter.setSelectedPosition(-1);
                        }, 200); // delay 0.5 giây
                    }
                    firstSelected = null;
                    firstPosition = -1;
                }

                if (adapter.getMatchedCount() == wordList.size() * 2) {
                    Toast.makeText(getApplicationContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_LONG).show();
                    SoundPool soundPool = new SoundPool.Builder()
                            .setMaxStreams(5)
                            .build();
                    int soundId = soundPool.load(getApplicationContext(), R.raw.win_game_guess_word, 1);
                    soundPool.setOnLoadCompleteListener((sp, id, status) -> {
                        if (status == 0) {
                            soundPool.play(soundId, 1, 1, 0, 0, 1);
                        }
                    });
                }
            }

        });

    }
}
