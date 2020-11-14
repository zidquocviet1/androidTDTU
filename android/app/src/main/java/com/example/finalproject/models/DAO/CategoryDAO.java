package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.finalproject.models.Category;
import com.example.finalproject.models.Question;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("select count(*) from category")
    int count();

    @Insert
    long insert(Category category);

    @Query("select * from category")
    List<Category> getAllCategories();
}
