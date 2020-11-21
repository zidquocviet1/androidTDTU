package com.example.finalproject.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.Comment;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<List<Comment>> comments;

    public CommentViewModel(){
        comments = new MutableLiveData<>();
    }

    public MutableLiveData<List<Comment>> getComments() {
        return comments;
    }
}
