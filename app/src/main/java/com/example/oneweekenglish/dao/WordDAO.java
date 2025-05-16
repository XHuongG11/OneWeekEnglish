package com.example.oneweekenglish.dao;

import android.util.Log;

import com.example.oneweekenglish.model.User;
import com.example.oneweekenglish.model.Word;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WordDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void create(Word word, OnSaveUpdateListener listener)
    {
        try{
            word.setId(UUID.randomUUID().toString());
            db.collection("words")
                    .document(word.getId())
                    .set(word)
                    .addOnSuccessListener(documentReference  -> {
                        listener.onComplete(true);
                        Log.w("Firebase", "Success saving word");
                    })
                    .addOnFailureListener(
                            e ->
                            {
                                listener.onComplete(false);
                                Log.w("Firebase", "Error saving word", e);
                            }
                    );
        }catch (Exception ex){
            Log.d("CREATE_word",ex.getMessage());
        }
    }

    public void getByID(String wordID, OnGetByIdListener<Word> listener)
    {
        db.collection("words")
                .document(wordID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Word word = documentSnapshot.toObject(Word.class);
                        listener.onGetByID(word);
                    } else {
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting word by ID", e);
                    listener.onGetByID(null);
                });
    }
    public void getAll(OnGetAllListener<Word> listener) {
        db.collection("words")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Word> words = queryDocumentSnapshots.toObjects(Word.class);
                        listener.onGetAll(words);
                    } else {
                        listener.onGetAll(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting all words", e);
                    listener.onGetAll(Collections.emptyList());
                });
    }
}
