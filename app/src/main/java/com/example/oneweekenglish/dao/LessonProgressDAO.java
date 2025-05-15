package com.example.oneweekenglish.dao;

import android.util.Log;

import com.example.oneweekenglish.model.LearningProgress;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.model.LessonProgress;
import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LessonProgressDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void create(LessonProgress lessonProgress, OnSaveUpdateListener listener)
    {
        try{
            lessonProgress.setId(UUID.randomUUID().toString());
            db.collection("lessonProgresses")
                    .document(lessonProgress.getId())
                    .set(lessonProgress)
                    .addOnSuccessListener(documentReference  -> {
                        listener.onComplete(true);
                        Log.w("Firebase", "Success saving lesson");
                    })
                    .addOnFailureListener(
                            e ->
                            {
                                listener.onComplete(false);
                                Log.w("Firebase", "Error saving lessonProgress", e);
                            }
                    );
        }catch (Exception ex){
            Log.d("CREATE_lessonProgress",ex.getMessage());
        }
    }



    public void getAll(OnGetAllListener<LessonProgress> listener) {
        db.collection("lessonProgresses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<LessonProgress> lessonProgresses = queryDocumentSnapshots.toObjects(LessonProgress.class);
                        listener.onGetAll(lessonProgresses);
                    } else {
                        listener.onGetAll(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting all lessonProgresses", e);
                    listener.onGetAll(Collections.emptyList());
                });
    }

    public void getByID(String lessonProgressID, OnGetByIdListener<LearningProgress> listener)
    {
        db.collection("lessonProgresses")
                .document(lessonProgressID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        LearningProgress learningProgress = documentSnapshot.toObject(LearningProgress.class);
                        listener.onGetByID(learningProgress);
                    } else {
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting user by ID", e);
                    listener.onGetByID(null);
                });
    }

    public void update(String lessonProgressID, LessonProgress lessonProgress, OnSaveUpdateListener listener) {
        db.collection("lessonProgresses")
                .document(lessonProgressID)
                .set(lessonProgress)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "LessonProgress updated successfully");
                    listener.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating lessonProgress", e);
                    listener.onComplete(false);
                });
    }
}
