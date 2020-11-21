package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.ActivityCommentBinding;
import com.example.finalproject.models.Comment;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.CommentAdapter;
import com.example.finalproject.models.adapter.UserAdapter;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.viewmodel.CommentViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    private boolean isServer, isNetwork, isAvailable;
    private CommentViewModel commentViewModel;
    private List<Comment> comments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LoadingDialog.showLoadingDialog(this);

        User user = getIntent().getParcelableExtra("user");
        Course course = getIntent().getParcelableExtra("course");
        isNetwork = getIntent().getBooleanExtra("network", true);
        isServer = getIntent().getBooleanExtra("server", true);
        comments = new ArrayList<>();

        initViewModel();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoadingDialog.dismissDialog();
            setUpUI(user);
            ToeicAPI.getCommentByCourseID(this, course.getId());
        }, 1000);

        binding.btnPost.setOnClickListener(v -> {
            if (isAvailable){

            }else{

            }
        });
    }

    private void setUpUI(User user) {
        if (user != null) {
            binding.imageUser.setImageDrawable(ContextCompat.getDrawable(this, UserAdapter.images[user.getAvatar()]));
            binding.txtNameUser.setText(user.getName());
            binding.rvCommentList.setVisibility(View.VISIBLE);
            initRecyclerView();
            isAvailable = true;
        } else {
            binding.imageUser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_portrait));
            binding.txtNameUser.setText("");
            isAvailable = false;
        }
    }

    private void initViewModel(){
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        commentViewModel.getComments().observe(this, comments1 -> {
            commentAdapter.setData(comments);
        });
    }
    private void initRecyclerView() {
        commentAdapter = new CommentAdapter(this, comments);

        binding.rvCommentList.setAdapter(commentAdapter);
        binding.rvCommentList.setHasFixedSize(true);
        binding.rvCommentList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public CommentViewModel getVM(){
        return commentViewModel;
    }
}