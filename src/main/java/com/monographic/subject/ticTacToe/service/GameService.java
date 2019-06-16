package com.monographic.subject.ticTacToe.service;

import com.monographic.subject.ticTacToe.model.dto.BoardDTO;
import com.monographic.subject.ticTacToe.model.dto.MoveDTO;
import com.monographic.subject.ticTacToe.model.dto.PersonalDataDTO;
import com.monographic.subject.ticTacToe.model.dto.PlayerDTO;

public interface GameService {
    BoardDTO createNewGame(int size);

    BoardDTO move(MoveDTO moveDTO, long gameNo);

    BoardDTO check(long gameNo);

    PlayerDTO getPlayerStatistics(long playerNo);

    String getPlayer(long playerNo);

    PersonalDataDTO getPlayerInfo(long id);

    PersonalDataDTO setPlayerInfo(PersonalDataDTO playerInfo, long id);
}