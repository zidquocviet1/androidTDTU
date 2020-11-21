package com.example.finalproject.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalproject.Utils;
import com.example.finalproject.models.DAO.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

@Database(entities = {Account.class, Question.class, Course.class,
        Category.class, Word.class, Progress.class, User.class}, version = 1)

public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
            instance.populateInitialData(context);
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

    private void populateInitialData(Context context) {
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
                try {
                    InputStream is = context.getAssets().open("question.json");
                    String jsonFileString = Utils.getJsonFromAssets(is);

                    Gson gson = new Gson();
                    Type listQuestionType = new TypeToken<List<Question>>() {
                    }.getType();
                    List<Question> questions = gson.fromJson(jsonFileString, listQuestionType);

                    questions.forEach(q -> {
                        q.setAudioFile("course_" + q.getCourseID() + "/" + q.getAudioFile());
                        questionDAO().insertAll(q);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (wordDAO().count() == 0) {
            runInTransaction(() -> {
                try {
                    InputStream is = context.getAssets().open("vocab_json.json");
                    String jsonFileString = Utils.getJsonFromAssets(is);

                    Gson gson = new Gson();
                    Type listWordType = new TypeToken<List<Word>>() {
                    }.getType();
                    List<Word> words = gson.fromJson(jsonFileString, listWordType);

                    words.forEach(w -> wordDAO().addWord(w));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public abstract AccountDAO accDAO();

    public abstract CourseDAO courseDAO();

    public abstract QuestionDAO questionDAO();

    public abstract CategoryDAO categoryDAO();

    public abstract WordDAO wordDAO();

    public abstract ProgressDAO progressDAO();

    public abstract UserDAO userDAO();
}
