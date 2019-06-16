package com.monographic.subject.ticTacToe.repository;

import com.monographic.subject.ticTacToe.model.entity.BoardBE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepo extends JpaRepository<BoardBE, Long> {
}
