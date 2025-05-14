package com.example.oneweekenglish.model;

import java.util.List;

public class MatchWord {
    private String id;
    private EPracticeType type;
    private List<Word> words;
    //constructor

    public MatchWord(String id,List<Word> words) {
        this.id = id;
        this.type = EPracticeType.MATCH_WORD;
        this.words = words;
    }

    public MatchWord(List<Word> words) {
        this.type = type;
        this.type = EPracticeType.MATCH_WORD;
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
