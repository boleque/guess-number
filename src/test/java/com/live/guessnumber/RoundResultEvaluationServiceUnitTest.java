package com.live.guessnumber;


import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.domains.RoundResult;
import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.RoundResultEvaluationService;
import com.live.guessnumber.service.RoundResultEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest(classes = {RoundResultEvaluationServiceImpl.class})
@ExtendWith({SpringExtension.class})
public class RoundResultEvaluationServiceUnitTest {

    private final static BigDecimal WIN_STAKE_MULTIPLIER = BigDecimal.valueOf(9);

    private final static BigDecimal BALANCE = BigDecimal.valueOf(100);

    private final static BigDecimal STAKE = BigDecimal.valueOf(50);

    private final static long ROUND_ID = 1L;

    private final static long PLAYER_WINNER_ID = 1L;

    private final static long PLAYER_LOSER_ID = 2L;

    private final static String PLAYER_WINNER_NICKNAME = "winner";

    private final static String PLAYER_LOSER_NICKNAME = "loser";

    private final static long PLAYER_WINNER_BET_ID = 1L;

    private final static long PLAYER_LOSER_BET_ID = 2L;

    private final static int WIN_GUESS_NUMBER = 5;

    private final static int WRONG_GUESS_NUMBER = 1;

    private final static int EXPECTED_NUMBER_OF_RESULT_REPORTS = 2;

    private final static int RESULT_REPORT_WINNER_IDX = 0;

    private final static int RESULT_REPORT_LOSER_IDX = 1;

    @Autowired
    private RoundResultEvaluationService roundResultEvaluationService;

    @MockBean
    private BetRepository betRepository;

    @MockBean
    private RoundRepository roundRepository;

    @MockBean
    private GameProperties gameProperties;

    @Test
    public void shouldEvaluateRoundResultCorrectly() {
        final Round round = new Round(ROUND_ID, null, true, WIN_GUESS_NUMBER);
        final Player playerWinner = new Player(PLAYER_WINNER_ID, "winner", BALANCE);
        final Player playerLoser = new Player(PLAYER_LOSER_ID, "loser", BALANCE);

        final List<Bet> bets = List.of(
                new Bet(PLAYER_WINNER_BET_ID, null, WIN_GUESS_NUMBER, STAKE, false, round, playerWinner),
                new Bet(PLAYER_LOSER_BET_ID, null, WRONG_GUESS_NUMBER, STAKE, false, round, playerLoser)
        );

        when(gameProperties.getWinStakeMultiplier()).thenReturn(WIN_STAKE_MULTIPLIER);
        when(roundRepository.getReferenceById(ROUND_ID)).thenReturn(round);
        when(betRepository.findBetsByRoundId(ROUND_ID)).thenReturn(bets);

        // service return round results for actual number of players
        List<RoundResult> roundResults = roundResultEvaluationService.evaluateRoundResult(ROUND_ID);
        assertThat(roundResults).isNotNull().hasSize(EXPECTED_NUMBER_OF_RESULT_REPORTS)
                .allMatch(s -> s.getRoundId() == ROUND_ID);

        // correct round result for the winner
        RoundResult roundResultWinner = roundResults.get(RESULT_REPORT_WINNER_IDX);
        assertThat(roundResultWinner)
                .matches(rr -> rr.getNickname().equals(PLAYER_WINNER_NICKNAME))
                .matches(RoundResult::isWin);

        // correct round result for the loser
        RoundResult roundResultLoser = roundResults.get(RESULT_REPORT_LOSER_IDX);
        assertThat(roundResultLoser)
                .matches(rr -> rr.getNickname().equals(PLAYER_LOSER_NICKNAME))
                .matches(s -> !s.isWin());
    }
}
