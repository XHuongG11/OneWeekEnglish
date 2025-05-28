package com.example.oneweekenglish.activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.oneweekenglish.dao.LearningProgressDAO;
import com.example.oneweekenglish.dao.OnGetByIdListener;
import com.example.oneweekenglish.model.EPracticeType;
import com.example.oneweekenglish.model.LearningProgress;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.model.LessonProgress;
import com.example.oneweekenglish.model.User;
import com.example.oneweekenglish.model.Word;
import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.MusicService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchPictureWithLetterActivity extends AppCompatActivity {
    private List<Word> wordList;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_picture_with_letter);
        checkIfLessonAlreadyCompletedThenNext(); // Kiểm tra và chuyển nếu đã học
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
                    saveLearningProgress();
                }
            }

        });

    }
    private void saveLearningProgress() {
        SharedPreferences prefs = getSharedPreferences("CurentUser", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        String email = prefs.getString("email", null);
        String fullName = prefs.getString("fullName", null);

        if (userId == null || email == null || fullName == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = new User(userId, email, fullName, null);

        LearningProgressDAO lpDAO = new LearningProgressDAO();

        lpDAO.getByUser(currentUser, new OnGetByIdListener<LearningProgress>() {
            @Override
            public void onGetByID(LearningProgress lp) {
                if (lp == null) {
                    lp = new LearningProgress(currentUser, new ArrayList<>());
                }

                String lessonId = GlobalVariable.currentLesson.getId();
                List<LessonProgress> list = lp.getLessonProgress();
                LessonProgress target = null;

                for (LessonProgress p : list) {
                    if (lessonId.equals(p.getLesson().getId())) {
                        target = p;
                        break;
                    }
                }

                if (target == null) {
                    target = new LessonProgress(GlobalVariable.currentLesson, 100.0);
                    list.add(target);
                } else {
                    target.setPercent(100.0);
                }

                target.markPracticeCompleted(EPracticeType.LEARN_WORD);
                lp.setLessonProgress(list);

                if (lp.getId() == null) {
                    lpDAO.create(lp, success -> {
                        if (success) {
                            Toast.makeText(MatchPictureWithLetterActivity.this, "Tiến độ đã lưu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MatchPictureWithLetterActivity.this, "Lưu tiến độ thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    lpDAO.update(lp.getId(), lp, success -> {
                        if (success) {
                            Toast.makeText(MatchPictureWithLetterActivity.this, "Tiến độ đã cập nhật!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MatchPictureWithLetterActivity.this, "Cập nhật tiến độ thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onGetFailed(Exception e) {
                Toast.makeText(MatchPictureWithLetterActivity.this, "Lỗi truy xuất tiến độ!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkIfLessonAlreadyCompletedThenNext() {
        SharedPreferences prefs = getSharedPreferences("CurentUser", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        String email = prefs.getString("email", null);
        String fullName = prefs.getString("fullName", null);

        if (userId == null || email == null || fullName == null) {
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = new User(userId, email, fullName, null);
        String currentLessonId = GlobalVariable.currentLesson.getId();

        LearningProgressDAO lpDAO = new LearningProgressDAO();

        lpDAO.getByUser(currentUser, new OnGetByIdListener<LearningProgress>() {
            @Override
            public void onGetByID(LearningProgress lp) {
                if (lp == null || lp.getLessonProgress() == null) return;

                for (LessonProgress p : lp.getLessonProgress()) {
                    if (currentLessonId.equals(p.getLesson().getId())) {
                        if (p.getPercent() >= 100 && p.isPracticeCompleted(EPracticeType.LEARN_WORD)) {
                            // ✅ Đã học xong → chuyển sang WordGuessActivity
                            goToWordGuessActivity();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onGetFailed(Exception e) {
                Toast.makeText(MatchPictureWithLetterActivity.this, "Không thể kiểm tra tiến độ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToWordGuessActivity() {
        Intent intent = new Intent(this, WordGuessActivity.class);
        startActivity(intent);
        finish();
    }




}