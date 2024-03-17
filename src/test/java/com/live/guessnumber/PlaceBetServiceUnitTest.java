package com.live.guessnumber;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.exceptions.BalanceNotEnoughException;
import com.live.guessnumber.exceptions.BetAlreadyDoneException;
import com.live.guessnumber.exceptions.IncorrectGuessNumber;
import com.live.guessnumber.exceptions.RoundExpiredException;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {PlaceBetServiceImpl.class})
@ExtendWith({SpringExtension.class})
public class PlaceBetServiceUnitTest {

    private final static int MIN_GUESS_VALUE = 1;

    private final static int MAX_GUESS_VALUE = 10;

    private final static long ROUND_ID = 1L;

    private final static long PLAYER_ID = 1L;

    private final static BigDecimal CORRECT_STAKE = BigDecimal.valueOf(0);

    private final static BigDecimal CORRECT_BALANCE = BigDecimal.valueOf(1);

    private final static int CORRECT_GUESS = 1;

    private final static String PLAYER_NICKNAME = "bot_1710488164282";

    private final Round ACTIVE_ROUND_ENTITY = new Round(ROUND_ID, null, false, CORRECT_GUESS);

    private final Player PLAYER_ENTITY = new Player(PLAYER_ID, PLAYER_NICKNAME, CORRECT_BALANCE);

    @Autowired
    private PlaceBetService betService;

    @MockBean
    private BetRepository betRepository;

    @MockBean
    private RoundRepository roundRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private GameProperties gameProperties;

    @BeforeEach
    public void setUp() {
        when(gameProperties.getMinGuessValue()).thenReturn(MIN_GUESS_VALUE);
        when(gameProperties.getMaxGuessValue()).thenReturn(MAX_GUESS_VALUE);
    }

    @Test
    public void shouldFailedIfGuessIsIncorrect() {
        final int incorrectGuess = 11;

        when(roundRepository.findById(anyLong())).thenReturn(Optional.of(ACTIVE_ROUND_ENTITY));
        when(playerRepository.findByNickname(anyString())).thenReturn(PLAYER_ENTITY);

        assertThrows(IncorrectGuessNumber.class,
                () -> betService.placeBet(
                        PLAYER_NICKNAME,
                        ROUND_ID,
                        CORRECT_STAKE,
                        incorrectGuess
                ));
    }

    @Test
    public void shouldFailedIfRoundIsInactive() {
        final Optional<Round> finishedRound = Optional.of(
                new Round(
                        ROUND_ID,
                        null,
                        true, // round finished
                        CORRECT_GUESS
                ));

        when(roundRepository.findById(anyLong())).thenReturn(finishedRound);
        when(playerRepository.findByNickname(anyString())).thenReturn(PLAYER_ENTITY);

        assertThrows(RoundExpiredException.class,
                () -> betService.placeBet(
                        PLAYER_NICKNAME,
                        ROUND_ID,
                        CORRECT_STAKE,
                        CORRECT_GUESS
                ));
    }

    @Test
    public void shouldFailedIfPlayerBalanceIsNotSufficient() {
        final BigDecimal balance = BigDecimal.valueOf(1);
        final BigDecimal stakeMoreThatBalance = BigDecimal.valueOf(2);
        final Player playerWithNotSufficientBalance = new Player(PLAYER_ID, PLAYER_NICKNAME, balance);

        when(roundRepository.findById(anyLong())).thenReturn(Optional.of(ACTIVE_ROUND_ENTITY));
        when(playerRepository.findByNickname(anyString())).thenReturn(playerWithNotSufficientBalance);

        assertThrows(BalanceNotEnoughException.class, () -> betService.placeBet(
                PLAYER_NICKNAME,
                ROUND_ID,
                stakeMoreThatBalance,
                CORRECT_GUESS
        ));
    }

    @Test
    public void shouldFailedIfBetIsAlreadyDone() {
        final Optional<Bet> alreadyDoneBet = Optional.of(new Bet());

        when(roundRepository.findById(anyLong())).thenReturn(Optional.of(ACTIVE_ROUND_ENTITY));
        when(playerRepository.findByNickname(anyString())).thenReturn(PLAYER_ENTITY);
        when(betRepository.findBetByPlayerIdAndRoundId(anyLong(), anyLong())).thenReturn(alreadyDoneBet);

        assertThrows(BetAlreadyDoneException.class, () -> betService.placeBet(
                PLAYER_NICKNAME,
                ROUND_ID,
                CORRECT_STAKE,
                CORRECT_GUESS
        ));
    }
}
