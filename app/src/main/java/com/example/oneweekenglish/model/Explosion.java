package com.example.oneweekenglish.model;

public class Explosion {
    public float x, y;
    public int life = 10; // số frame tồn tại

    public Explosion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        life--;
    }

    public boolean isDead() {
        return life <= 0;
    }
}
