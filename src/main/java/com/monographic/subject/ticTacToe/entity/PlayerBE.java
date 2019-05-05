package com.monographic.subject.ticTacToe.entity;

import javax.persistence.*;

@Entity
@Table(name = "Player")
public class PlayerBE {

    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private Player player;

    private int lostGames;

    private int wonGames;

    private int drawnGames;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_personal_data")
    private PersonalDataBE personalData;

    public PlayerBE() {
    }

    public PlayerBE(long id, Player player) {
        this.id = id;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getDrawnGames() {
        return drawnGames;
    }

    public void setDrawnGames(int drawnGames) {
        this.drawnGames = drawnGames;
    }

    public PersonalDataBE getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDataBE personalData) {
        this.personalData = personalData;
    }
}
