package com.example.finalproject.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;

    public Category(long id, @NonNull String name, @NonNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    static final Category[] categories = {
            new Category(0, "Tag question", "Câu hỏi đuôi"),
            new Category(0,"Vocabulary", "Câu hỏi nghĩa của từ"),
            new Category(0, "Wh-question", "Câu hỏi WH"),
            new Category(0, "Yes/no/tag - question", "Câu hỏi Yes/No"),
            new Category(0, "Word form", "Câu hỏi về từ loại")
    };
}
