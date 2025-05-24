package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;

public class GameActivity extends AppCompatActivity {

    private ConstraintLayout btnCatchlistGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        ConstraintLayout llSportQuiz = findViewById(R.id.llSportQuiz);
        llSportQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện click ở đây
                //Toast.makeText(GameActivity.this, "Sport Quiz clicked!", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(MainActivity.this, SportQuizActivity.class));
            }
        });

        btnCatchlistGame = findViewById(R.id.llSportQuiz);
        btnCatchlistGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaitingRoomActivity.class);
            startActivity(intent);
        });

        // Load image into ImageView using Glide with hardcoded Cloudinary URL
        ImageView ivSportQuiz = findViewById(R.id.ivSportQuiz);
        String imageUrl = "https://res.cloudinary.com/dvjxenags/image/upload/v1747884646/output-onlinegiftools_2_dspmrj.gif";
        Glide.with(this)
                .load(imageUrl)
                .into(ivSportQuiz);
        ImageView commingsoon = findViewById(R.id.commingsoon);
        String imageUrl1 = "https://res.cloudinary.com/dvjxenags/image/upload/v1747884210/output-onlinegiftools_u2axpk.gif";
        Glide.with(this)
                .load(imageUrl1)
                .into(commingsoon);
        ImageView game1 = findViewById(R.id.game1);
        String imageUrl2 = "https://res.cloudinary.com/dvjxenags/image/upload/v1747885109/lollipop-unscreen_pzkpov.gif";
        Glide.with(this)
                .load(imageUrl2)
                .into(game1);
        ImageView game2 = findViewById(R.id.game2);
        String imageUrl3 = "https://res.cloudinary.com/dvjxenags/image/upload/v1747885775/dolphin-unscreen_jyen94.gif";
        Glide.with(this)
                .load(imageUrl3)
                .into(game2);
        ImageView game3 = findViewById(R.id.game3);
        String imageUrl4 = "https://res.cloudinary.com/dvjxenags/image/upload/v1747885408/candy-unscreen_kuvmi1.gif";
        Glide.with(this)
                .load(imageUrl4)
                .into(game3);
        ImageView game4 = findViewById(R.id.game4);
        String imageUrl5 = "https://res.cloudinary.com/dvjxenags/image/upload/v1747886053/cubes-unscreen_fib03n.gif";
        Glide.with(this)
                .load(imageUrl5)
                .into(game4);


    }
}