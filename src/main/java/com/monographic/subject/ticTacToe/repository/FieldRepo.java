package com.monographic.subject.ticTacToe.repository;

import com.monographic.subject.ticTacToe.entity.FieldBE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepo extends JpaRepository<FieldBE, Long> {
    List<FieldBE> findByBoardId(long boardId);
}
