package com.example.finalproject.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseAndQuestion {
    @Embedded
    public Course course;
    @Relation(
            parentColumn = "id",
            entityColumn = "courseID"
    )
    public List<Question> questions;
}
