package com.example.finalproject.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long courseId;
    private String accountId;
    private int score;
    @Ignore
    private String name;

    public Record(int id, long courseId, String accountId, int score) {
        this.id = id;
        this.courseId = courseId;
        this.accountId = accountId;
        this.score = score;
    }

    @Ignore
    public Record(int id, long courseId, String accountId, int score, String name) {
        this.id = id;
        this.courseId = courseId;
        this.accountId = accountId;
        this.score = score;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
