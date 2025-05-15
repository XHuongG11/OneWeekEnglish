package com.example.oneweekenglish.model;

import java.util.List;

public class LearningProgress {
    private String id;
    private User user;
    private List<LessonProgress> lessonProgress;
    //constructor

    public LearningProgress() {
    }

    public LearningProgress(List<LessonProgress> lessonProgress, User user, String id) {
        this.lessonProgress = lessonProgress;
        this.user = user;
        this.id = id;
    }

    public LearningProgress(User user, List<LessonProgress> lessonProgress) {
        this.user = user;
        this.lessonProgress = lessonProgress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LessonProgress> getLessonProgress() {
        return lessonProgress;
    }

    public void setLessonProgress(List<LessonProgress> lessonProgress) {
        this.lessonProgress = lessonProgress;
    }
}
