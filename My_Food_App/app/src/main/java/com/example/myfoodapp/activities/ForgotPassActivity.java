package com.example.myfoodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.R;
import com.example.myfoodapp.controller.userController;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSend;
    private userController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        userController = new userController(this);

        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            String newPassword = userController.resetPassword(email);
            if (newPassword != null) {
                // Hiển thị mật khẩu mới bằng Toast
                Toast.makeText(this, "Mật khẩu mới của bạn đã được gửi ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back_to_login(View view) {
        startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
        finish();
    }
}
