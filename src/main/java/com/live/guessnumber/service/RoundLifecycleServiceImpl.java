package com.live.guessnumber.service;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class RoundLifecycleServiceImpl implements RoundLifecycleService {

    private final RoundRepository roundRepository;

    private final GameProperties gameProperties;

    @Override
    public long startRound() {
        Round newRound = roundRepository.save(new Round());
        return newRound.getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void finishRound(long roundId) {
        int minGuessValue = gameProperties.getMinGuessValue();
        int maxGuessValue = gameProperties.getMaxGuessValue();
        int guess = Utils.generateGuessNumber(minGuessValue, maxGuessValue);
        Round round = roundRepository.getReferenceById(roundId);
        round.setGuess(guess);
        round.setFinished(true);
    }
}
