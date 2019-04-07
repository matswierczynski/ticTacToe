package com.monographic.subject.ticTacToe.entity;

import org.springframework.stereotype.Component;

@Component
public class BoardDTO {

    private String[][] board;
    private String winner;

    public String[][] getBoard() {
        return board;
    }

    public String getWinnner() {
        return winner;
    }

    public void setWinnner(String winnner) {
        this.winner = winnner;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}
