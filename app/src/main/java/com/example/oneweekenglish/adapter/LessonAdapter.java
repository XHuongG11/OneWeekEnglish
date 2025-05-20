package com.example.oneweekenglish.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.activity.HomeActivity;
import com.example.oneweekenglish.activity.MatchPictureWithLetterActivity;
import com.example.oneweekenglish.dao.LessonDAO;
import com.example.oneweekenglish.dao.OnGetByIdListener;
import com.example.oneweekenglish.model.LearnWord;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.util.GlobalVariable;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;
    private int visiblePosition = 0;

    public LessonAdapter(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.lessonTitle.setText(lesson.getName() != null ? lesson.getName() : "Unknown Lesson");
        holder.animalImage.setImageResource(getAnimalDrawableForLesson(lesson));
        holder.lessonBox.setVisibility(position == visiblePosition ? View.VISIBLE : View.INVISIBLE);

        // Set click listener for animal image
        holder.animalImage.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            int previousPosition = visiblePosition;
            visiblePosition = currentPosition;
            if (previousPosition != -1) {
                notifyItemChanged(previousPosition);
            }
            if (visiblePosition != -1) {
                notifyItemChanged(visiblePosition);
            }
        });

        // Set click listener for start button
        holder.startButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Lesson lessonAtPosition = lessonList.get(currentPosition);

            // save current lesson
            LessonDAO lessonDAO = new LessonDAO();
            lessonDAO.getByID(lessonAtPosition.getId(), new OnGetByIdListener<Lesson>() {
                @Override
                public void onGetByID(Lesson findLesson) {
                    if (lesson != null) {
                        Log.d("DEBUG", findLesson.getId());
                        GlobalVariable.currentLesson = findLesson;
                        // Chuyển đến trang bài học
                        Intent intent = new Intent(v.getContext(), MatchPictureWithLetterActivity.class);
                        v.getContext().startActivity(intent);
                    } else {
                        Log.d("Lesson", "No lesson found with this ID.");
                    }
                }

                @Override
                public void onGetFailed(Exception e) {
                    Toast.makeText(v.getContext(), "Tải dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    private int getAnimalDrawableForLesson(Lesson lesson) {
        String key = lesson.getId() != null ? lesson.getId() : lesson.getName();
        switch (key) {
            case "1":
            case "Bài 1: Giraffe Adventure":
                return R.drawable.ic_giraffe;
            case "2":
            case "Bài 2: Monkey Puzzle":
                return R.drawable.ic_monkey;
            case "3":
            case "Bài 3: Elephant Journey":
                return R.drawable.ic_monkey;
            case "4":
            case "Bài 4: Lion Quest":
                return R.drawable.ic_pig;
            case "5":
            case "Bài 5: Tiger Tale":
                return R.drawable.ic_monkey;
            default:
                return R.drawable.ic_monkey;
        }
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        ImageView animalImage;
        LinearLayout lessonBox;
        TextView lessonTitle;
        ImageButton startButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            animalImage = itemView.findViewById(R.id.animalImage);
            lessonBox = itemView.findViewById(R.id.lessonBox);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }
}