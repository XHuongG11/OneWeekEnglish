package com.example.oneweekenglish.model;

import java.util.List;

public class Grammar {
    private String id;
    private EPracticeType type;
    private List<Question> questions;
    //constructor

    public Grammar() {
    }

    public Grammar(String id, List<Question> questions) {
        this.id = id;
        this.type = EPracticeType.GRAMMAR;
        this.questions = questions;
    }

    public Grammar(List<Question> questions) {
        this.type = EPracticeType.GRAMMAR;
        this.questions = questions;
    }

    //getters

    public String getId() {
        return id;
    }

    public EPracticeType getType() {
        return type;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setType(EPracticeType type) {
        this.type = type;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
