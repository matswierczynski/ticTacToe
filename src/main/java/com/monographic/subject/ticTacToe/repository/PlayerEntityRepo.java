package com.monographic.subject.ticTacToe.repository;

import com.monographic.subject.ticTacToe.model.entity.Player;
import com.monographic.subject.ticTacToe.model.entity.PlayerBE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerEntityRepo extends JpaRepository<PlayerBE, Long> {
    PlayerBE findByPlayer(Player player);
}
