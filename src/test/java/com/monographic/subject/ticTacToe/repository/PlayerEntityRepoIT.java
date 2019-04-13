package com.monographic.subject.ticTacToe.repository;

import com.monographic.subject.ticTacToe.entity.Player;
import com.monographic.subject.ticTacToe.entity.PlayerBE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerEntityRepoIT {

    @Autowired
    private PlayerEntityRepo playerEntityRepo;

    @Test
    public void shouldSavePlayerEntity() {
        PlayerBE createdPlayer = playerEntityRepo
                .save(new PlayerBE(1l, Player.PLAYER1));
        Optional<PlayerBE> foundPlayer = playerEntityRepo
                .findById(createdPlayer.getId());
        assertThat(foundPlayer).isPresent();
        assertThat(foundPlayer.get().getId()).isEqualTo(1L);
        assertThat(foundPlayer.get().getPlayer())
                .isEqualTo(Player.PLAYER1)
                .isNotEqualTo(Player.PLAYER2);
    }
}
