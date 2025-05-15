package com.example.oneweekenglish.dao;

import android.util.Log;

import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void create(User user,OnSaveUpdateListener listener)
    {
        try{
            user.setId(UUID.randomUUID().toString());
            db.collection("users")
                    .document(user.getId())
                    .set(user)
                    .addOnSuccessListener(documentReference  -> {
                        listener.onComplete(true);
                        Log.w("Firebase", "Success saving user");
                    })
                    .addOnFailureListener(
                            e ->
                            {
                                listener.onComplete(false);
                                Log.w("Firebase", "Error saving user", e);
                            }
                    );
        }catch (Exception ex){
            Log.d("CREATE_USER",ex.getMessage());
        }
    }


    public void getByEmail(String email, OnGetByIdListener<User> listener)
    {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(!documentSnapshot.isEmpty()){
                        DocumentSnapshot doc = documentSnapshot.getDocuments().get(0);
                        User user = doc.toObject(User.class);
                        listener.onGetByID(user);
                    }
                    else{
                        listener.onGetByID(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting user by ID", e);
                    listener.onGetByID(null);
                });
    }

    public void getAll(OnGetAllListener<User> listener) {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                        listener.onGetAll(userList);
                    } else {
                        listener.onGetAll(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error getting all users", e);
                    listener.onGetAll(Collections.emptyList());
                });
    }

    public void update(String email, User user, OnSaveUpdateListener listener) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        String docID = doc.getId();

                        db.collection("users")
                                .document(docID)
                                .set(user)
                                .addOnSuccessListener(runnable -> {
                                    Log.d("Firebase", "User updated successfully");
                                    listener.onComplete(true);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firebase", "Error updating user", e);
                                    listener.onComplete(false);
                                });
                    }
                    else {
                        listener.onComplete(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error finding user by email", e);
                    listener.onComplete(false);
                });
    }

}
