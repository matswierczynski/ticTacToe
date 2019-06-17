package com.monographic.subject.ticTacToe.model.dto;

import org.springframework.stereotype.Component;

@Component
public class BoardDTO {

    private String[][] board;

    private String winner;

    public String[][] getBoard() {
        if (this.board != null) {
            String[][] copiedBoard = this.board.clone();
            for (int i = 0; i < copiedBoard.length; i++) {
                copiedBoard[i] = copiedBoard[i].clone();
            }
            return copiedBoard;
        } else {
            return null;
        }
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setBoard(String[][] board) {
        if (board != null) {
            this.board = board.clone();
            for (int i = 0; i < this.board.length; i++) {
                this.board[i] = this.board[i].clone();
            }
        } else {
            this.board = null;
        }
    }
}
