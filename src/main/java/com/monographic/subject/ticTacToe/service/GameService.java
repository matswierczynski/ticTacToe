package com.monographic.subject.ticTacToe.service;

import com.monographic.subject.ticTacToe.entity.Board;
import com.monographic.subject.ticTacToe.entity.BoardDTO;
import com.monographic.subject.ticTacToe.entity.MoveDTO;
import com.monographic.subject.ticTacToe.entity.Player;
import exception.BusyPositionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private int MARKS_TO_WIN = 5;
    private final Board board;
    private final BoardDTO boardDTO;

    @Autowired
    public GameService(Board board, BoardDTO boardDTO) {
        this.board = board;
        this.boardDTO = boardDTO;
    }

    public BoardDTO prepareNewGame(int size) {
        Board newBoard = startNewGame(size);
        Optional<Player>[][] newBoardState = newBoard.getBoard();
        String[][] newTransferBoard = transformOptionalsArrayyToPrimitives(newBoardState);
        boardDTO.setBoard(newTransferBoard);
        return boardDTO;
    }

    public Board startNewGame(int size) {
        board.init(size);
        return board;
    }

    public BoardDTO move(MoveDTO moveDTO) {
        Optional<Player>[][] transformedBoardState = transformPrimitivesArrayToOptionals(moveDTO.getBoard());
        Player player = Player.valueOf(moveDTO.getPlayer());
        this.board.setBoard(transformedBoardState);
        Board board = makeMove(this.board, player, moveDTO.getxCoord(), moveDTO.getyCoord());
        this.boardDTO.setBoard(transformOptionalsArrayyToPrimitives(board.getBoard()));
        return boardDTO;
    }

    public BoardDTO check(BoardDTO boardDTO) {
        this.board.setBoard(transformPrimitivesArrayToOptionals(boardDTO.getBoard()));
        Player winner = checkState(this.board);
        if (winner != null)
            boardDTO.setWinnner(winner.name());
        return boardDTO;
    }

    public Player checkState(Board board) {
        Optional<Player> straightWinning = checkStraightSequences(board);
        Optional<Player> diagonalWinning = checkDiagonalsSequences(board);
        return straightWinning.isPresent() ? straightWinning.get() :
                diagonalWinning.isPresent() ? diagonalWinning.get() : null;
    }

    public Board makeMove(Board board, Player player, int xCord, int yCord) {
        Optional<Player>[][] boardState = board.getBoard();
        if (xCord >= boardState.length || yCord >= boardState.length)
            throw new ArrayIndexOutOfBoundsException();
        if (boardState[xCord][yCord].isPresent())
            throw new BusyPositionException("Position not available");
        boardState[xCord][yCord] = Optional.ofNullable(player);
        return board;
    }

    protected Optional<Player> checkStraightSequences(Board board) {
        Optional<Player>[][] boardState = board.getBoard();
        List<List<Optional<Player>>> straightSequences = new ArrayList<>(boardState.length);
        for (int column = 0; column < boardState.length; column++) {
            List<Optional<Player>> rowSequence = new ArrayList<>(boardState.length);
            List<Optional<Player>> columnSequence = new ArrayList<>(boardState.length);
            for (int row = 0; row < boardState.length; row++) {
                rowSequence.add(boardState[column][row]);
                columnSequence.add(boardState[row][column]);
            }
            straightSequences.add(rowSequence);
            straightSequences.add(columnSequence);
        }
        return validateSequences(straightSequences);
    }

    protected Optional<Player> checkDiagonalsSequences(Board board) {
        Optional<Player>[][] boardState = board.getBoard();
        int size = boardState.length;
        List<List<Optional<Player>>> diagonals = new ArrayList<>(size);

        // left to top
        for (int k = 0; k < size; k++) {
            List<Optional<Player>> diagonal = new ArrayList<>(size);
            for (int column = 0, row = k; row >= 0; row--, column++) {
                diagonal.add(boardState[column][row]);
            }
            diagonals.add(diagonal);
        }
        //bottom to right
        for (int k = 0; k < size; k++) {
            List<Optional<Player>> diagonal = new ArrayList<>(size);
            for (int column = k, row = size - 1; column < size; column++, row--) {
                diagonal.add(boardState[column][row]);
            }
            diagonals.add(diagonal);
        }
        //top to right
        for (int k = 0; k < size; k++) {
            List<Optional<Player>> diagonal = new ArrayList<>();
            for (int column = k, row = 0; column < size; row++, column++) {
                diagonal.add(boardState[column][row]);
            }
            diagonals.add(diagonal);
        }
        //left to bottom
        for (int k = 0; k < size; k++) {
            List<Optional<Player>> diagonal = new ArrayList<>(size);
            for (int column = 0, row = k; row < size; column++, row++) {
                diagonal.add(boardState[column][row]);
            }
            diagonals.add(diagonal);
        }
        return validateSequences(diagonals);
    }

    protected Optional<Player> validateSequences(List<List<Optional<Player>>> sequences) {
        for (List<Optional<Player>> sequence : sequences) {
            if (sequence.size() >= MARKS_TO_WIN) {
                for (int idx = 0; idx <= sequence.size() / 2; idx++) {
                    int consecutiveOccurrences = 1;
                    int iterator = idx + 1;
                    Optional<Player> plOnStartPosition = sequence.get(idx);
                    while (iterator < sequence.size()) {
                        if (plOnStartPosition.isPresent() && sequence.get(iterator).isPresent()
                                && sequence.get(iterator).get().equals(plOnStartPosition.get())) {
                            consecutiveOccurrences++;
                            iterator++;
                        } else {
                            iterator = sequence.size();
                        }
                        if (consecutiveOccurrences >= MARKS_TO_WIN)
                            return plOnStartPosition;
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Player>[][] transformPrimitivesArrayToOptionals(String[][] boardState) {
        int length = boardState.length;
        Optional<Player>[][] transformedBoardState = new Optional[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                transformedBoardState[column][row] = boardState[column][row].equals(" ") ?
                        Optional.empty() : Optional.of(Player.valueOf(boardState[column][row]));
            }
        }
        return transformedBoardState;
    }

    private String[][] transformOptionalsArrayyToPrimitives(Optional<Player>[][] boardState) {
        int length = boardState.length;
        String[][] transformedBoardState = new String[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                transformedBoardState[column][row] = boardState[column][row].isPresent() ?
                        boardState[column][row].get().name() : " ";
            }
        }
        return transformedBoardState;
    }
}
