package com.monographic.subject.ticTacToe.config;

import error.BusyPositionException;
import error.NoSuchBoardException;
import error.NoSuchPlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(BusyPositionException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Position already assigned to player")
    public void handleBusyPositionException() {
    }

    @ExceptionHandler(NoSuchBoardException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Board with given id does not exist")
    public void handleNoSuchBoardException() {
    }

    @ExceptionHandler(NoSuchFieldException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Position not available for specified board")
    public void handleNoSuchFieldException() {
    }

    @ExceptionHandler(NoSuchPlayerException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Player with given id does not exist")
    public void handleNoSuchPlayerException() {
    }
}
