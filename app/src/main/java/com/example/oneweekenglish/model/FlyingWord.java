package com.example.oneweekenglish.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FlyingWord {
    public String word;
    public String meaning;
    public float x, y;
    public int speed;

    public FlyingWord(String word, String meaning, float x, float y, int speed) {
        this.word = word;
        this.meaning = meaning;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public FlyingWord() {
    }
    public void draw(Canvas canvas, Paint paint) {
        float radius = 80; // bán kính bóng
        paint.setColor(Color.parseColor("#FFEB3B")); // màu vàng bóng
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius, paint);

        // Viền ngoài cho đẹp
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(x, y, radius, paint);

        // Chữ trong bóng
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(word, x, y + 15, paint);

//        x += Math.sin(y / 15.0) * 2; // lắc nhẹ
//        y += speed;
    }

}
