package com.example.finalproject.models.converter;

import androidx.room.TypeConverter;

import com.example.finalproject.models.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListConverter {
    @TypeConverter
    public static ArrayList<Comment> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Comment>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Comment> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
