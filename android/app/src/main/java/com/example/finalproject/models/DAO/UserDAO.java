package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.models.User;

@Dao
public interface UserDAO {
    @Query("select * from user limit 1")
    User getFirstUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User... users);

    @Update
    void updateUser(User... users);
}
