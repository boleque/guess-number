package com.live.guessnumber;


import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.RoundResultEvaluationService;
import com.live.guessnumber.service.RoundResultEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
@ComponentScan(basePackages = {"com.live.guessnumber.repository"})
public class RoundResultEvaluationServiceIntegrationTest {

    private final static BigDecimal WIN_STAKE_MULTIPLIER = BigDecimal.valueOf(9);

    private final static BigDecimal BALANCE = BigDecimal.valueOf(1000);

    private final static String PLAYER_WINNER_NICKNAME = "winner";

    private final static String PLAYER_LOSER_NICKNAME = "loser";

    private final static int WIN_GUESS_NUMBER = 5;

    private final static BigDecimal STAKE = BigDecimal.valueOf(100);

    private final static int WRONG_GUESS_NUMBER = 1;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private RoundRepository roundRepository;

    @MockBean
    private GameProperties gameProperties;

    @Test
    public void shouldEvaluateRoundResultCorrectly() {
        final Round round = em.persistAndFlush(new Round(null, null, true, WIN_GUESS_NUMBER));

        final Player playerWinner = em.persistAndFlush(new Player(null, PLAYER_WINNER_NICKNAME, BALANCE));
        final Player playerLoser = em.persistAndFlush(new Player(null, PLAYER_LOSER_NICKNAME, BALANCE));

        Timestamp betTs = Timestamp.valueOf(LocalDateTime.now());
        final Bet winBet = em.persistAndFlush(new Bet(null, betTs, WIN_GUESS_NUMBER, STAKE, false, round, playerWinner));
        final Bet lostBet = em.persistAndFlush(new Bet(null, betTs, WRONG_GUESS_NUMBER, STAKE, false, round, playerLoser));

        when(gameProperties.getWinStakeMultiplier()).thenReturn(WIN_STAKE_MULTIPLIER);

        RoundResultEvaluationService roundResultEvaluationService = new RoundResultEvaluationServiceImpl(
                betRepository, roundRepository, gameProperties
        );

        roundResultEvaluationService.evaluateRoundResult(round.getId());

        // check winner gain a profit
        Player winnerExpectedPlayer = em.find(Player.class, playerWinner.getId());
        assertThat(winnerExpectedPlayer)
                .matches(epw -> epw.getNickname().equals(PLAYER_WINNER_NICKNAME))
                .matches(epw -> epw.getBalance().compareTo(BALANCE.add(WIN_STAKE_MULTIPLIER.multiply(STAKE))) == 0);

        // check loser lost stake amount
        Player loserExpectedPlayer = em.find(Player.class, playerLoser.getId());
        assertThat(loserExpectedPlayer)
                .matches(epw -> epw.getNickname().equals(PLAYER_LOSER_NICKNAME))
                .matches(epw -> epw.getBalance().compareTo(BALANCE.subtract(STAKE)) == 0);

        // check winner bet marked as win
        Bet winExpectedBet = em.find(Bet.class, winBet.getId());
        assertThat(winExpectedBet)
                .matches(Bet::isWon);

        // check loser bet marked as lost
        Bet lostExpectedBet = em.find(Bet.class, lostBet.getId());
        assertThat(lostExpectedBet)
                .matches(leb -> !leb.isWon());
    }
}
