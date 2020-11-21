package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.ActivityCommentBinding;
import com.example.finalproject.models.Comment;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.CommentAdapter;
import com.example.finalproject.models.adapter.UserAdapter;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.viewmodel.CommentViewModel;
import com.example.finalproject.viewmodel.ToeicViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityCommentBinding binding;
    private boolean isServer, isNetwork, isAvailable;
    private CommentViewModel commentViewModel;
    private List<Comment> comments;
    private CommentAdapter commentAdapter;
    private User user;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LoadingDialog.showLoadingDialog(this);

        user = getIntent().getParcelableExtra("user");
        course = getIntent().getParcelableExtra("course");
        isNetwork = getIntent().getBooleanExtra("network", true);
        isServer = getIntent().getBooleanExtra("server", true);
        comments = new ArrayList<>();

        initRecyclerView();
        initViewModel();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoadingDialog.dismissDialog();
            setUpUI(user);
            ToeicAPI.getCommentByCourseID(this, course.getId());
        }, 3000);

        binding.btnPost.setOnClickListener(this);
        binding.back.setOnClickListener(this);
    }

    public void checkNetworkAndServer(boolean isAvailable) {
        if (!NetworkController.isOnline(this)) {
            binding.txtError.setText(getString(R.string.connection));
            binding.txtError.setVisibility(View.VISIBLE);
            binding.rvCommentList.setVisibility(View.GONE);
            binding.btnPost.setEnabled(false);
        } else if (NetworkController.isOnline(this) && !isAvailable) {
            binding.txtError.setText(getString(R.string.server_error));
            binding.txtError.setVisibility(View.VISIBLE);
            binding.rvCommentList.setVisibility(View.GONE);
            binding.btnPost.setEnabled(false);
        } else {
            binding.txtError.setVisibility(View.GONE);
            binding.rvCommentList.setVisibility(View.VISIBLE);
            binding.btnPost.setEnabled(true);
        }
    }

    private void setUpUI(User user) {
        if (user != null) {
            binding.imageUser.setImageDrawable(ContextCompat.getDrawable(this, UserAdapter.images[user.getAvatar()]));
            binding.txtNameUser.setText(user.getName());
            isAvailable = true;
        } else {
            binding.imageUser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_portrait));
            binding.txtNameUser.setText("");
            isAvailable = false;
        }
    }

    private void initViewModel() {
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        commentViewModel.getComments().observe(this, comments1 -> {
            if (comments1 != null)
                commentAdapter.setData(comments1);
        });
    }

    private void initRecyclerView() {
        commentAdapter = new CommentAdapter(this, comments);

        binding.rvCommentList.setAdapter(commentAdapter);
        binding.rvCommentList.setHasFixedSize(true);
        binding.rvCommentList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvCommentList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public CommentViewModel getVM() {
        return commentViewModel;
    }

    private void showLoginDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.account_title))
                .setMessage(getString(R.string.account_message_login))
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton("Yes", ((dialog, which) -> {
                    startActivity(new Intent(CommentActivity.this, LoginActivity.class));
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.dismiss()))
                .setCancelable(false)
                .create().show();
    }

    public CommentAdapter getCommentAdapter(){
        return commentAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:
                if (user == null){
                    showLoginDialog();
                }else {
                    String content = binding.edtContent.getText().toString();

                    if (content.equals("")){
                        Toast.makeText(this, "Nội dung không được phép rỗng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Comment com = new Comment(0, content, 2, course.getId(), user.getAccountId(), null, null);
                    ToeicAPI.postComment(this, com, user.getAccountId(), course.getId());
                    binding.edtContent.setText("");
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}