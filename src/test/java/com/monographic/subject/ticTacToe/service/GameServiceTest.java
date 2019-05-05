package com.monographic.subject.ticTacToe.service;

import com.monographic.subject.ticTacToe.entity.*;
import com.monographic.subject.ticTacToe.repository.FieldRepo;
import com.monographic.subject.ticTacToe.repository.PlayerEntityRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {
    private static final MoveDTO MOVE_DTO_1 = new MoveDTO();
    private static final BoardDTO BOARD_DTO = new BoardDTO();

    @InjectMocks
    private GameService gameService;

    @Mock
    PlayerEntityRepo playerEntityRepo;

    @Mock
    FieldRepo fieldRepo;

    @Spy
    BoardDTO boardDTO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldMakeMove() {
        // given
        MOVE_DTO_1.setPlayer("PLAYER1");
        MOVE_DTO_1.setxCoord(0);
        MOVE_DTO_1.setyCoord(0);

        List<FieldBE> fields = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            fields.add(i, new FieldBE(i % 10, i / 10, null, null));
        }

        String[][] newBoard = generateEmptyBoard();
        newBoard[0][0] = "PLAYER1";
        BOARD_DTO.setBoard(newBoard);
        Mockito.when(playerEntityRepo.findByPlayer(Mockito.any()))
                .thenReturn(new PlayerBE(1l, Player.PLAYER1));
        Mockito.when(fieldRepo.findByBoardId(Mockito.anyLong()))
                .thenReturn(fields);

        // when
        BoardDTO board = gameService.move(MOVE_DTO_1, 1l);

        // then
        assertThat(board.getBoard().length).isEqualTo(10);
        for (int row = 0; row < 10; row++) {
            assertThat(board.getBoard()[row]).containsExactly(newBoard[row]);
        }
    }

    private String[][] generateEmptyBoard() {
        String[][] board = new String[10][10];
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                board[column][row] = " ";
            }
        }
        return board;
    }

}
