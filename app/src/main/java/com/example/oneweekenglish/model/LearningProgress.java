package com.example.oneweekenglish.model;

import java.util.List;

public class LearningProgress {
    private String id;
    private List<LessionProgress> lessionProgress;
    //constructor

    public LearningProgress(String id, List<LessionProgress> lessionProgress) {
        this.id = id;
        this.lessionProgress = lessionProgress;
    }
    public LearningProgress(List<LessionProgress> lessionProgress) {
        this.lessionProgress = lessionProgress;
    }
    //getters

    public String getId() {
        return id;
    }

    public List<LessionProgress> getLessionProgress() {
        return lessionProgress;
    }

    //setters


    public void setId(String id) {
        this.id = id;
    }

    public void setLessionProgress(List<LessionProgress> lessionProgress) {
        this.lessionProgress = lessionProgress;
    }
}
