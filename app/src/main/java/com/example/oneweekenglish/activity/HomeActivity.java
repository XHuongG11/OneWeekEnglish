package com.example.oneweekenglish.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.LessonAdapter;
import com.example.oneweekenglish.model.Lesson;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView lessonRecyclerView;
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize RecyclerView
        lessonRecyclerView = findViewById(R.id.lessonRecyclerView);
        lessonRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize lesson data
        lessonList = getLessonData();

        // Set up adapter
        lessonAdapter = new LessonAdapter(lessonList);
        lessonRecyclerView.setAdapter(lessonAdapter);
    }

    private List<Lesson> getLessonData() {
        List<Lesson> lessons = new ArrayList<>();
        // Hardcoded sample data (replace with database query)
        lessons.add(new Lesson("1", "Bài 1: Family", null, null, null, null));
        lessons.add(new Lesson("2", "Bài 2: Activity", null, null, null, null));
        lessons.add(new Lesson("3", "Bài 3: Tao cau", null, null, null, null));
        lessons.add(new Lesson("4", "Bài 4: Cooming soon", null, null, null, null));
        lessons.add(new Lesson("5", "Bài 5: Cooming soon", null, null, null, null));
        return lessons;
    }
}