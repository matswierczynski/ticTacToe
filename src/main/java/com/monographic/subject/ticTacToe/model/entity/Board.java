package com.monographic.subject.ticTacToe.model.entity;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Board {
    Optional<Player>[][] board;

    public void init(int size) {
        board = new Optional[size][size];
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                board[i][k] = Optional.empty();
            }
        }
    }

    public Optional<Player>[][] getBoard() {
        if (board != null) {
            Optional<Player>[][] copiedBoard = this.board.clone();
            for (int i = 0; i < copiedBoard.length; i++) {
                copiedBoard[i] = copiedBoard[i].clone();
            }
            return copiedBoard;
        } else {
            return null;
        }
    }

    public void setBoard(Optional<Player>[][] board) {
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
