package com.example.oneweekenglish.model;

import java.util.HashMap;
import java.util.Map;

public class LessonProgress {
    private String id;
    private Lesson lesson;
    private double percent;
    private Map<String, Boolean> practiceCompletion; // Lưu trạng thái hoàn thành của các bài tập

    public LessonProgress() {
        this.practiceCompletion = new HashMap<>();
    }

    public LessonProgress(String id, Lesson lesson, double percent) {
        this.id = id;
        this.lesson = lesson;
        this.percent = percent;
        this.practiceCompletion = new HashMap<>();
    }

    public LessonProgress(Lesson lesson, double percent) {
        this.lesson = lesson;
        this.percent = percent;
        this.practiceCompletion = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Phần trăm phải nằm trong khoảng 0 đến 100");
        }
        this.percent = percent;
    }

    public Map<String, Boolean> getPracticeCompletion() {
        return practiceCompletion;
    }

    public void setPracticeCompletion(Map<String, Boolean> practiceCompletion) {
        this.practiceCompletion = practiceCompletion;
    }

    public void markPracticeCompleted(EPracticeType practiceType) {
        practiceCompletion.put(practiceType.name(), true);
    }

    public boolean isPracticeCompleted(EPracticeType practiceType) {
        return practiceCompletion.getOrDefault(practiceType.name(), false);
    }
}