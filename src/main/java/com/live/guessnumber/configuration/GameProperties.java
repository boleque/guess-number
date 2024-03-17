package com.live.guessnumber.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Data
@Component
public class GameProperties {

    @Value("${game.min-guess-value}")
    private int minGuessValue;

    @Value("${game.max-guess-value}")
    private int maxGuessValue;

    @Value("${game.win-stake-multiplier}")
    private BigDecimal winStakeMultiplier;

    @Value("${game.bots}")
    private boolean bots;

    @Value("${game.initial-balance}")
    private BigDecimal initialBalance;
}
