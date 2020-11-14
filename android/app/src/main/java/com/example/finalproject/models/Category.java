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
            new Category(0, "Statement", "Chon ra loi tuyen bo dung cua cau hoi"),
            new Category(0,"Choice", "Chon ra cau dung"),
            new Category(0, "Wh-question", "Cau hoi WH"),
            new Category(0, "Yes/no/tag - question", "Cau hoi yes/no"),
            new Category(0, "Word form", "Cau hoi ve tu loai")
    };
}
