package com.example.oneweekenglish.model;

public class Message {
    private String content;
    private boolean isBot;

    public Message(String content, boolean isBot) {
        this.content = content;
        this.isBot = isBot;
    }

    public String getContent() {
        return content;
    }

    public boolean isBot() {
        return isBot;
    }
}