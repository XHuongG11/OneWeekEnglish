package com.example.oneweekenglish.model;

public class Bullet {
    public float x, y;
    public float speedY = -20f;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speedY;
    }

    public boolean isOutOfScreen() {
        return y < 0;
    }
}

