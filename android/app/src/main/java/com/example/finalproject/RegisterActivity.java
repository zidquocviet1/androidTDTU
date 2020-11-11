package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.ActivityRegisterBinding;
import com.example.finalproject.models.User;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.view.LoadingIndicator;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignup.setOnClickListener((view) -> {
            String username = binding.txtLayoutUsername.getEditText().getText().toString();
            String password = binding.txtLayoutPassword.getEditText().getText().toString();
            String confirmPassword = binding.txtLayoutConfirm.getEditText().getText().toString();

            if (confirmPassword.equals(password)){
                String id = UUID.randomUUID().toString();
                User user = new User(id, username, password, "default", false);
                UserAPI.register(this, user);
            }else{
                Toast.makeText(this, "Mật khẩu không trùng khớp. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void navigate(String message){
        new Handler().postDelayed(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            this.finish();
        }, 2000);
    }

    public void onRegisterFail(String message){
        new Handler().postDelayed(() -> {
            LoadingDialog.dismissDialog();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }, 2000);
    }
}