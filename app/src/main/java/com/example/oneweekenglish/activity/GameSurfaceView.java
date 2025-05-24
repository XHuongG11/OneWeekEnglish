package com.example.oneweekenglish.activity;


import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.Bullet;
import com.example.oneweekenglish.model.Explosion;
import com.example.oneweekenglish.model.FlyingWord;
import com.example.oneweekenglish.util.Sound;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private List<FlyingWord> words;
    private final Paint paint;
    private final Random random = new Random();
    private List<FlyingWord> randomWords;
    private FlyingWord currentTarget;

    private final String roomId;
    private final String uid;
    private String opponentUid;
    private String myName;
    private String opponentName;
    private int score = 0;
    private int opponentScore = 0;

    private float playerX;
    private  float playerWidth = 200;
    private  float playerHeight = 50;
    private Bitmap backgroundBitmap;
    private Bitmap playerBitmap;
    private float backgroundX = 0;
    private final float backgroundScrollSpeed = 0.3f;
    private DatabaseReference roomRef;
    private Bitmap scaledBackground;
    private boolean isStartGame;
    private boolean isWin = false;
    private MediaPlayer mediaPlayer;

    public GameSurfaceView(Context context, String roomId, String uid) {
        super(context);
        this.roomId = roomId;
        this.uid = uid;
        getHolder().addCallback(this);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_game);
        playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_game);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        setFocusable(true);

        // phát nhạt nền
        mediaPlayer = MediaPlayer.create(context, R.raw.background_sound_shooting_word);
        mediaPlayer.setLooping(true); // Nhạc lặp lại
        mediaPlayer.start();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // scale background
        int screenHeight = getHeight();
        int screenWidth = getWidth();
        scaledBackground = Bitmap.createScaledBitmap(
                backgroundBitmap,
                backgroundBitmap.getWidth() * screenHeight / backgroundBitmap.getHeight(),
                screenHeight,
                true
        );

        // scale nhân vật
        int desiredHeight = 300;
        int originalWidth = playerBitmap.getWidth();
        int originalHeight = playerBitmap.getHeight();
        int desiredWidth = originalWidth * desiredHeight / originalHeight;

        playerBitmap = Bitmap.createScaledBitmap(playerBitmap, desiredWidth, desiredHeight, true);
        playerWidth = desiredWidth;
        playerHeight = desiredHeight;

        playerX = getWidth() / 2f - playerWidth / 2f;

        roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomId);

        roomRef.child("gameStarted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean started = snapshot.getValue(Boolean.class);
                if (started != null && started) {
                    if (thread == null || !thread.isAlive()) {
                        startGame();
                    }
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });

        roomRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 2) {
                    roomRef.child("gameStarted").setValue(true);
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void startGame() {
        fetchRoomData(getHolder());
    }

    private void fetchRoomData(SurfaceHolder holder) {
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                DataSnapshot playersSnapshot = snapshot.child("players");
                for (DataSnapshot playerSnap : playersSnapshot.getChildren()) {
                    String playerUid = playerSnap.getKey();
                    String name = playerSnap.child("name").getValue(String.class);

                    if (playerUid.equals(uid)) {
                        myName = name;
                    } else {
                        opponentUid = playerUid;
                        opponentName = name;
                    }
                }

                if (opponentUid != null) {
                    roomRef.child("players").child(opponentUid).child("score")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer value = snapshot.getValue(Integer.class);
                                    if (value != null) opponentScore = value;
                                }

                                @Override public void onCancelled(@NonNull DatabaseError error) {}
                            });
                }

                DataSnapshot wordsSnapshot = snapshot.child("words");
                words = new ArrayList<>();
                randomWords = new ArrayList<>();

                for (DataSnapshot wordSnap : wordsSnapshot.getChildren()) {
                    String word = wordSnap.child("word").getValue(String.class);
                    String meaning = wordSnap.child("mean").getValue(String.class);
                    float x = random.nextInt(getWidth() - 200) + 100;
                    float y = 0;
                    int speed = wordSnap.child("speed").getValue(Long.class).intValue();

                    FlyingWord fw = new FlyingWord(word, meaning, x, y, speed);
                    randomWords.add(fw);
                }

                if (!randomWords.isEmpty()) {
                    currentTarget = randomWords.get(random.nextInt(randomWords.size()));
                }

                DataSnapshot isStartGameSnapshot = snapshot.child("gameStarted");
                isStartGame = Boolean.TRUE.equals(isStartGameSnapshot.getValue(boolean.class));

                thread = new GameThread(holder);
                thread.setRunning(true);
                thread.start();
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateScoreInFirebase(int newScore) {
        roomRef.child("players").child(uid).child("score").setValue(newScore);
    }

    private class GameThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private boolean running;

        private long lastSpawnTime = System.currentTimeMillis();
        private final int spawnDelay = 2000;

        public GameThread(SurfaceHolder holder) {
            surfaceHolder = holder;
            words = new ArrayList<>();
        }

        public void setRunning(boolean run) {
            running = run;
        }

        @Override
        public void run() {
            // theo dõi start game
            roomRef.child("gameStarted").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean isGameStarted = snapshot.getValue(Boolean.class);
                    if (isGameStarted != null && !isGameStarted) {
                        Intent intent = new Intent(getContext(), EndGameActivity.class);
                        if(isWin){
                            intent.putExtra("RESULT", "WIN");
                        }
                        else {
                            intent.putExtra("RESULT", "LOSE");
                        }
                        getContext().startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Lỗi khi theo dõi gameStarted: " + error.getMessage());
                }
            });

            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas == null) continue;

                    synchronized (surfaceHolder) {
                        // Cập nhật vị trí background theo player
                        backgroundX = -(playerX / getWidth()) * (scaledBackground.getWidth() - getWidth());

                        // Vẽ background
                        canvas.drawBitmap(scaledBackground, backgroundX, 0, null);

                        // Tạo từ mới mỗi spawnDelay nếu chưa quá nhiều
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastSpawnTime > spawnDelay && randomWords != null && !randomWords.isEmpty() && words.size() < 5) {
                            FlyingWord newWord = randomWords.get(random.nextInt(randomWords.size()));
                            float x = random.nextInt(getWidth() - 200) + 100;
                            int speed = random.nextInt(5) + 5;

                            words.add(new FlyingWord(newWord.word, newWord.meaning, x, 0, speed));
                            lastSpawnTime = currentTime;
                        }

                        // Vẽ và cập nhật vị trí các từ đang rơi
                        Iterator<FlyingWord> iterator = words.iterator();
                        while (iterator.hasNext()) {
                            FlyingWord word = iterator.next();
                            word.y += word.speed;
                            word.draw(canvas, paint);

                            RectF wordRect = new RectF(word.x, word.y, word.x + 80, word.y + 80);
                            RectF playerRect = new RectF(playerX, getHeight() - playerHeight - 20,
                                    playerX + playerWidth, getHeight() - 20);
                            // Kiểm tra va chạm
                            if (RectF.intersects(wordRect, playerRect))  {
                                if (currentTarget != null && word.word.equals(currentTarget.word)) {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.correct_word_game);
                                    mediaPlayer.start();
                                    // Giải phóng sau khi phát xong
                                    mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                                    score++;
                                    updateScoreInFirebase(score);

                                    // Xoá currentTarget khỏi danh sách randomWords
                                    Iterator<FlyingWord> randomIt = randomWords.iterator();
                                    while (randomIt.hasNext()) {
                                        if (randomIt.next().word.equals(currentTarget.word)) {
                                            randomIt.remove();
                                            break;
                                        }
                                    }
                                    currentTarget = randomWords.isEmpty() ? null :
                                            randomWords.get(random.nextInt(randomWords.size()));

                                    // Kết thúc game
                                    if (currentTarget == null) {
                                        isWin = true;
                                        // end game
                                        roomRef.child("gameStarted").setValue(false);
                                        return;
                                    }
                                }
                                else{
                                    MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.incorrect_word_game);
                                    mediaPlayer.start();
                                    // Giải phóng sau khi phát xong
                                    mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                                }
                                iterator.remove();
                            } else if (word.y > 2000) {
                                iterator.remove();
                            }
                        }

                        // Vẽ người chơi
                        canvas.drawBitmap(playerBitmap, playerX, getHeight() - playerHeight - playerBitmap.getHeight(), null);
                        paint.setColor(Color.BLACK);

                        // Vẽ thông tin
                        drawPlayerInfo(canvas);
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText("Từ cần hứng: " + (currentTarget != null ? currentTarget.meaning : "Xong!"), getWidth() / 2f, 400, paint);
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignored) {}
            }
        }
    }
    private void drawPlayerInfo(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.LEFT);

        int offsetY = 60;

        // Nền mờ cho tên người chơi
        paint.setColor(Color.parseColor("#80000000"));
        canvas.drawRect(20, 20 + offsetY, 520, 160 + offsetY, paint);

        paint.setColor(Color.WHITE);
        canvas.drawText("You", 40, 80 + offsetY, paint);
        canvas.drawText("Score: " + score, 40, 130 + offsetY, paint);

        // Góc phải cho đối thủ
        paint.setColor(Color.parseColor("#80000000"));
        canvas.drawRect(getWidth() - 520, 20 + offsetY, getWidth() - 20, 160 + offsetY, paint);

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Competitor ", getWidth() - 40, 80 + offsetY, paint);
        canvas.drawText("Score: " + opponentScore, getWidth() - 40, 130 + offsetY, paint);
    }


    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (thread != null) thread.setRunning(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {

            float touchX = event.getX();
            playerX = touchX - playerWidth / 2;
            playerX = Math.max(0, Math.min(playerX, getWidth() - playerWidth));
        }
        return true;
    }
}
