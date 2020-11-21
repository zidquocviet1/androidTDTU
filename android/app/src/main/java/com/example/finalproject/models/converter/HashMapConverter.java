package com.example.finalproject.models.converter;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapConverter {
    @TypeConverter
    public static String mapToString(Map<Integer, String> value){
        return value == null ? "" : new Gson().toJson(value);
    }

    @TypeConverter
    public static Map<Integer, String> stringToMap(String value){
        Type listQuestionType = new TypeToken<Map<Integer, String>>(){ }.getType();
        return new Gson().fromJson(value, listQuestionType);
    }
}
