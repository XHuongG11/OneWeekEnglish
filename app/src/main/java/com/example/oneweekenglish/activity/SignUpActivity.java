package com.example.oneweekenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.dao.OnSaveUpdateListener;
import com.example.oneweekenglish.dao.UserDAO;
import com.example.oneweekenglish.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private UserDAO userDAO;
    private ImageButton buttonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        userDAO = new UserDAO();

        TextView moveSignIn = findViewById(R.id.moveSignIn);
        SpannableString content = new SpannableString("You have an account");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        moveSignIn.setText(content);
        moveSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                signup(fullName,email,password);
            }
        });

    }
    private void signup(String fullName, String email, String password){
        if(isValidInput(email,password)){
            User user = new User(email,fullName,password);
            userDAO.create(user, (isSuccess) -> {
                if(isSuccess){
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công, vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại, vui lòng kiểm tra thông tin", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean isValidInput(String email, String password) {
        if (email.isEmpty()) {
            editTextEmail.setError("Yêu cầu email");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Sai định dạng email");
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Yêu cầu mật khẩu");
            return false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 6 chữ số");
            return false;
        }

        return true;
    }
}