package com.monographic.subject.ticTacToe.model.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    public void shouldCreateEmptyBoard() {
        //given
        Board board = new Board();

        // when
        board.init(10);

        // then
        assertThat(board.getBoard()).isNotNull();
        assertThat(board.getBoard().length).isEqualTo(10);
    }
}