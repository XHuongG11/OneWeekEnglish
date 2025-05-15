package com.example.oneweekenglish.dao;

import android.util.Log;

import com.example.oneweekenglish.model.Lesson;
import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LessonDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void create(Lesson lesson, OnSaveUpdateListener listener)
    {
        try{
            lesson.setId(UUID.randomUUID().toString());
            db.collection("lessons")
                    .document(lesson.getId())
                    .set(lesson)
                    .addOnSuccessListener(documentReference  -> {
                        listener.onComplete(true);
                        Log.w("Firebase", "Success saving lesson");
                    })
                    .addOnFailureListener(
                            e ->
                            {
                                listener.onComplete(false);
                                Log.w("Firebase", "Error saving lesson", e);
                            }
                    );
        }catch (Exception ex){
            Log.d("CREATE_lesson",ex.getMessage());
        }
    }


    public void getByName(String name, OnGetByIdListener<Lesson> listener)
    {
        db.collection("lessons")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(!documentSnapshot.isEmpty()){
                        DocumentSnapshot doc = documentSnapshot.getDocuments().get(0);
                        Lesson lesson = doc.toObject(Lesson.class);
                        listener.onGetByID(lesson);
                    }
                    else{
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting lesson by ID", e);
                    listener.onGetByID(null);
                });
    }

    public void getAll(OnGetAllListener<Lesson> listener) {
        db.collection("lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Lesson> lessonList = queryDocumentSnapshots.toObjects(Lesson.class);
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

    public void getByID(String lessonID, OnGetByIdListener<Lesson> listener)
    {
        db.collection("lessons")
                .document(lessonID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Lesson lesson = documentSnapshot.toObject(Lesson.class);
                        listener.onGetByID(lesson);
                    } else {
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting lesson by ID", e);
                    listener.onGetByID(null);
                });
    }

    public void update(String lessonID, Lesson lesson, OnSaveUpdateListener listener) {
        db.collection("lessons")
                .document(lessonID)
                .set(lesson)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Lesson updated successfully");
                    listener.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating lesson", e);
                    listener.onComplete(false);
                });
    }
}
