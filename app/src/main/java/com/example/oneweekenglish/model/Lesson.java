package com.example.oneweekenglish.model;

public class Lesson {
    private String id;
    private String name;
    private FillBlank fillBlankPractice;
    private Grammar grammarPractice;
    private LearnWord learnWordPractice;
    private MatchWord matchWordPractice;

    //constructor

    public Lesson() {
    }

    public Lesson(String id, String name, FillBlank fillBlankPractice, Grammar grammarPractice, LearnWord learnWordPractice, MatchWord matchWordPractice) {
        this.id = id;
        this.name = name;
        this.fillBlankPractice = fillBlankPractice;
        this.grammarPractice = grammarPractice;
        this.learnWordPractice = learnWordPractice;
        this.matchWordPractice = matchWordPractice;
    }
    public Lesson(String name, FillBlank fillBlankPractice, Grammar grammarPractice, LearnWord learnWordPractice, MatchWord matchWordPractice) {
        this.name = name;
        this.fillBlankPractice = fillBlankPractice;
        this.grammarPractice = grammarPractice;
        this.learnWordPractice = learnWordPractice;
        this.matchWordPractice = matchWordPractice;
    }
    //getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FillBlank getFillBlankPractice() {
        return fillBlankPractice;
    }

    public Grammar getGrammarPractice() {
        return grammarPractice;
    }

    public LearnWord getLearnWordPractice() {
        return learnWordPractice;
    }

    public MatchWord getMatchWordPractice() {
        return matchWordPractice;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFillBlankPractice(FillBlank fillBlankPractice) {
        this.fillBlankPractice = fillBlankPractice;
    }

    public void setGrammarPractice(Grammar grammarPractice) {
        this.grammarPractice = grammarPractice;
    }

    public void setLearnWordPractice(LearnWord learnWordPractice) {
        this.learnWordPractice = learnWordPractice;
    }

    public void setMatchWordPractice(MatchWord matchWordPractice) {
        this.matchWordPractice = matchWordPractice;
    }
}
