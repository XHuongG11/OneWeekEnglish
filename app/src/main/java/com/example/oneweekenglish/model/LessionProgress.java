package com.example.oneweekenglish.model;

public class LessionProgress {
    private String id;
    private Lession lession;
    private double percent;
    //constructor

    public LessionProgress(String id, Lession lession, double percent) {
        this.id = id;
        this.lession = lession;
        this.percent = percent;
    }
    public LessionProgress(Lession lession, double percent) {
        this.lession = lession;
        this.percent = percent;
    }

    //getters

    public String getId() {
        return id;
    }

    public Lession getLession() {
        return lession;
    }

    public double getPercent() {
        return percent;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setLession(Lession lession) {
        this.lession = lession;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
