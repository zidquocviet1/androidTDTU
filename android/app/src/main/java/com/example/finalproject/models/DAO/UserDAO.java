package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.finalproject.models.User;

@Dao
public interface UserDAO {

    @Insert
    void addUser(User... users);

    @Update
    void updateUser(User... users);
}
