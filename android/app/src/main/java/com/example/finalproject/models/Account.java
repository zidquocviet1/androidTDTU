package com.example.finalproject.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.finalproject.models.DAO.AccountDAO;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity(tableName = "account")
public class Account {
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

    public Account(String id, String username, String password, String type, boolean isLogin) {
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

    public static void saveAccountInfo(FirebaseUser user, Context context) {
        Account newAccount = new Account(user.getUid(), user.getEmail(), "", "facebook", true);
        AccountDAO dao = MyDatabase.getInstance(context).userDAO();

        if (dao.getAllAccount().size() != 0)
            dao.deleteAll();
        dao.addAccount(newAccount);
    }

    public static void saveAccountInfo(Account account, Context context) {
        Account newAccount = new Account(account.getId(), account.getUsername(), account.getPassword(), account.getType(), true);
        AccountDAO dao = MyDatabase.getInstance(context).userDAO();

        if (dao.getAllAccount().size() != 0)
            dao.deleteAll();

        dao.addAccount(newAccount);
    }

    public void encryptPassword(){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(this.getPassword().getBytes());
            this.setPassword(byteToHex(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String byteToHex(byte[] input){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            sb.append(Integer.toString((input[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
