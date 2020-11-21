package com.example.finalproject.models;

public class Comment {
    private int id;
    private String description;
    private int rating;
    private long courseId;
    private String userId;
    private Course course;
    private User user;

    public Comment(int id, String description, int rating, long courseId, String userId, Course course, User user) {
        this.id = id;
        this.description = description;
        this.rating = rating;
        this.courseId = courseId;
        this.userId = userId;
        this.course = course;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
