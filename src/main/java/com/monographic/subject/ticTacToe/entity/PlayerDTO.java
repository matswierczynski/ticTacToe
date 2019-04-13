package com.monographic.subject.ticTacToe.entity;

public class PlayerDTO {
    private String playerName;
    private int wonGames;
    private int lostGames;
    private int drawnGames;

    public PlayerDTO() {
    }

    public PlayerDTO(String playerName, int wonGames, int lostGames, int drawnGames) {
        this.playerName = playerName;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.drawnGames = drawnGames;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    public int getDrawnGames() {
        return drawnGames;
    }

    public void setDrawnGames(int drawnGames) {
        this.drawnGames = drawnGames;
    }
}
