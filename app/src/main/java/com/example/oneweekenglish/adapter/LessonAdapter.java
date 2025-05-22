package com.example.oneweekenglish.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
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
import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.activity.MatchPictureWithLetterActivity;
import com.example.oneweekenglish.dao.LessonDAO;
import com.example.oneweekenglish.dao.OnGetByIdListener;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.util.GlobalVariable;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;
    private int visiblePosition = 0;
    private Context context;

    public LessonAdapter(List<Lesson> lessonList, Context context) {
        this.lessonList = lessonList;
        this.context = context;
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

        // Load image using Glide
        String imageUrl = getAnimalImageUrlForLesson(lesson);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_monkey) // Fallback image while loading
                .error(R.drawable.ic_monkey) // Fallback image if loading fails
                .into(holder.animalImage);

        // Set visibility for lessonBox and clickMeBox
        boolean isVisible = position == visiblePosition;
        holder.lessonBox.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        holder.clickMeBox.setVisibility(isVisible ? View.GONE : View.VISIBLE);

        // Apply bouncing animation to clickMeBox when visible
        if (holder.clickMeBox.getVisibility() == View.VISIBLE) {
            if (holder.clickMeBox.getTag() instanceof ObjectAnimator) {
                ((ObjectAnimator) holder.clickMeBox.getTag()).cancel();
            }
            ObjectAnimator bounceAnimator = ObjectAnimator.ofFloat(
                    holder.clickMeBox,
                    "translationY",
                    0f, -20f, 0f
            );
            bounceAnimator.setDuration(800);
            bounceAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            bounceAnimator.setRepeatMode(ObjectAnimator.RESTART);
            bounceAnimator.start();
            holder.clickMeBox.setTag(bounceAnimator);
        } else {
            if (holder.clickMeBox.getTag() instanceof ObjectAnimator) {
                ((ObjectAnimator) holder.clickMeBox.getTag()).cancel();
                holder.clickMeBox.setTag(null);
            }
            holder.clickMeBox.setTranslationY(0f);
        }

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
            SoundPool soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .build();

            int soundId = soundPool.load(context, R.raw.click_button_letter, 1);
            soundPool.setOnLoadCompleteListener((sp, id, status) -> {
                if (status == 0) {
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                }
            });
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Lesson lessonAtPosition = lessonList.get(currentPosition);

            // Save current lesson
            LessonDAO lessonDAO = new LessonDAO();
            lessonDAO.getByID(lessonAtPosition.getId(), new OnGetByIdListener<Lesson>() {
                @Override
                public void onGetByID(Lesson findLesson) {
                    if (findLesson != null) {
                        Log.d("DEBUG", findLesson.getId());
                        GlobalVariable.currentLesson = findLesson;
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

    private String getAnimalImageUrlForLesson(Lesson lesson) {
        String key = lesson.getId() != null ? lesson.getId() : lesson.getName();
        switch (key) {
            case "29e1a694-f65d-426c-a3cc-fae90ef01602":
            case "Bài 1: Giraffe Adventure":
                return "https://res.cloudinary.com/dvjxenags/image/upload/v1747884889/output-onlinegiftools_3_qy2llj.gif";
            case "2":
            case "Bài 2: Monkey Puzzle":
                return "https://res.cloudinary.com/dvjxenags/image/upload/v1747896412/output-onlinegiftools_4_nmzg0d.gif";
            case "3":
            case "Bài 3: Elephant Journey":
                return "https://example.com/images/elephant.jpg";
            case "4":
            case "Bài 4: Lion Quest":
                return "https://res.cloudinary.com/dvjxenags/image/upload/v1747896894/output-onlinegiftools_5_qse52u.gif";
            case "5":
            case "Bài 5: Tiger Tale":
                return "https://res.cloudinary.com/dvjxenags/image/upload/v1747896979/output-onlinegiftools_6_oti2ya.gif";
            default:
                return "https://example.com/images/default.jpg";
        }
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        ImageView animalImage;
        LinearLayout lessonBox;
        TextView lessonTitle;
        ImageButton startButton;
        TextView clickMeBox;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            animalImage = itemView.findViewById(R.id.animalImage);
            lessonBox = itemView.findViewById(R.id.lessonBox);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            startButton = itemView.findViewById(R.id.startButton);
            clickMeBox = itemView.findViewById(R.id.clickMeBox);
        }
    }
}