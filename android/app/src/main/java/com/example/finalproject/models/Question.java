package com.example.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question")
public class Question implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String description;
    private String question;
    private String questionA;
    private String questionB;
    private String questionC;
    private String questionD;
    private String answer;
    private long courseID;
    private long categoryID;
    private int part;
    private String audioFile;

    public Question(long id, String description, String question, String questionA, String questionB,
                    String questionC, String questionD, String answer, long courseID,
                    long categoryID, int part, String audioFile) {
        this.id = id;
        this.description = description;
        this.question = question;
        this.questionA = questionA;
        this.questionB = questionB;
        this.questionC = questionC;
        this.questionD = questionD;
        this.answer = answer;
        this.courseID = courseID;
        this.categoryID = categoryID;
        this.part = part;
        this.audioFile = audioFile;
    }

    protected Question(Parcel in) {
        id = in.readLong();
        description = in.readString();
        question = in.readString();
        questionA = in.readString();
        questionB = in.readString();
        questionC = in.readString();
        questionD = in.readString();
        answer = in.readString();
        courseID = in.readLong();
        categoryID = in.readLong();
        part = in.readInt();
        audioFile = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionA() {
        return questionA;
    }

    public void setQuestionA(String questionA) {
        this.questionA = questionA;
    }

    public String getQuestionB() {
        return questionB;
    }

    public void setQuestionB(String questionB) {
        this.questionB = questionB;
    }

    public String getQuestionC() {
        return questionC;
    }

    public void setQuestionC(String questionC) {
        this.questionC = questionC;
    }

    public String getQuestionD() {
        return questionD;
    }

    public void setQuestionD(String questionD) {
        this.questionD = questionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    static final Question[] READING_QUESTIONS = {
            new Question(0, "","The assets of Marble Faun Publishing Company ___ last quarter when one of their main local distributors went out of business."
            , "suffer", "suffers", "suffering", "suffered", "suffered", 1, 5, 5, ""),
            new Question(0, "","Indie film director Luke Steele will be in London for the premiere of ___ new movie."
            , "him", "his", "himself", "he", "his", 1, 5, 5, ""),
            new Question(0,"", "Laboratory employees are expected to wear a name tag and carry identification at ___ times."
            , "full", "complete", "all", "total", "all", 1, 2, 5, ""),
            new Question(0,"", "The latest training ___ contains tips on teaching a second language to international students:"
            , "method", "guide", "staff", "role", "guide", 1, 2, 5, ""),
            new Question(0, "","Once you have your resume with references and ___ , please submit it to the Human Resources Department on the 3rd floor."
            , "qualified", "qualifications", "qualify", "qualifying", "qualifications", 1, 5, 5, "")
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(description);
        dest.writeString(question);
        dest.writeString(questionA);
        dest.writeString(questionB);
        dest.writeString(questionC);
        dest.writeString(questionD);
        dest.writeString(answer);
        dest.writeLong(courseID);
        dest.writeLong(categoryID);
        dest.writeInt(part);
        dest.writeString(audioFile);
    }
}
