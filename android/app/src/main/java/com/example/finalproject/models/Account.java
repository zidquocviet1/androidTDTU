package com.example.finalproject.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.finalproject.models.DAO.AccountDAO;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity(tableName = "account")
public class Account implements Parcelable {
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
    private String fullName;

    public Account(String id, String username, String password, String type, boolean isLogin, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.isLogin = isLogin;
        this.fullName = fullName;
    }

    protected Account(Parcel in) {
        id = in.readString();
        username = in.readString();
        password = in.readString();
        type = in.readString();
        isLogin = in.readByte() != 0;
        fullName = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static void saveAccountInfo(FirebaseUser user, Context context) {
        Account newAccount = new Account(user.getUid(), user.getEmail(), "", "facebook", true, user.getDisplayName());
        AccountDAO dao = MyDatabase.getInstance(context).accDAO();

        if (dao.getAllAccount().size() != 0)
            dao.deleteAll();
        dao.addAccount(newAccount);
    }

    public static void saveAccountInfo(Account account, Context context) {
        Account newAccount = new Account(account.getId(), account.getUsername(), account.getPassword(), account.getType(), true, account.getFullName());
        AccountDAO dao = MyDatabase.getInstance(context).accDAO();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(type);
        dest.writeByte((byte) (isLogin ? 1 : 0));
        dest.writeString(fullName);
    }
}
