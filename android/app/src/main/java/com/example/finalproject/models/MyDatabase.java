package com.example.finalproject.models;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalproject.R;
import com.example.finalproject.models.DAO.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

@Database(entities = {Account.class, Question.class, Course.class, Category.class, Word.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
            InputStream in = context.getResources().openRawResource(R.raw.vocab_csv);
            instance.populateInitialData(in);
        }
        return instance;
    }

    public MyDatabase() {

    }

    private static MyDatabase create(Context context) {
        return Room.databaseBuilder(context, MyDatabase.class, "toeictesting")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    private void populateInitialData(InputStream in) {
        if (courseDAO().count() == 0) {
            runInTransaction(() -> {
                for (Course c : Course.courses) {
                    courseDAO().addCourse(c);
                }
            });
        }
        if (categoryDAO().count() == 0) {
            runInTransaction(() -> {
                for (Category c : Category.categories) {
                    categoryDAO().insert(c);
                }
            });
        }
        if (questionDAO().count() == 0) {
            runInTransaction(() -> {
                for (Question q : Question.questions) {
                    questionDAO().insert(q);
                }
            });
        }

        if (wordDAO().count() == 0) {
            runInTransaction(() -> {
                Scanner sc = new Scanner(in);
                int i = 0;
                while (sc.hasNext() && i <= 1000){
                    String data = sc.nextLine();
                    String[] vocab = data.split(",");

                    String name = vocab[0].replace("\"", "");
                    String des = vocab[1].replace("\"", "");
                    String pron = vocab[2].replace("\"", "");

                    Word word = new Word(0, name, des, pron);
                    wordDAO().addWord(word);
                    i++;
                }
            });
        }
    }

    public abstract AccountDAO userDAO();

    public abstract CourseDAO courseDAO();

    public abstract QuestionDAO questionDAO();

    public abstract CategoryDAO categoryDAO();

    public abstract WordDAO wordDAO();
}
