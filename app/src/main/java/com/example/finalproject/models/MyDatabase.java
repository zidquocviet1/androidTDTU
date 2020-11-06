package com.example.finalproject.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context){
        if (instance == null)
            instance = create(context);
        return instance;
    }
    public MyDatabase(){

    }
    private static MyDatabase create(Context context){
        return Room.databaseBuilder(context, MyDatabase.class, "toeictesting")
                .allowMainThreadQueries()
                .build();
    }
    public abstract UserDAO getUserDAO();
}
