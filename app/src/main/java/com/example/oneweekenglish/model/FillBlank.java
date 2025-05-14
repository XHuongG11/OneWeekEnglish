package com.example.oneweekenglish.model;

import java.util.List;

public class FillBlank {
    private String id;
    private EPracticeType type;
    private List<Word> words;
    //constructor

    public FillBlank(String id,List<Word> words) {
        this.id = id;
        this.type = EPracticeType.FILL_BLANK;
        this.words = words;
    }

    public FillBlank(List<Word> words) {
        this.type = type;
        this.type = EPracticeType.FILL_BLANK;
        this.words = words;
    }

    //getters

    public String getId() {
        return id;
    }

    public EPracticeType getType() {
        return type;
    }

    public List<Word> getWords() {
        return words;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setType(EPracticeType type) {
        this.type = type;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
