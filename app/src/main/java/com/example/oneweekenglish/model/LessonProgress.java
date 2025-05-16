package com.example.oneweekenglish.model;

public class LessonProgress {
    private String id;
    private Lesson lesson;
    private double percent;
    //constructor

    public LessonProgress() {
    }

    public LessonProgress(String id, Lesson lesson, double percent) {
        this.id = id;
        this.lesson = lesson;
        this.percent = percent;
    }
    public LessonProgress(Lesson lesson, double percent) {
        this.lesson = lesson;
        this.percent = percent;
    }

    //getters

    public String getId() {
        return id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public double getPercent() {
        return percent;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
