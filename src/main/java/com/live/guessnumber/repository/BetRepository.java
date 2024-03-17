package com.live.guessnumber.repository;

import com.live.guessnumber.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BetRepository extends JpaRepository<Bet, Long> {
    Optional<Bet> findBetByPlayerIdAndRoundId(Long player_id, Long round_id);

    List<Bet> findBetsByRoundId(Long round_id);
}
