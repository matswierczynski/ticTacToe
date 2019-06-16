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
        return board;
    }

    public void setBoard(Optional<Player>[][] board) {
        this.board = board;
    }
}
