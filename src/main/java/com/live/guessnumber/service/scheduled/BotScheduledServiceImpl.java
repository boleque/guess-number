package com.live.guessnumber.service.scheduled;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.PlaceBetService;
import com.live.guessnumber.service.PlayerRegistrationService;
import com.live.guessnumber.service.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@AllArgsConstructor
public class BotScheduledServiceImpl implements BotScheduledService {

    private final GameProperties gameProperties;

    private PlayerRegistrationService playerRegistrationService;

    private PlaceBetService betService;

    private RoundRepository roundRepository;

    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void play() throws Exception {
        if (gameProperties.isBots()) {
            String nickname = generateNickname();
            register(nickname);
            makeBet(nickname);
        }
    }

    private String generateNickname() {
        return "bot_%s".formatted(System.currentTimeMillis());
    }

    private void register(String nickname) {
        playerRegistrationService.register(nickname, BigDecimal.valueOf(1000));
    }

    private int makeGuess() {
        int minGuessValue = gameProperties.getMinGuessValue();
        int maxGuessValue = gameProperties.getMaxGuessValue();
        return Utils.generateGuessNumber(minGuessValue, maxGuessValue);
    }

    private void makeBet(String nickname) throws Exception {
        int guess = makeGuess();
        Round activeRound = roundRepository.findByIsFinishedFalse();
        betService.placeBet(nickname, activeRound.getId(), BigDecimal.valueOf(200), guess);
    }
}
