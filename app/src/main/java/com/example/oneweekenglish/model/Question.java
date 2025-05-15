package com.example.oneweekenglish.model;

import java.util.List;

public class Question {
    private String id;
    private String urlImage;
    private String content;
    //constructor

    public Question() {
    }

    public Question(String id, String urlImage, String content) {
        this.id = id;
        this.urlImage = urlImage;
        this.content = content;
    }

    public Question(String content, String urlImage) {
        this.content = content;
        this.urlImage = urlImage;
    }
    //getters

    public String getId() {
        return id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getContent() {
        return content;
    }
    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
