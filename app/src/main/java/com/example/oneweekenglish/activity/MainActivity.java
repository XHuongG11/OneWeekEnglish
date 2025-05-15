package com.example.oneweekenglish.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.dao.UserDAO;
import com.example.oneweekenglish.model.User;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        UserDAO userDAO = new UserDAO();
        User user = new User("22110156@student.hcmute.edu.vn", "Xuân Hương", "123456");
        userDAO.create(user, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Tạo user thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tạo user thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}