package com.example.oneweekenglish.model;

import java.util.List;
import java.util.Map;

public class Room {
    private Map<String, Player> players;
    private List<FlyingWord> words;
    private boolean gameStarted;

    public Room() {

    }

    public Room(Map<String, Player> players, List<FlyingWord> words, boolean gameStarted) {
        this.players = players;
        this.words = words;
        this.gameStarted = gameStarted;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public List<FlyingWord> getWords() {
        return words;
    }

    public void setWords(List<FlyingWord> words) {
        this.words = words;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean containsPlayer(String uid) {
        return players != null && players.containsKey(uid);
    }
}
