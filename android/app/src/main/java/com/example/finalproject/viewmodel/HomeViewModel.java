package com.example.finalproject.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.Account;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.User;
import com.example.finalproject.models.Word;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Boolean> serverState;
    private MutableLiveData<Boolean> networkState;
    private MutableLiveData<Account> loginAccount;
    private MutableLiveData<User> userInfo;
    private MutableLiveData<List<Course>> courses;
    private MutableLiveData<List<Word>> words;

    public HomeViewModel() {
        serverState = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
        loginAccount = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();
        courses = new MutableLiveData<>();
        words = new MutableLiveData<>();

        serverState.setValue(true);
        networkState.setValue(true);
        loginAccount.setValue(null);
        userInfo.setValue(null);
    }

    public MutableLiveData<Boolean> getServerState() {
        return serverState;
    }

    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<Account> getAccount() {
        return loginAccount;
    }

    public MutableLiveData<User> getUser() {
        return userInfo;
    }

    public MutableLiveData<List<Course>> getCourses() {
        return courses;
    }

    public MutableLiveData<List<Word>> getWords() {
        return words;
    }
}
