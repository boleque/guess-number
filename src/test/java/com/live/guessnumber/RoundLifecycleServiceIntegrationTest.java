package com.live.guessnumber;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.RoundLifecycleService;
import com.live.guessnumber.service.RoundLifecycleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@DataJpaTest
@ComponentScan(basePackages = {"com.live.guessnumber.repository"})
public class RoundLifecycleServiceIntegrationTest {

    private final static int MIN_GUESS_VALUE = 1;

    private final static int MAX_GUESS_VALUE = 10;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private TestEntityManager em;

    @MockBean
    private GameProperties gameProperties;

    @BeforeEach
    public void setUp() {
        given(gameProperties.getMinGuessValue()).willReturn(MIN_GUESS_VALUE);
        given(gameProperties.getMaxGuessValue()).willReturn(MAX_GUESS_VALUE);
    }

    @Test
    public void shouldCorrectlyStartAndFinishRound() {
        RoundLifecycleService playerRegistrationService = new RoundLifecycleServiceImpl(roundRepository, gameProperties);

        // start round
        long roundId = playerRegistrationService.startRound();
        Round expectedStartedRound = em.find(Round.class, roundId);
        assertThat(expectedStartedRound).isNotNull()
                .matches(s -> s.getGuess() == 0)
                .matches(s -> !s.isFinished());

        // finish round
        playerRegistrationService.finishRound(roundId);
        Round expectedFinishedRound = em.find(Round.class, roundId);
        assertThat(expectedFinishedRound).isNotNull()
                .matches(s -> s.getGuess() != 0)
                .matches(Round::isFinished);
    }
}
