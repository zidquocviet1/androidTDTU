package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.models.Record;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("select * from record where courseId = :courseId and accountId = :accountId")
    Record getByCourseIdAndAccountId(long courseId, String accountId);

    @Insert
    void addRecord(Record... records);

    @Update
    void updateRecord(Record... records);

    @Query("select * from record")
    List<Record> getAll();
}
