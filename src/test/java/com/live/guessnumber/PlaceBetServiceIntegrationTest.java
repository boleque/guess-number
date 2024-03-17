package com.live.guessnumber;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.repository.PlayerRepository;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.service.PlaceBetService;
import com.live.guessnumber.service.PlaceBetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;


@DataJpaTest
@ComponentScan(basePackages = {"com.live.guessnumber.repository"})
public class PlaceBetServiceIntegrationTest {

    private final static int MIN_GUESS_VALUE = 1;

    private final static int MAX_GUESS_VALUE = 10;

    private final static int GUESS_NUMBER = 5;

    private final static BigDecimal PLAYER_BALANCE = BigDecimal.valueOf(1000);

    private final static BigDecimal STAKE = BigDecimal.valueOf(500);

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @MockBean
    private GameProperties gameProperties;

    private final static String PLAYER_NICKNAME = "bot_1710488164282";

    @BeforeEach
    public void setUp() {
        when(gameProperties.getMinGuessValue()).thenReturn(MIN_GUESS_VALUE);
        when(gameProperties.getMaxGuessValue()).thenReturn(MAX_GUESS_VALUE);
    }

    @Test
    public void shouldCorrectlyPlaceBet() {
        final Round round = em.persistAndFlush(new Round(null, null, false, GUESS_NUMBER));
        final Player player = em.persistAndFlush(new Player(null, PLAYER_NICKNAME, PLAYER_BALANCE));


        final PlaceBetService placeBetService = new PlaceBetServiceImpl(
                betRepository, roundRepository, playerRepository, gameProperties
        );

        Bet bet = assertDoesNotThrow(() -> placeBetService.placeBet(PLAYER_NICKNAME, round.getId(), STAKE, GUESS_NUMBER));

        Bet winExpectedBet = em.find(Bet.class, bet.getId());
        assertThat(winExpectedBet)
                .matches(b -> b.getRound().equals(round))
                .matches(b -> b.getPlayer().getNickname().equals(PLAYER_NICKNAME));

        Player expectedPlayer = em.find(Player.class, player.getId());
        assertThat(expectedPlayer)
                .matches(p -> p.getBalance().compareTo(PLAYER_BALANCE.subtract(STAKE)) == 0);
    }
}
