package com.monographic.subject.ticTacToe.controller;

import exception.BusyPositionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class GameControllerIT {

    private static String EMPTY_BOARD_JSON = "{\"board\":" +
            "[[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]],";
    private static String PLAYER1_MOVE_BOARD_JSON = "{\"board\":" +
            "[[\"PLAYER1\",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]],";
    private static String PLAYER1_WINNER_BOARD_JSON = "{\"board\":" +
            "[[\"PLAYER1\",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\"PLAYER1\",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\"PLAYER1\",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\"PLAYER1\",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\"PLAYER1\",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]," +
            "[\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \",\" \"]],";
    private static String NEW_GAME_JSON = EMPTY_BOARD_JSON + "\"winner\":null}";
    private static String PLAYER1_MOVE_DTO = EMPTY_BOARD_JSON + "\"player\":\"PLAYER1\",\"xCoord\":0,\"yCoord\":0}";
    private static String PLAYER1_MOVED_DTO = PLAYER1_MOVE_BOARD_JSON + "\"winner\":null}";
    private static String PLAYER_ON_POSITION = PLAYER1_MOVE_BOARD_JSON + "\"player\":\"PLAYER1\",\"xCoord\":0,\"yCoord\":0}";
    private static String PLAYER_WINNER_BOARD = PLAYER1_WINNER_BOARD_JSON + "\"winner\":null}";
    private static String PLAYER_WINNER_BOARD_ANSWER = PLAYER1_WINNER_BOARD_JSON + "\"winner\":\"PLAYER1\"}";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldReturnNewGameObject() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/newGame"))
                .andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(NEW_GAME_JSON);
    }

    @Test
    public void shouldMakeMove() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/move")
                        .content(PLAYER1_MOVE_DTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(PLAYER1_MOVED_DTO);
    }

    @Test
    public void shouldNotMakeMoveWhenPositionBusy() {
        assertThatThrownBy(() ->
                this.mockMvc
                        .perform(post("/move")
                                .content(PLAYER_ON_POSITION)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isInternalServerError()))
                .hasCause(new BusyPositionException("Position not available"));
    }

    @Test
    public void shouldReturnWinner() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/checkState")
                        .content(PLAYER_WINNER_BOARD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(PLAYER_WINNER_BOARD_ANSWER);
    }

}
