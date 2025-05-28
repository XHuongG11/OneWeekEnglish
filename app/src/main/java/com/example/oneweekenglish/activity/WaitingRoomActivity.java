package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.FlyingWord;
import com.example.oneweekenglish.model.Player;
import com.example.oneweekenglish.model.Room;
import com.example.oneweekenglish.util.GlobalVariable;
import com.example.oneweekenglish.util.MusicManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WaitingRoomActivity extends AppCompatActivity {
    private Random random = new Random();
    private Button btnFindMatch;
    private TextView tvGameRules;
    private TextView gameStartText;
    private final String[] matchedPlayerUid = new String[1];
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ds các lesson
        Spinner spinnerLessons = findViewById(R.id.spinnerLessons);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lesson_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessons.setAdapter(adapter);

        spinnerLessons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                Toast.makeText(WaitingRoomActivity.this, "Bạn chọn: " + selectedLesson, Toast.LENGTH_SHORT).show();
                GlobalVariable.lessonGame = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // phát nhạt nền
        mediaPlayer = MediaPlayer.create(this, R.raw.background_sound_shooting_word);
        mediaPlayer.setLooping(true); // Nhạc lặp lại
        mediaPlayer.start();

        btnFindMatch = findViewById(R.id.btnFindMatch);
        btnFindMatch.setVisibility(View.VISIBLE);
        ImageView ivMatchEffect = findViewById(R.id.ivMatchEffect);
        ivMatchEffect.setVisibility(View.GONE);
        gameStartText = findViewById(R.id.gameStartText);
        gameStartText.setVisibility(View.GONE);
        // hiện luật chơi
        tvGameRules = findViewById(R.id.tvGameRules);
        showGameRulesDialog();

        String uid = String.valueOf(random.nextInt(100));

        btnFindMatch.setOnClickListener(v -> {
            // phát âm thanh
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.draw_sword);
            mediaPlayer.start();

            DatabaseReference waitingRef = FirebaseDatabase.getInstance().getReference("waitingPlayers");
            DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference("rooms");

            // hiệu ứng xuất hiện ivMatchEffect
            tvGameRules.setVisibility(View.GONE);
            Glide.with(this).asGif().load(R.drawable.vs).into(ivMatchEffect);

            ivMatchEffect.setScaleX(0f);
            ivMatchEffect.setScaleY(0f);
            ivMatchEffect.setAlpha(0f);
            ivMatchEffect.setVisibility(View.VISIBLE);

            // Bắt đầu animation
            ivMatchEffect.animate()
                    .scaleX(1.4f)
                    .scaleY(1.4f)
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(new OvershootInterpolator()) // tạo cảm giác bật nhẹ
                    .withEndAction(() -> {
                        // Sau khi scale lên to hơn
                        ivMatchEffect.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(200)
                                .start();
                    })
                    .start();

            waitingRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    Map<String, Object> players = (Map<String, Object>) currentData.getValue();

                    if (players == null || players.isEmpty()) {
                        // Không có ai trong hàng đợi -> thêm mình vào
                        Player player = new Player(uid, GlobalVariable.lessonGame);
                        currentData.child(uid).setValue(player);
                        return Transaction.success(currentData);
                    } else if (!players.containsKey(uid)) {
                        // Có người khác đang chờ -> ghép
                        for (String otherUid : players.keySet()) {
                            if (!otherUid.equals(uid)) {
                                // Lấy lesson của người chơi đang chờ
                                String otherLesson = currentData.child(otherUid).child("name").getValue(String.class);

                                // So sánh lesson
                                if (otherLesson != null && otherLesson.equals(GlobalVariable.lessonGame)) {
                                    matchedPlayerUid[0] = otherUid;

                                    // Xóa cả hai khỏi hàng đợi
                                    currentData.child(otherUid).setValue(null);
                                    currentData.child(uid).setValue(null);
                                    return Transaction.success(currentData);
                                }
                            }
                        }
                    }

                    return Transaction.success(currentData);
                }

                @Override
                public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                    if (error != null) {
                        Log.e("Matchmaking", "Transaction failed", error.toException());
                        return;
                    }

                    if (committed && matchedPlayerUid[0] != null && currentData != null) {
                        String otherUid = matchedPlayerUid[0];

                        // Lấy thông tin 2 người chơi từ snapshot
                        DataSnapshot mySnap = currentData.child(uid);
                        DataSnapshot otherSnap = currentData.child(otherUid);

                        // Trong 1 trường hợp snapshot đã bị xóa, cần check
                        Player me = new Player(uid, GlobalVariable.lessonGame); // fallback nếu null
                        Player opponent = new Player(otherUid, GlobalVariable.lessonGame);

                        if (mySnap.exists()) {
                            me = mySnap.getValue(Player.class);
                        }

                        if (otherSnap.exists()) {
                            opponent = otherSnap.getValue(Player.class);
                        }

                        // tạo words data
                        Map<String, Object> words = createWords();

                        // Tạo room
                        Map<String, Object> playersMap = new HashMap<>();
                        playersMap.put(uid, playerToMap(me));
                        playersMap.put(otherUid, playerToMap(opponent));

                        Map<String, Object> roomData = new HashMap<>();
                        roomData.put("players", playersMap);
                        roomData.put("words", words);
                        roomData.put("gameStarted", false);

                        DatabaseReference newRoomRef = roomsRef.push();
                        newRoomRef.setValue(roomData).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                gameStartEffect(() -> {
                                    Log.d("Matchmaking", "Room created with ID: " + newRoomRef.getKey());
                                    Intent intent = new Intent(WaitingRoomActivity.this, ShootingWordActivity.class);
                                    intent.putExtra("ROOM_ID", newRoomRef.getKey());
                                    intent.putExtra("UID", uid);
                                    startActivity(intent);
                                    finish();
                                });
                            } else {
                                Log.e("Matchmaking", "Room creation failed", task.getException());
                            }
                        });
                    } else {
                        // Không tìm thấy ai để ghép, thêm mình vào hàng đợi
                        Player me = new Player(uid, GlobalVariable.lessonGame);
                        FirebaseDatabase.getInstance().getReference("waitingPlayers")
                                .child(uid).setValue(me);
                        Log.d("Matchmaking", "Added to waiting room");
                        // Bắt đầu theo dõi danh sách phòng để xem khi nào có phòng chứa mình
                        FirebaseDatabase.getInstance().getReference("rooms")
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Room room = snapshot.getValue(Room.class);
                                        if (room != null && room.containsPlayer(uid)) {
                                            // Tìm thấy phòng có mình trong đó => chuyển màn hình
                                            Intent intent = new Intent(WaitingRoomActivity.this, ShootingWordActivity.class);
                                            intent.putExtra("ROOM_ID", snapshot.getKey());
                                            intent.putExtra("UID", uid);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                    @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                                    @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                                });
                    }
                }
            });
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Dừng nhạc khi Activity bị tạm dừng
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Phát lại nhạc nếu cần khi Activity quay lại
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng MediaPlayer khi thoát Activity
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private Map<String, Object> wordsAnimalsInit() {
        Map<String, Object> words = new HashMap<>();
        words.put("1", createWord("apple", false));      // Không phải động vật
        words.put("2", createWord("banana", false));   // Không phải động vật
        words.put("3", createWord("cat", true));         // Động vật
        words.put("4", createWord("dog",  true));         // Động vật
        words.put("5", createWord("sun",  false));       // Không phải động vật
        words.put("6", createWord("moon",  false));     // Không phải động vật
        words.put("7", createWord("car",  false));         // Không phải động vật
        words.put("8", createWord("tree",  false));       // Không phải động vật
        words.put("9", createWord("fish",  true));         // Động vật
        words.put("10", createWord("lion",  true));         // Động vật

        return words;
    }
    private Map<String, Object> wordsFamilyInit() {
        Map<String, Object> words = new HashMap<>();
        words.put("1", createWord("father", true));        // Gia đình
        words.put("2", createWord("mother", true));        // Gia đình
        words.put("3", createWord("son", true));           // Gia đình
        words.put("4", createWord("banana", false));       // Không phải gia đình
        words.put("5", createWord("cat", false));          // Không phải gia đình
        words.put("6", createWord("aunt", true));          // Gia đình
        words.put("7", createWord("car", false));          // Không phải gia đình
        words.put("8", createWord("tree", false));         // Không phải gia đình
        words.put("9", createWord("cousin", true));        // Gia đình
        words.put("10", createWord("dog", false));         // Không phải gia đình

        return words;
    }

    private Map<String, Object> createWord(String word, boolean correct) {
        Map<String, Object> wordData = new HashMap<>();
        wordData.put("word", word);
        wordData.put("isCorrect", correct);
        return wordData;
    }

    private Map<String, Object> createWords() {
        Map<String, Object> wordsMap = new HashMap<>();
        Map<String, Object> allWords = new HashMap<>();
        if(GlobalVariable.lessonGame.equals("Animals")){
             allWords = wordsAnimalsInit();
        }
        else if(GlobalVariable.lessonGame.equals("Family")){
            allWords = wordsFamilyInit();
        }
        Random random = new Random();

        int screenWidth = 1080;
        int index = 0;

        for (Map.Entry<String, Object> entry : allWords.entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> wordObj = (Map<String, Object>) entry.getValue();

            float x = random.nextInt(screenWidth - 200) + 100;
            float y = 0;
            int speed = 8 + random.nextInt(6);

            Map<String, Object> wordData = new HashMap<>();
            wordData.put("word", wordObj.get("word"));
            wordData.put("isCorrect", wordObj.get("isCorrect"));
            wordData.put("x", x);
            wordData.put("y", y);
            wordData.put("speed", speed);

            wordsMap.put(String.valueOf(index), wordData);
            index++;
        }

        return wordsMap;
    }

    private Map<String, Object> playerToMap(Player player) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", player.getUid());
        map.put("name", player.getName());
        map.put("score", player.getScore());
        return map;
    }
    private void showGameRulesDialog() {
        tvGameRules.setMovementMethod(new ScrollingMovementMethod());
        String title = "🎮 Luật Chơi - Shooting Word\n\n";
        String body =
                        "🕹️ Cách chơi:\n" +
                        "• Từ vựng tiếng Anh sẽ rơi từ trên xuống như những quả bóng.\n" +
                        "• Bạn cần hứng những quá bóng có nghĩa trong chủ đề.\n" +
                        "• Di chuyển nhân vật sang trái/phải để hứng đúng từ tiếng Anh có nghĩa trùng khớp.\n" +
                        "• Hứng đúng tất cả từ được yêu cầu trong thời gian nhanh nhất, để giành chiến thắng.\n" +
                        "👑 Đây là trò chơi giúp bạn rèn luyện từ vựng, phản xạ và ghi nhớ siêu nhanh!";

        SpannableString spannable = new SpannableString(title + body);

        // Làm dòng đầu tiên to hơn
        spannable.setSpan(new RelativeSizeSpan(1.3f), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Căn giữa dòng đầu tiên
        spannable.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvGameRules.setText(spannable);
        tvGameRules.setVisibility(View.VISIBLE);


        // Animation: mờ dần hiện lên
        tvGameRules.setAlpha(0f);
        tvGameRules.setTranslationY(100);
        tvGameRules.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(2000)
                .start();
    }
    private void gameStartEffect(Runnable onComplete){
        // Lấy chiều rộng màn hình
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        btnFindMatch.setVisibility(View.GONE);
        gameStartText.setVisibility(View.VISIBLE);
        TranslateAnimation enterMiddle = new TranslateAnimation(
                -800, screenWidth / 2f - 200, // từ ngoài trái đến giữa
                0, 0);
        enterMiddle.setDuration(2000);
        enterMiddle.setFillAfter(true);
        gameStartText.startAnimation(enterMiddle);

        new Handler().postDelayed(() -> {
            TranslateAnimation exitRight = new TranslateAnimation(
                    0, screenWidth, 0, 0);
            exitRight.setDuration(2000);
            exitRight.setFillAfter(true);
            gameStartText.startAnimation(exitRight);
            onComplete.run();
        }, 3000);
    }

}
