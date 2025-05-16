package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oneweekenglish.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        TextView forgotpass = findViewById(R.id.forgotpass);
        SpannableString content1 = new SpannableString("Forgot Password?");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        forgotpass.setText(content1);

        TextView moveSignUp = findViewById(R.id.moveSignUp);
        SpannableString content = new SpannableString("Don't have an account?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        moveSignUp.setText(content);
        moveSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}