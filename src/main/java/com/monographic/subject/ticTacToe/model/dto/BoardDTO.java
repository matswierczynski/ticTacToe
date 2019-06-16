package com.monographic.subject.ticTacToe.model.dto;

import org.springframework.stereotype.Component;

@Component
public class BoardDTO {

    private String[][] board;
    private String winner;

    public String[][] getBoard() {
        return board;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}
