package com.example.oneweekenglish.dao;

import android.util.Log;

import com.example.oneweekenglish.model.LearningProgress;
import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LearningProgressDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void create(LearningProgress learningProgress, OnSaveUpdateListener listener)
    {
        try{
            learningProgress.setId(UUID.randomUUID().toString());
            db.collection("learningProgresses")
                    .document(learningProgress.getId())
                    .set(learningProgress)
                    .addOnSuccessListener(documentReference  -> {
                        listener.onComplete(true);
                        Log.w("Firebase", "Success saving learningProgress");
                    })
                    .addOnFailureListener(
                            e ->
                            {
                                listener.onComplete(false);
                                Log.w("Firebase", "Error saving learningProgresses", e);
                            }
                    );
        }catch (Exception ex){
            Log.d("CREATE_learningProgress",ex.getMessage());
        }
    }


    public void getByUser(User user, OnGetByIdListener<LearningProgress> listener)
    {
        db.collection("learningProgresses")
                .whereEqualTo("user.email", user.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(!documentSnapshot.isEmpty()){
                        DocumentSnapshot doc = documentSnapshot.getDocuments().get(0);
                        LearningProgress lesson = doc.toObject(LearningProgress.class);
                        listener.onGetByID(lesson);
                    }
                    else{
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting learningProgress by ID", e);
                    listener.onGetByID(null);
                });
    }

    public void getAll(OnGetAllListener<LearningProgress> listener) {
        db.collection("learningProgresses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<LearningProgress> lessonList = queryDocumentSnapshots.toObjects(LearningProgress.class);
                        listener.onGetAll(lessonList);
                    } else {
                        listener.onGetAll(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting all lessons", e);
                    listener.onGetAll(Collections.emptyList());
                });
    }

    public void getByID(String learningProgressID, OnGetByIdListener<LearningProgress> listener)
    {
        db.collection("learningProgresses")
                .document(learningProgressID)
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

    public void update(String learningProgressID, LearningProgress learningProgress, OnSaveUpdateListener listener) {
        db.collection("learningProgresses")
                .document(learningProgressID)
                .set(learningProgress)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "LearningProgress updated successfully");
                    listener.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating LearningProgress", e);
                    listener.onComplete(false);
                });
    }
}
