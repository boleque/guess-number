package com.live.guessnumber;

import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.service.RoundWinStatisticsService;
import com.live.guessnumber.service.RoundWinStatisticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {RoundWinStatisticsServiceImpl.class})
@ExtendWith({SpringExtension.class})
public class RoundWinStatisticsServiceUnitTest {

    private final static int EXPECTED_NUMBER_OF_WINS = 1;

    private final static BigDecimal STAKE = BigDecimal.valueOf(1);

    @Autowired
    private RoundWinStatisticsService roundWinStatisticsService;

    @MockBean
    private BetRepository betRepository;

    @Test
    void shouldCalculateWinStatisticsCorrectly() {
        final Player player = new Player(1L, "bot_1710488164282", BigDecimal.valueOf(0));

        final Round round1 = new Round(1L, null, true, 1);
        final Round round2 = new Round(2L, null, true, 2);
        final Round round3 = new Round(3L, null, false, 3);

        final Bet winBet = new Bet(1L, null, 1, STAKE, true, round1, player);
        final Bet lostBet = new Bet(2L, null, 2, STAKE, false, round2, player);
        final Bet unfinishedRoundBet = new Bet(3L, null, 3, STAKE, true, round3, player);

        final List<Bet> bets = List.of(winBet, lostBet, unfinishedRoundBet);

        when(betRepository.findAll()).thenReturn(bets);

        Map<String, Long> winStats = roundWinStatisticsService.calculateWinStatistics();
        assertThat(winStats.values()).isNotNull().hasSize(EXPECTED_NUMBER_OF_WINS);
    }
}
