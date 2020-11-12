package com.example.finalproject.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalproject.models.DAO.AccountDAO;
import com.example.finalproject.models.DAO.CategoryDAO;
import com.example.finalproject.models.DAO.CourseDAO;
import com.example.finalproject.models.DAO.QuestionDAO;

@Database(entities = {Account.class, Question.class, Course.class, Category.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context){
        if (instance == null) {
            instance = create(context);
            instance.populateInitialData();
        }
        return instance;
    }
    public MyDatabase(){

    }
    private static MyDatabase create(Context context){
        return Room.databaseBuilder(context, MyDatabase.class, "toeictesting")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
    private void populateInitialData(){
        if (courseDAO().count() == 0){
            runInTransaction(() -> {
                for (Course c : Course.courses){
                    courseDAO().addCourse(c);
                }
            });
        }
        if (categoryDAO().count() == 0){
            runInTransaction(() -> {
                for (Category c : Category.categories){
                    categoryDAO().insert(c);
                }
            });
        }
        if (questionDAO().count() == 0){
            runInTransaction(() -> {
                for (Question q : Question.questions){
                    questionDAO().insert(q);
                }
            });
        }
    }
    public abstract AccountDAO userDAO();
    public abstract CourseDAO courseDAO();
    public abstract QuestionDAO questionDAO();
    public abstract CategoryDAO categoryDAO();
}
