package com.monographic.subject.ticTacToe.controller;

import com.monographic.subject.ticTacToe.entity.BoardDTO;
import com.monographic.subject.ticTacToe.entity.MoveDTO;
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
            value = "/move",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public BoardDTO makeMove(@RequestBody MoveDTO moveDTO) {
        return gameService.move(moveDTO);
    }

    @RequestMapping(
            value = "/checkState",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public BoardDTO checkState(@RequestBody BoardDTO boardDTO) {
        return gameService.check(boardDTO);
    }
}
