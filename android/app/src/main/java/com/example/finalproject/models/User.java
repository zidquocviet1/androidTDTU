package com.example.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable {
    private int id;
    private String name;
    private boolean gender;
    private Date birthday;
    private String address;
    private String email;
    private int score;
    private String accountId;
    private int avatar;

    public User(int id, String name, boolean gender, Date birthday,
                String address, String email, int score, String accountId, int avatar) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.score = score;
        this.accountId = accountId;
        this.avatar = avatar;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        gender = in.readByte() != 0;
        address = in.readString();
        email = in.readString();
        score = in.readInt();
        accountId = in.readString();
        avatar = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(address);
        dest.writeString(email);
        dest.writeInt(score);
        dest.writeString(accountId);
        dest.writeInt(avatar);
    }
}
