package com.live.guessnumber.repository;

import com.live.guessnumber.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoundRepository extends JpaRepository<Round, Long> {
    Round findByIsFinishedFalse();
}
