package com.live.guessnumber.service;


public interface RoundLifecycleService {
    long startRound();

    void finishRound(long roundId);
}
