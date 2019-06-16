package com.monographic.subject.ticTacToe.rest;

import com.monographic.subject.ticTacToe.model.dto.BoardDTO;
import com.monographic.subject.ticTacToe.model.dto.MoveDTO;
import com.monographic.subject.ticTacToe.model.dto.PersonalDataDTO;
import com.monographic.subject.ticTacToe.model.dto.PlayerDTO;
import com.monographic.subject.ticTacToe.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @Autowired
    GameServiceImpl gameServiceImpl;

    @RequestMapping(
            value = "/newGame",
            method = RequestMethod.GET,
            produces = "application/json")
    public BoardDTO startNewGame(@RequestParam(value = "size", defaultValue = "10") int size) {
        return gameServiceImpl.createNewGame(size);
    }

    @RequestMapping(
            value = "/move/{gameNo}",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public BoardDTO makeMove(@RequestBody MoveDTO moveDTO, @PathVariable long gameNo) {
        return gameServiceImpl.move(moveDTO, gameNo);
    }

    @RequestMapping(
            value = "/checkState/{gameNo}",
            method = RequestMethod.GET,
            produces = "application/json")
    public BoardDTO checkState(@PathVariable long gameNo) {
        return gameServiceImpl.check(gameNo);
    }

    @RequestMapping(
            value = "/player/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    public String getPlayer(@PathVariable long id) {
        return gameServiceImpl.getPlayer(id);
    }

    @RequestMapping(
            value = "/player/{id}/statistics",
            method = RequestMethod.GET,
            produces = "application/json")
    public PlayerDTO getPlayerStatistics(@PathVariable long id) {
        return gameServiceImpl.getPlayerStatistics(id);
    }

    @RequestMapping(
            value = "player/{id}/info",
            method = RequestMethod.GET,
            produces = "application/json")
    public PersonalDataDTO getPlayerInfo(@PathVariable long id) {
        return gameServiceImpl.getPlayerInfo(id);
    }

    @RequestMapping(
            value = "player/{id}/edit",
            method = RequestMethod.PUT,
            produces = "application/json")
    public PersonalDataDTO setPlayerInfo(@RequestBody PersonalDataDTO playerInfo, @PathVariable long id) {
        return gameServiceImpl.setPlayerInfo(playerInfo, id);
    }
}
