package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.finalproject.models.Question;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Question... questions);

    @Query("select count(*) from question")
    int count();

    @Insert
    long insert(Question question);

    @Query("select * from question")
    List<Question> getAllQuestions();
}
