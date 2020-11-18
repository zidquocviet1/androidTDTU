package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.finalproject.databinding.ActivityUserBinding;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.UserAdapter;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User user = getIntent().getParcelableExtra("user");
        if (user != null){
            binding.imgAvatar.setImageDrawable(getDrawable(UserAdapter.images[user.getAvatar()]));
        }else{
            binding.imgAvatar.setImageDrawable(getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait));
        }
    }
}