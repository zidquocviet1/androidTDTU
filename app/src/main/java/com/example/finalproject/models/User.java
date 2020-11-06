package com.example.finalproject.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String type;
    @NonNull
    private boolean isLogin;

    public User(String id, String username, String password, String type, boolean isLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.isLogin = isLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
