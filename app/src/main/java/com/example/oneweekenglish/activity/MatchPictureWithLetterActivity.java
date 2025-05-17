package com.example.oneweekenglish.activity;
import android.os.Bundle;
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

public class MatchPictureWithLetterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_picture_with_letter);
        //tải các thẻ
        GridView cardGridView = findViewById(R.id.cardGridView);
        cardGridView.setAdapter(new CardAdapter());

        ImageButton closeButton = findViewById(R.id.closeButton);
        ImageButton hintButton = findViewById(R.id.hintButton);
        //đóng lại
        closeButton.setOnClickListener(v -> {
            //
        });
        //nút gợi ý
        hintButton.setOnClickListener(v -> {
            //
        });
        //nút tiếp tục, code chỉ hiện khi đã match được hết nha
        ImageButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            //
        });
    }
}
