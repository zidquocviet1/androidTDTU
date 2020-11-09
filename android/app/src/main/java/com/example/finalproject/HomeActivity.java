package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogout.setOnClickListener((view) -> {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));

            User user = MyDatabase.getInstance(this).getUserDAO().getUser().get(0);
            user.setLogin(false);
            MyDatabase.getInstance(this).getUserDAO().updateUser(user);
        });
    }
}