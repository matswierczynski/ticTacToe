package com.monographic.subject.ticTacToe.service.impl;

import com.monographic.subject.ticTacToe.model.dto.BoardDTO;
import com.monographic.subject.ticTacToe.model.dto.MoveDTO;
import com.monographic.subject.ticTacToe.model.dto.PersonalDataDTO;
import com.monographic.subject.ticTacToe.model.dto.PlayerDTO;
import com.monographic.subject.ticTacToe.model.entity.*;
import com.monographic.subject.ticTacToe.repository.BoardRepo;
import com.monographic.subject.ticTacToe.repository.FieldRepo;
import com.monographic.subject.ticTacToe.repository.PersonalDataRepo;
import com.monographic.subject.ticTacToe.repository.PlayerEntityRepo;
import com.monographic.subject.ticTacToe.service.GameService;
import error.BusyPositionException;
import error.NoSuchBoardException;
import error.NoSuchFieldException;
import error.NoSuchPlayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {
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

    @Autowired
    private PersonalDataRepo personalDataRepo;

    public BoardDTO createNewGame(int size) {
        Board newBoard = startNewGame(size);
        Optional<Player>[][] newBoardState = newBoard.getBoard();
        String[][] newTransferBoard = transformOptionalsArrayToPrimitives(newBoardState);
        boardDTO.setBoard(newTransferBoard);
        boardDTO.setWinner(null);
        return boardDTO;
    }

    public BoardDTO move(MoveDTO moveDTO, long gameNo) {
        Optional<List<FieldBE>> fields = Optional.ofNullable(fieldRepo.findByBoardId(gameNo));

        Optional<PlayerBE> player;
        try {
            player = Optional
                    .of(playerEntityRepo
                            .findByPlayer(Player.valueOf(moveDTO.getPlayer())));
        } catch (IllegalArgumentException e) {
            player = Optional.empty();
        }
        if (player.isPresent()) {
            if (fields.isPresent()) {
                Optional<FieldBE> field = fields.get()
                        .stream()
                        .filter(singleField ->
                                singleField.getxCoord() == moveDTO.getxCoord()
                                        && singleField.getyCoord() == moveDTO.getyCoord())
                        .findFirst();
                if (field.isPresent()) {
                    if (field.get().getPlayer() != null)
                        throw new BusyPositionException();
                    field.get().setPlayer(player.get());
                    fieldRepo.save(field.get());
                    boardDTO.setBoard(transformBoardBeToPrimitive(fields));
                    boardDTO.setWinner(null);
                    return boardDTO;
                } else {
                    throw new NoSuchBoardException();
                }
            } else {
                throw new NoSuchFieldException();
            }
        } else {
            throw new NoSuchPlayerException();
        }
    }

    public BoardDTO check(long gameNo) {
        boardDTO.setBoard(null);
        boardDTO.setWinner(null);
        Optional<List<FieldBE>> fields = Optional.ofNullable(fieldRepo.findByBoardId(gameNo));
        String[][] fieldsOfGame = transformBoardBeToPrimitive(fields);
        this.board.setBoard(transformPrimitivesArrayToOptionals(fieldsOfGame));
        Player winner = checkState(this.board);
        if (winner != null) {
            Optional<PlayerBE> looser = playerEntityRepo.findAll()
                    .stream()
                    .filter(player -> !player.getPlayer().name().equals(winner.name()))
                    .findFirst();
            PlayerBE winnerBE = playerEntityRepo.findByPlayer(winner);
            PlayerBE looserBE = looser.get();
            int wonGames = winnerBE.getWonGames();
            int lostGames = looserBE.getLostGames();
            winnerBE.setWonGames(++wonGames);
            looserBE.setLostGames(++lostGames);
            playerEntityRepo.save(winnerBE);
            playerEntityRepo.save(looserBE);
            boardDTO.setWinner(winner.name());
        } else {
            if (checkDraw(this.board)) {
                List<PlayerBE> players = playerEntityRepo.findAll();
                for (PlayerBE player : players) {
                    int drawnGames = player.getDrawnGames();
                    player.setDrawnGames(++drawnGames);
                    playerEntityRepo.save(player);
                }
                boardDTO.setWinner("draw");
            }
        }
        return boardDTO;
    }

    public PlayerDTO getPlayerStatistics(long playerNo) {
        Optional<PlayerBE> player = playerEntityRepo.findById(playerNo);
        PlayerDTO playerDTO = new PlayerDTO();
        if (player.isPresent()) {
            playerDTO.setPlayerName(player.get().getPlayer().name());
            playerDTO.setWonGames(player.get().getWonGames());
            playerDTO.setLostGames(player.get().getLostGames());
            playerDTO.setDrawnGames(player.get().getDrawnGames());
            return playerDTO;
        } else {
            throw new NoSuchPlayerException();
        }
    }

    public String getPlayer(long playerNo) {
        Optional<PlayerBE> playerObj = playerEntityRepo.findById(playerNo);
        StringBuilder player = new StringBuilder("{\"player\":");
        if (playerObj.isPresent()) {
            player.append(playerObj.get().getPlayer().name());
            return player.toString();
        } else {
            throw new NoSuchPlayerException();
        }
    }

    public PersonalDataDTO getPlayerInfo(long id) {
        Optional<PersonalDataBE> playerInfo = Optional.ofNullable(personalDataRepo.findFirstByPlayerId(id));
        PersonalDataDTO personalDataDTO = new PersonalDataDTO();
        if (playerInfo.isPresent()) {
            personalDataDTO.setPlayer(playerInfo.get().getPlayer().getPlayer().name());
            personalDataDTO.setName(playerInfo.get().getName());
            personalDataDTO.setSurname(playerInfo.get().getSurname());
            personalDataDTO.setEmail(playerInfo.get().getEmail());
            return personalDataDTO;
        } else {
            throw new NoSuchPlayerException();
        }
    }

    public PersonalDataDTO setPlayerInfo(PersonalDataDTO playerInfo, long id) {
        Optional<PlayerBE> player = playerEntityRepo.findById(id);
        if (player.isPresent()) {
            PersonalDataBE personalData = player.get().getPersonalData();
            personalData.setName(playerInfo.getName());
            personalData.setSurname(playerInfo.getSurname());
            personalData.setEmail(playerInfo.getEmail());
            personalDataRepo.save(personalData);
            return playerInfo;
        } else {
            throw new NoSuchPlayerException();
        }
    }

    Board startNewGame(int size) {
        board.init(size);
        BoardBE newBoard = new BoardBE();
        newBoard.setFields(createFieldsForNewBoard(size));
        boardRepo.save(newBoard);
        return board;
    }

    Player checkState(Board board) {
        Optional<Player> straightWinning = checkStraightSequences(board);
        Optional<Player> diagonalWinning = checkDiagonalsSequences(board);
        return straightWinning.isPresent() ? straightWinning.get() :
                diagonalWinning.isPresent() ? diagonalWinning.get() : null;
    }

    boolean checkDraw(Board board) {
        Optional<Player> winner;
        Optional<Player>[][] boardState = board.getBoard();
        List<Optional<Player>> stateList = new ArrayList<>();
        for (Optional<Player>[] row : boardState) {
            stateList.addAll(Arrays.asList(row));
        }
        if (stateList.stream().allMatch(Optional::isPresent)) {
            return true;
        } else {
            return false;
        }
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

    Optional<Player>[][] transformPrimitivesArrayToOptionals(String[][] boardState) {
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

    String[][] transformOptionalsArrayToPrimitives(Optional<Player>[][] boardState) {
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

    List<FieldBE> createFieldsForNewBoard(int size) {
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

    String[][] transformBoardBeToPrimitive(Optional<List<FieldBE>> fields) {
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
