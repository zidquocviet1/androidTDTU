package com.example.finalproject.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @ColumnInfo(defaultValue = "5")
    private float rating;

    public Course(long id, @NonNull String name, @NonNull String description, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    static final Course[] courses = {
            new Course(0, "Toeic 750", "Mục tiêu khóa này là 750 điểm", 4),
            new Course(0, "Toeic 550", "Mục tiêu khóa này là 550 điểm", 3),
            new Course(0, "Toeic 850", "Mục tiêu khóa này là 850 điểm", 2),
            new Course(0, "Toeic 990", "Mục tiêu khóa này là 990 điểm", 1),
            new Course(0, "Toeic 650", "Mục tiêu khóa này là 650 điểm", 3)
    };
}
