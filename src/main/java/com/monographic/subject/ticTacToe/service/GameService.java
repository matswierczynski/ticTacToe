package com.monographic.subject.ticTacToe.service;

import com.monographic.subject.ticTacToe.entity.*;
import com.monographic.subject.ticTacToe.repository.BoardRepo;
import com.monographic.subject.ticTacToe.repository.FieldRepo;
import com.monographic.subject.ticTacToe.repository.PlayerEntityRepo;
import exception.BusyPositionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameService {
    private int MARKS_TO_WIN = 5;

    @Autowired
    private Board board;

    @Autowired
    private BoardDTO boardDTO;

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private PlayerEntityRepo playerEntityRepo;

    public BoardDTO prepareNewGame(int size) {
        Board newBoard = startNewGame(size);
        Optional<Player>[][] newBoardState = newBoard.getBoard();
        String[][] newTransferBoard = transformOptionalsArrayToPrimitives(newBoardState);
        boardDTO.setBoard(newTransferBoard);
        boardDTO.setWinner("");
        return boardDTO;
    }

    Board startNewGame(int size) {
        board.init(size);
        BoardBE newBoard = new BoardBE();
        newBoard.setFields(createFieldsForNewBoard(size));
        boardRepo.save(newBoard);
        return board;
    }

    public BoardDTO move(MoveDTO moveDTO, long gameNo) {
        Optional<List<FieldBE>> fields = Optional.ofNullable(fieldRepo.findByBoardId(gameNo));
        Optional<PlayerBE> player = Optional
                .ofNullable(playerEntityRepo
                        .findByPlayer(Player.valueOf(moveDTO.getPlayer())));
        if (player.isPresent()) {
            fields.ifPresent(f -> {
                Optional<FieldBE> field = f
                        .stream()
                        .filter(singleField ->
                                singleField.getxCoord() == moveDTO.getxCoord()
                                        && singleField.getyCoord() == moveDTO.getyCoord())
                        .findFirst();
                field.ifPresent(singleField -> {
                    if (singleField.getPlayer() != null)
                        throw new BusyPositionException("Position not available");
                    singleField.setPlayer(player.get());
                    fieldRepo.save(singleField);
                    boardDTO.setBoard(transformBoardBeToPrimitive(fields));
                    boardDTO.setWinner("");
                });
            });
            return boardDTO;
        } else throw new NoSuchElementException("Player with name" + moveDTO.getPlayer() + "does not exist");
    }

    public BoardDTO check(long gameNo) {
        Optional<List<FieldBE>> fields = Optional.ofNullable(fieldRepo.findByBoardId(gameNo));
        String[][] fieldsOfGame = transformBoardBeToPrimitive(fields);
        this.board.setBoard(transformPrimitivesArrayToOptionals(fieldsOfGame));
        Player winner = checkState(this.board);
        if (winner != null)
            boardDTO.setWinner(winner.name());
        return boardDTO;
    }

    public PlayerDTO getPlayerStatistics(long playerNo) {
        Optional<PlayerBE> player = playerEntityRepo.findById(playerNo);
        PlayerDTO playerDTO = new PlayerDTO();
        player.ifPresent(p -> {
            playerDTO.setPlayerName(p.getPlayer().name());
            playerDTO.setWonGames(p.getWonGames());
            playerDTO.setLostGames(p.getLostGames());
            playerDTO.setDrawnGames(p.getDrawnGames());
        });
        return playerDTO;
    }

    public String getPlayer(long playerNo) {
        Optional<PlayerBE> playerObj = playerEntityRepo.findById(playerNo);
        StringBuilder player = new StringBuilder("{\"player\":");
        playerObj.ifPresent(p -> player.append(p.getPlayer().name()));
        return player.toString();
    }

    Player checkState(Board board) {
        Optional<Player> straightWinning = checkStraightSequences(board);
        Optional<Player> diagonalWinning = checkDiagonalsSequences(board);
        return straightWinning.isPresent() ? straightWinning.get() :
                diagonalWinning.isPresent() ? diagonalWinning.get() : null;
    }

    Optional<Player> checkStraightSequences(Board board) {
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

    Optional<Player> checkDiagonalsSequences(Board board) {
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

    Optional<Player> validateSequences(List<List<Optional<Player>>> sequences) {
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

    private String[][] transformOptionalsArrayToPrimitives(Optional<Player>[][] boardState) {
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

    private List<FieldBE> createFieldsForNewBoard(int size) {
        List<FieldBE> fields = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                FieldBE newField = new FieldBE();
                newField.setxCoord(column);
                newField.setyCoord(row);
                fields.add(newField);
            }
        }
        return fields;
    }

    private String[][] transformBoardBeToPrimitive(Optional<List<FieldBE>> fields) {
        if (fields.isPresent()) {
            List<FieldBE> field = fields.get();
            String[][] transformedBoard = new String[field.size() / 10][field.size() / 10];
            for (int i = 0; i < field.size(); i++) {
                int column = i % 10;
                int row = i / 10;
                Optional<PlayerBE> player = Optional.ofNullable(field.get(i).getPlayer());
                if (player.isPresent()) {
                    transformedBoard[column][row] = player.get().getPlayer().name();
                } else {
                    transformedBoard[column][row] = " ";
                }
            }
            return transformedBoard;
        } else
            throw new NoSuchElementException();
    }
}
