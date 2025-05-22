package com.example.oneweekenglish.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.LessonAdapter;
import com.example.oneweekenglish.dao.LessonDAO;
import com.example.oneweekenglish.dao.OnSaveUpdateListener;
import com.example.oneweekenglish.model.EPracticeType;
import com.example.oneweekenglish.model.FillBlank;
import com.example.oneweekenglish.model.Grammar;
import com.example.oneweekenglish.model.LearnWord;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.model.MatchWord;
import com.example.oneweekenglish.model.Question;
import com.example.oneweekenglish.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView lessonRecyclerView;
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Khởi tạo MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.background_meditation);
        mediaPlayer.setLooping(true); // Nhạc lặp lại
        mediaPlayer.start();

        // Initialize RecyclerView
        lessonRecyclerView = findViewById(R.id.lessonRecyclerView);
        lessonRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize lesson data
        lessonList = getLessonData();

        // Set up adapter
        lessonAdapter = new LessonAdapter(lessonList,getApplicationContext());
        lessonRecyclerView.setAdapter(lessonAdapter);

//        createDatabase();
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
    private List<Lesson> getLessonData() {
        List<Lesson> lessons = new ArrayList<>();
        // Hardcoded sample data (replace with database query)
        lessons.add(new Lesson("29e1a694-f65d-426c-a3cc-fae90ef01602", "Lesson 1: Animals", null, null, null, null));
        lessons.add(new Lesson("2", "Bài 2: Activity", null, null, null, null));
        lessons.add(new Lesson("3", "Bài 3: Tao cau", null, null, null, null));
        lessons.add(new Lesson("4", "Cooming soon", null, null, null, null));
        lessons.add(new Lesson("5", "Cooming soon", null, null, null, null));
        return lessons;
    }

    private void createDatabase(){
        // Lesson 1: Động vật
        Lesson lesson1 = new Lesson();
        lesson1.setId(UUID.randomUUID().toString());
        lesson1.setName("Động vật");

        // Practice 101: LEARN_WORD
        LearnWord learnWord = new LearnWord();
        learnWord.setId(UUID.randomUUID().toString());
        learnWord.setType(EPracticeType.LEARN_WORD);
        learnWord.setWords(Arrays.asList(
                new Word("dog", Arrays.asList("chó"), Arrays.asList("a domesticated carnivorous mammal"), "/dɒɡ/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("cat", Arrays.asList("mèo"), Arrays.asList("a small domesticated carnivorous mammal with soft fur"), "/kæt/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663323/cat_uylk2d.gif", "noun"),
                new Word("rabbit", Arrays.asList("thỏ"), Arrays.asList("a small mammal with long ears, soft fur, and a short tail"), "/\u02c8r\u00e6b.\u026at/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663327/bunny_fgvweu.png", "noun"),
                new Word("bear", Arrays.asList("gấu"), Arrays.asList("a large heavy mammal with thick fur and a short tail"), "/be\u0259r/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663327/bear_dlcsuf.gif", "noun"),
                new Word("tiger", Arrays.asList("hổ"), Arrays.asList("a large carnivorous feline mammal with a yellow-brown coat striped with black"), "/\u02c8ta\u026a.\u0261\u0259r/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/tiger_abe5bo.gif", "noun")
        ));
        lesson1.setLearnWordPractice(learnWord);

        // Practice 102: MATCH_WORD
        MatchWord matchWord = new MatchWord();
        matchWord.setId(UUID.randomUUID().toString());
        matchWord.setType(EPracticeType.MATCH_WORD);
        matchWord.setWords(Arrays.asList(
                new Word("lion", Arrays.asList("sư tử"), Arrays.asList("a large tawny-colored big cat"), "/\u02c8la\u026a.\u0259n/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("giraffe", Arrays.asList("hươu cao cổ"), Arrays.asList("a tall African mammal with a very long neck and legs"), "/d\u0292\u026a\u02c8r\u00e6f/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("zebra", Arrays.asList("ngựa vằn"), Arrays.asList("an African wild horse with black-and-white stripes"), "/\u02c8zi\u02d0.br\u0259/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("kangaroo", Arrays.asList("chuột túi"), Arrays.asList("a large plant-eating Australian marsupial that moves by jumping"), "/\u02c8k\u00e6\u014b.\u0261\u0259\u02c8ru\u02d0/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun")
        ));
        lesson1.setMatchWordPractice(matchWord);

        // Practice 103: FILL_BLANK
        FillBlank fillBlank = new FillBlank();
        fillBlank.setId(UUID.randomUUID().toString());
        fillBlank.setType(EPracticeType.FILL_BLANK);
        fillBlank.setWords(Arrays.asList(
                new Word("Cat", Arrays.asList("Mèo"), Arrays.asList("A small domesticated carnivorous mammal", "A feline animal"), "/kæt/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("Pig", Arrays.asList("Lợn"), Arrays.asList("A domesticated hoofed mammal", "An animal kept for its meat (pork)"), "/pɪɡ/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("Monkey", Arrays.asList("Khỉ"), Arrays.asList("A small to medium-sized primate", "An intelligent animal that lives in trees"), "/ˈmʌŋ.ki/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun"),
                new Word("Run", Arrays.asList("Chạy"), Arrays.asList("Move at a speed faster than a walk", "To operate or function"), "/rʌn/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "verb"),
                new Word("Elephant", Arrays.asList("Voi"), Arrays.asList("A large herbivorous mammal"), "/ˈɛl.ɪ.fənt/", "https://res.cloudinary.com/dvjxenags/image/upload/v1747663322/dog_t7wtjy.gif", "noun")
        ));
        lesson1.setFillBlankPractice(fillBlank);

        // Practice 104: GRAMMAR
        Grammar grammar = new Grammar();
        grammar.setId(UUID.randomUUID().toString());
        grammar.setType(EPracticeType.GRAMMAR);
        grammar.setQuestions(Arrays.asList(
                new Question("1", "https://res.cloudinary.com/dvjxenags/image/upload/v1747497051/monkey_love_banana_vqvpzv.gif", "Monkeys like bananas"),
                new Question("2", "https://res.cloudinary.com/dvjxenags/image/upload/v1747496464/the_bird_can_fly_rlt6fs.gif", "The bird can fly"),
                new Question("3", "https://res.cloudinary.com/dvjxenags/image/upload/v1747487549/i_like_dog_pk8xja.png", "I like dog"),
                new Question("4", "https://res.cloudinary.com/dvjxenags/image/upload/v1747498090/lion_is_strong_qtrhsd.jpg", "A lion is strong")
        ));
        lesson1.setGrammarPractice(grammar);

        LessonDAO lessonDAO = new LessonDAO();
        lessonDAO.create(lesson1, new OnSaveUpdateListener() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    Log.d("Firebase", "Lesson saved successfully.");
                } else {
                    Log.d("Firebase", "Failed to save lesson.");
                }
            }
        });
    }
}