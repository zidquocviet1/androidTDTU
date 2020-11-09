package com.example.finalproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToeicViewModel extends ViewModel {
    private MutableLiveData<Boolean> serverState;
    private MutableLiveData<Boolean> networkState;

    public ToeicViewModel(){
        serverState = new MutableLiveData<>();
        networkState = new MutableLiveData<>();

        serverState.setValue(true);
        networkState.setValue(true);
    }

    public MutableLiveData<Boolean> getServerState(){
        return serverState;
    }

    public MutableLiveData<Boolean> getNetworkState() {
        return networkState;
    }
}
