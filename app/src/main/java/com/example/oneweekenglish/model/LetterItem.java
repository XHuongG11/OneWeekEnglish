package com.example.oneweekenglish.model;
public class LetterItem {
    private String letter;
    private boolean isSelected;

    public LetterItem(String letter) {
        this.letter = letter;
        this.isSelected = false;
    }

    public String getLetter() {
        return letter;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}