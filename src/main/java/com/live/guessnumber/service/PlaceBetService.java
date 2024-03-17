package com.live.guessnumber.service;

import com.live.guessnumber.model.Bet;

import java.math.BigDecimal;


public interface PlaceBetService {
    Bet placeBet(String nickname, long roundId, BigDecimal stake, int guess) throws Exception;
}
