package com.example.oneweekenglish.model;

public class CardItem {
    public String type; // "image" hoặc "text"
    public String value; // dùng để so sánh
    public String imageUrl; // link ảnh, nếu là image

    public CardItem(String type, String value, String imageUrl) {
        this.type = type;
        this.value = value;
        this.imageUrl = imageUrl;
    }
}

