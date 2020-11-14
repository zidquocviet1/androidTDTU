package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.finalproject.models.Course;
import com.example.finalproject.models.CourseAndQuestion;

import java.util.List;

@Dao
public interface CourseDAO {
    @Query("select count(*) from course")
    int count();

    @Insert
    void addCourse(Course course);

    @Query("select * from course")
    List<Course> getAllCourse();

    @Query("select * from course where id = :id")
    Course getCourseById(int id);

    @Update
    void updateCourse(Course course);

    @Query("delete from course")
    void deleteAll();

    @Transaction
    @Query("select * from course where id = :id")
    List<CourseAndQuestion> getCourseAndQuestion(long id);
}
