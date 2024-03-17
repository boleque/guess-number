package com.live.guessnumber.service;

import com.live.guessnumber.domains.RoundResult;

import java.util.List;


public interface RoundResultEvaluationService {
    List<RoundResult> evaluateRoundResult(long roundId);
}
