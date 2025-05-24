package com.example.oneweekenglish.model;

public class Player {
    private String uid;
    private String name;
    private long score;

    public Player() {}

    public Player(String uid, String name) {
        this.uid = uid;
        this.name = name;
        this.score = 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
    public float x;
    public  float width;
    public  float height;

    public Player(float startX, float width, float height) {
        this.x = startX;
        this.width = width;
        this.height = height;
    }

    public void moveTo(float touchX, float screenWidth) {
        this.x = touchX - width / 2;
        this.x = Math.max(0, Math.min(this.x, screenWidth - width));
    }

    public boolean isCatching(FlyingWord word, float screenHeight) {
        float wordBottomY = word.y;
        float playerTopY = screenHeight - height;

        return wordBottomY >= playerTopY &&
                word.x >= x &&
                word.x <= x + width;
    }
}

