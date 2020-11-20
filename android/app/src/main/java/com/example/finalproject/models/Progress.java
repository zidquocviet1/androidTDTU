package com.example.finalproject.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Progress {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long courseID;
    private int remainTime;

    @TypeConverters(HashMapConverter.class)
    private Map<Integer, String> questions;
    private boolean isCounting;

    public Progress(long id, long courseID, int remainTime, Map<Integer, String> questions, boolean isCounting) {
        this.id = id;
        this.courseID = courseID;
        this.remainTime = remainTime;
        this.questions = questions;
        this.isCounting = isCounting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public Map<Integer, String> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Integer, String> questions) {
        this.questions = questions;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }
}
