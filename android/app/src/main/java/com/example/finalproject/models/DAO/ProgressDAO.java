package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.models.Progress;

@Dao
public interface ProgressDAO {

    @Query("select * from progress where courseID = :id")
    Progress getByCourseId(long id);

    @Insert
    void add(Progress... progresses);

    @Update
    void update(Progress... progresses);
}
