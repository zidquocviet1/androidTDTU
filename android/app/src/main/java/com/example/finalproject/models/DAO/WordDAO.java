package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.models.Word;

import java.util.List;

@Dao
public interface WordDAO {
    @Query("select count(*) from word")
    int count();

    @Insert
    long addWord(Word word);

    @Query("select * from word order by random() limit 30")
    List<Word> getTop30();

    @Query("select * from word")
    List<Word> getAll();

    @Update
    void updateWord(Word word);
}
