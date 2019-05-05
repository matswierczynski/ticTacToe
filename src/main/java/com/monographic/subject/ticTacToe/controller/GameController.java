package com.monographic.subject.ticTacToe.controller;

import com.monographic.subject.ticTacToe.entity.BoardDTO;
import com.monographic.subject.ticTacToe.entity.MoveDTO;
import com.monographic.subject.ticTacToe.entity.PersonalDataDTO;
import com.monographic.subject.ticTacToe.entity.PlayerDTO;
import com.monographic.subject.ticTacToe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(
            value = "/newGame",
            method = RequestMethod.GET,
            produces = "application/json")
    public BoardDTO startNewGame(@RequestParam(value = "size", defaultValue = "10") int size) {
        return gameService.prepareNewGame(size);
    }

    @RequestMapping(
            value = "/move/{gameNo}",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public BoardDTO makeMove(@RequestBody MoveDTO moveDTO, @PathVariable long gameNo) {
        return gameService.move(moveDTO, gameNo);
    }

    @RequestMapping(
            value = "/checkState/{gameNo}",
            method = RequestMethod.GET,
            produces = "application/json")
    public BoardDTO checkState(@PathVariable long gameNo) {
        return gameService.check(gameNo);
    }

    @RequestMapping(
            value = "/player/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    public String getPlayer(@PathVariable long id) {
        return gameService.getPlayer(id);
    }

    @RequestMapping(
            value = "/player/{id}/statistics",
            method = RequestMethod.GET,
            produces = "application/json")
    public PlayerDTO getPlayerStatistics(@PathVariable long id) {
        return gameService.getPlayerStatistics(id);
    }

    @RequestMapping(
            value = "player/{id}/info",
            method = RequestMethod.GET,
            produces = "application/json")
    public PersonalDataDTO getPlayerInfo(@PathVariable long id) {
        return gameService.getPlayerInfo(id);
    }

    @RequestMapping(
            value = "player/{id}/edit",
            method = RequestMethod.PUT,
            produces = "application/json")
    public PersonalDataDTO setPlayerInfo(@RequestBody PersonalDataDTO playerInfo, @PathVariable long id) {
        return gameService.setPlayerInfo(playerInfo, id);
    }
}
