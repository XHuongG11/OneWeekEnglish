package com.example.oneweekenglish.model;

import java.util.List;

public class Word {
    private String id;
    private String content;
    private List<String> meanings;
    private List<String> definitions;
    private String ipa;
    private String imageUrl;
    private String type;
    //constructor

    public Word(String id, String content, List<String> meanings, List<String> definitions, String ipa, String imageUrl,String type) {
        this.id = id;
        this.content = content;
        this.meanings = meanings;
        this.definitions = definitions;
        this.ipa = ipa;
        this.imageUrl = imageUrl;
        this.type = type;
    }
    public Word(String content, List<String> meanings, List<String> definitions, String ipa, String imageUrl,String type) {
        this.content = content;
        this.meanings = meanings;
        this.definitions = definitions;
        this.ipa = ipa;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    //getters

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public String getIpa() {
        return ipa;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType(){
        return type;
    }
    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setType(String type){
        this.type = type;
    }
}
