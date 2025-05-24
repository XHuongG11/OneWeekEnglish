package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;

import java.util.Objects;

public class EndGameActivity extends AppCompatActivity {
    private ImageView imgResult;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_end_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgResult = findViewById(R.id.imgResultGame);
        txtResult = findViewById(R.id.txtResultGame);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String result = intent.getStringExtra("RESULT");

        if(Objects.equals(result, "WIN")){
            Glide.with(this).load("https://res.cloudinary.com/dvjxenags/image/upload/v1747843483/win_yroesm.gif")
                            .into(imgResult);
            txtResult.setText("Bạn đã chiến thắng");
        }
        else{
            Glide.with(this).load("https://res.cloudinary.com/dvjxenags/image/upload/v1747843483/lose_uws8ct.gif")
                    .into(imgResult);
            txtResult.setText("Bạn đã thua rồi");
        }

    }
}