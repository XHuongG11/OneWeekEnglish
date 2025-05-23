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
import com.example.oneweekenglish.dao.OnGetByIdListener;
import com.example.oneweekenglish.dao.UserDAO;
import com.example.oneweekenglish.model.User;

public class SignInActivity extends AppCompatActivity {

    private ImageButton buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        editTextEmail = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        userDAO = new UserDAO();



//        TextView forgotpass = findViewById(R.id.forgotpass);
//        SpannableString content1 = new SpannableString("Forgot Password?");
//        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
//        forgotpass.setText(content1);

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


        buttonLogin = findViewById(R.id.signInButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                login(email,password);
            }
        });

    }
    private void login(String email, String password) {

        userDAO.getByEmailAndPassword(email, password, new OnGetByIdListener<User>() {
            @Override
            public void onGetByID(User user) {
                if (user != null) {
                    // Đăng nhập thành công
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình chính hoặc hoạt động khác nếu cần
                    saveUserToSharedPreferences(user);
                     Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                     startActivity(intent);
                     finish();
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu hoặc email", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onGetFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveUserToSharedPreferences(User user) {
        // Chuyển sang SharedPreferences tên là "UserPrefs"
        getSharedPreferences("CurentUser", MODE_PRIVATE)
                .edit()
                .putString("userId", user.getId()) // tùy vào kiểu dữ liệu của ID
                .putString("email", user.getEmail())
                .putString("fullName", user.getFullName())
                .apply();
    }

}