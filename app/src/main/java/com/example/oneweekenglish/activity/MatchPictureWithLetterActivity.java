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
import com.example.oneweekenglish.model.Word;

import java.util.Arrays;
import java.util.List;

public class MatchPictureWithLetterActivity extends AppCompatActivity {

        public static List<Word> getWordList() {
            return Arrays.asList(
                    new Word(
                            "1",
                            "ability",
                            Arrays.asList("khả năng", "năng lực"),
                            Arrays.asList("Possession of the means or skill to do something", "Natural talent or acquired skill"),
                            "əˈbɪl.ə.ti",
                            "https://images.unsplash.com/photo-1506362802973-bd1717de901c?auto=format&fit=crop&w=800&q=60",
                            "noun"
                    ),
                    new Word(
                            "2",
                            "accident",
                            Arrays.asList("tai nạn", "biến cố"),
                            Arrays.asList("An unfortunate incident that happens unexpectedly", "Something that occurs by chance"),
                            "ˈæk.sɪ.dənt",
                            "https://images.unsplash.com/photo-1598899134739-24c46f58c5cf?auto=format&fit=crop&w=800&q=60",
                            "noun"
                    ),
                    new Word(
                            "3",
                            "beautiful",
                            Arrays.asList("xinh đẹp", "tuyệt vời"),
                            Arrays.asList("Pleasing the senses or mind aesthetically", "Of a very high standard"),
                            "ˈbjuː.tɪ.fəl",
                            "https://images.unsplash.com/photo-1504198266285-1653e5c6e33b?auto=format&fit=crop&w=800&q=60",
                            "adjective"
                    ),
                    new Word(
                            "4",
                            "create",
                            Arrays.asList("tạo ra", "sáng tạo"),
                            Arrays.asList("Bring something into existence", "Cause something to happen as a result of one’s actions"),
                            "kriˈeɪt",
                            "https://images.unsplash.com/photo-1581090700227-1e8c03abfd3c?auto=format&fit=crop&w=800&q=60",
                            "verb"
                    ),
                    new Word(
                            "5",
                            "quickly",
                            Arrays.asList("nhanh chóng", "ngay lập tức"),
                            Arrays.asList("At a fast speed", "Without delay"),
                            "ˈkwɪk.li",
                            "https://images.unsplash.com/photo-1520880867055-1e30d1cb001c?auto=format&fit=crop&w=800&q=60",
                            "adverb"
                    )
            );
        }
        private CardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_picture_with_letter);
        //tải các thẻ
        adapter = new CardAdapter(getWordList());
        GridView cardGridView = findViewById(R.id.cardGridView);
        cardGridView.setAdapter(adapter);

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

        //setup adapter
        setupAdapter();
    }
    private void setupAdapter(){
        adapter.setOnCardClickListener(new CardAdapter.OnCardClickListener() {
            private Word firstSelected = null;
            private int firstPosition = -1;

            @Override
            public void onCardClick(int position, Word word, int viewType) {
                if (firstSelected == null) {
                    // Lần chọn đầu tiên
                    firstSelected = word;
                    firstPosition = position;
                } else {
                    if (firstSelected.getId().equals(word.getId()) && position != firstPosition) {
                        // Đúng
                        Toast.makeText(getApplicationContext(), "Đúng rồi!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Sai
                        Toast.makeText(getApplicationContext(), "Sai rồi!", Toast.LENGTH_SHORT).show();
                    }
                    // Reset để chọn lại
                    firstSelected = null;
                    firstPosition = -1;
                }
            }
        });

    }
}
