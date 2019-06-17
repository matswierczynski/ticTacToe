package com.monographic.subject.ticTacToe.service.impl;

import com.monographic.subject.ticTacToe.model.dto.BoardDTO;
import com.monographic.subject.ticTacToe.model.entity.Board;
import com.monographic.subject.ticTacToe.model.entity.Player;
import com.monographic.subject.ticTacToe.repository.BoardRepo;
import com.monographic.subject.ticTacToe.repository.FieldRepo;
import com.monographic.subject.ticTacToe.repository.PlayerEntityRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Spy
    @Autowired
    BoardDTO boardDTO;

    @Spy
    Board board;

    @Mock
    BoardRepo boardRepo;

    @Mock
    FieldRepo fieldRepo;

    @Mock
    PlayerEntityRepo playerEntityRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnPlayer() {
        //given
        Player winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[1][3] = Optional.of(Player.PLAYER1);
        currentState[1][4] = Optional.of(Player.PLAYER1);
        currentState[1][5] = Optional.of(Player.PLAYER1);
        currentState[1][6] = Optional.of(Player.PLAYER1);
        currentState[1][7] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkState(board);

        // then
        assertThat(winner).isNotNull();
        assertThat(winner).isEqualTo(Player.PLAYER1);

    }

    @Test
    public void shouldNotReturnPlayer() {
        //given
        Player winner;
        Board board = gameService.startNewGame(10);

        // when
        winner = gameService.checkState(board);

        // then
        assertThat(winner).isNull();
    }


    @Test
    public void shouldFindWinnerInColumn() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[1][3] = Optional.of(Player.PLAYER1);
        currentState[1][4] = Optional.of(Player.PLAYER1);
        currentState[1][5] = Optional.of(Player.PLAYER1);
        currentState[1][6] = Optional.of(Player.PLAYER1);
        currentState[1][7] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkStraightSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER1);
    }

    @Test
    public void shouldNotFindWinnerInColumnWhenMarksAreNotInOrder() {
        // given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[2][0] = Optional.of(Player.PLAYER1);
        currentState[2][1] = Optional.of(Player.PLAYER2);
        currentState[2][2] = Optional.of(Player.PLAYER1);
        currentState[2][3] = Optional.of(Player.PLAYER1);
        currentState[2][4] = Optional.of(Player.PLAYER1);
        currentState[2][5] = Optional.of(Player.PLAYER1);
        currentState[2][7] = Optional.of(Player.PLAYER1);
        currentState[2][9] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        //when
        winner = gameService.checkStraightSequences(board);

        // then
        assertThat(winner).isEmpty();
    }

    @Test
    public void shouldFindWinnerInRow() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[5][5] = Optional.of(Player.PLAYER1);
        currentState[6][5] = Optional.of(Player.PLAYER1);
        currentState[7][5] = Optional.of(Player.PLAYER1);
        currentState[8][5] = Optional.of(Player.PLAYER1);
        currentState[9][5] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        //when
        winner = gameService.checkStraightSequences(board);

        //then
        assertThat(winner).isNotEmpty();
        assertThat(winner.get()).isEqualTo(Player.PLAYER1);
    }

    @Test
    public void shouldNotFindWinnerInRowWhenMarksNotInOrder() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[5][1] = Optional.of(Player.PLAYER1);
        currentState[5][2] = Optional.of(Player.PLAYER1);
        currentState[5][3] = Optional.of(Player.PLAYER1);
        currentState[5][4] = Optional.of(Player.PLAYER1);
        currentState[6][6] = Optional.of(Player.PLAYER2);
        currentState[7][7] = Optional.of(Player.PLAYER1);
        currentState[8][8] = Optional.of(Player.PLAYER1);
        currentState[9][9] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkStraightSequences(board);

        // then
        assertThat(winner).isEmpty();
    }

    @Test
    public void shouldFindOnlyOneWinner() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[1][1] = Optional.of(Player.PLAYER2);
        currentState[1][2] = Optional.of(Player.PLAYER2);
        currentState[1][3] = Optional.of(Player.PLAYER2);
        currentState[1][4] = Optional.of(Player.PLAYER2);
        currentState[1][5] = Optional.of(Player.PLAYER2);
        currentState[5][1] = Optional.of(Player.PLAYER1);
        currentState[5][2] = Optional.of(Player.PLAYER1);
        currentState[5][3] = Optional.of(Player.PLAYER1);
        currentState[5][4] = Optional.of(Player.PLAYER1);
        currentState[5][5] = Optional.of(Player.PLAYER1);
        currentState[5][6] = Optional.of(Player.PLAYER1);
        currentState[5][7] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkStraightSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);

    }

    @Test
    public void shouldFindWinnerInWestSouthDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[0][4] = Optional.of(Player.PLAYER2);
        currentState[1][5] = Optional.of(Player.PLAYER2);
        currentState[2][6] = Optional.of(Player.PLAYER2);
        currentState[3][7] = Optional.of(Player.PLAYER2);
        currentState[4][8] = Optional.of(Player.PLAYER2);
        currentState[5][9] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);
    }

    @Test
    public void shouldNotFindWinnerInWestSouthDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[0][4] = Optional.of(Player.PLAYER2);
        currentState[1][5] = Optional.of(Player.PLAYER1);
        currentState[2][6] = Optional.of(Player.PLAYER2);
        currentState[3][7] = Optional.of(Player.PLAYER2);
        currentState[4][8] = Optional.of(Player.PLAYER2);
        currentState[5][9] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isEmpty();
    }

    @Test
    public void shouldFindWinnerInNorthEastDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[5][0] = Optional.of(Player.PLAYER2);
        currentState[6][1] = Optional.of(Player.PLAYER2);
        currentState[7][2] = Optional.of(Player.PLAYER2);
        currentState[8][3] = Optional.of(Player.PLAYER2);
        currentState[9][4] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);
    }

    @Test
    public void shouldNotFindWinnerInNorthEastDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[2][3] = Optional.of(Player.PLAYER2);
        currentState[3][4] = Optional.of(Player.PLAYER2);
        currentState[4][5] = Optional.of(Player.PLAYER1);
        currentState[5][6] = Optional.of(Player.PLAYER2);
        currentState[6][7] = Optional.of(Player.PLAYER2);
        currentState[7][8] = Optional.of(Player.PLAYER2);
        currentState[8][9] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isEmpty();
    }


    @Test
    public void shouldFindWinnerInWestNorthDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[1][7] = Optional.of(Player.PLAYER2);
        currentState[2][6] = Optional.of(Player.PLAYER2);
        currentState[3][5] = Optional.of(Player.PLAYER2);
        currentState[4][4] = Optional.of(Player.PLAYER2);
        currentState[5][3] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);
    }

    @Test
    public void shouldNotFindWinnerInWestNorthDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[1][7] = Optional.of(Player.PLAYER2);
        currentState[2][6] = Optional.of(Player.PLAYER2);
        currentState[3][5] = Optional.of(Player.PLAYER2);
        currentState[4][4] = Optional.of(Player.PLAYER2);
        currentState[5][3] = Optional.of(Player.PLAYER1);
        currentState[6][2] = Optional.of(Player.PLAYER2);
        currentState[7][1] = Optional.of(Player.PLAYER2);
        currentState[8][0] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isEmpty();
    }

    @Test
    public void shouldFindWinnerInSouthEastDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[4][8] = Optional.of(Player.PLAYER2);
        currentState[5][7] = Optional.of(Player.PLAYER2);
        currentState[6][6] = Optional.of(Player.PLAYER2);
        currentState[7][5] = Optional.of(Player.PLAYER2);
        currentState[8][4] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);
    }

    @Test
    public void shouldNotFindWinnerInSouthEastDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[4][7] = Optional.of(Player.PLAYER2);
        currentState[5][6] = Optional.of(Player.PLAYER2);
        currentState[6][5] = Optional.of(Player.PLAYER1);
        currentState[7][4] = Optional.of(Player.PLAYER2);
        currentState[8][3] = Optional.of(Player.PLAYER2);
        currentState[9][2] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isEmpty();
    }

    @Test
    public void shouldFindWinnerOnFirstMainDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[2][2] = Optional.of(Player.PLAYER2);
        currentState[3][3] = Optional.of(Player.PLAYER2);
        currentState[4][4] = Optional.of(Player.PLAYER2);
        currentState[5][5] = Optional.of(Player.PLAYER2);
        currentState[6][6] = Optional.of(Player.PLAYER2);
        currentState[7][7] = Optional.of(Player.PLAYER2);
        currentState[8][8] = Optional.of(Player.PLAYER2);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER2);
    }

    @Test
    public void shouldFindWinnerOnSecondMainDiagonal() {
        //given
        Optional<Player> winner;
        Board board = gameService.startNewGame(10);
        Optional<Player>[][] currentState = board.getBoard();
        currentState[0][9] = Optional.of(Player.PLAYER1);
        currentState[1][8] = Optional.of(Player.PLAYER1);
        currentState[2][7] = Optional.of(Player.PLAYER1);
        currentState[3][6] = Optional.of(Player.PLAYER1);
        currentState[4][5] = Optional.of(Player.PLAYER1);
        currentState[5][4] = Optional.of(Player.PLAYER1);
        currentState[6][3] = Optional.of(Player.PLAYER1);
        board.setBoard(currentState);

        // when
        winner = gameService.checkDiagonalsSequences(board);

        // then
        assertThat(winner).isPresent();
        assertThat(winner.get()).isEqualTo(Player.PLAYER1);
    }
}