package com.example.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word")
public class Word implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private String pronounce;
    private boolean wasLearnt;

    public Word(long id, @NonNull String name, @NonNull String description, String pronounce) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pronounce = pronounce;
        this.wasLearnt = false;
    }

    protected Word(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        pronounce = in.readString();
        wasLearnt = in.readByte() != 0;
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

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

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public boolean isWasLearnt() {
        return wasLearnt;
    }

    public void setWasLearnt(boolean wasLearnt) {
        this.wasLearnt = wasLearnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(pronounce);
        dest.writeByte((byte) (wasLearnt ? 1 : 0));
    }
}
