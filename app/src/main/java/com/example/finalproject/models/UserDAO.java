package com.example.finalproject.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void addUser(User user);

    @Query("select * from user")
    List<User> getUser();

    @Query("select * from user where id = :id")
    User getUserById(String id);

    @Update
    void updateUser(User user);
}
