package com.monographic.subject.ticTacToe.entity;

import javax.persistence.*;

@Entity
@Table(name = "Field")
public class FieldBE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int xCoord;
    private int yCoord;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_player")
    private PlayerBE player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_fk")
    private BoardBE board;

    public FieldBE() {
    }

    public FieldBE(int xCoord, int yCoord, PlayerBE player, BoardBE board) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.player = player;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public PlayerBE getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBE player) {
        this.player = player;
    }

    public BoardBE getBoard() {
        return board;
    }

    public void setBoard(BoardBE board) {
        this.board = board;
    }
}
