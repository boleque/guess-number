package com.live.guessnumber.service;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.exceptions.IncorrectGuessNumber;
import com.live.guessnumber.exceptions.RoundNotFoundException;
import com.live.guessnumber.exceptions.RoundExpiredException;
import com.live.guessnumber.exceptions.BalanceNotEnoughException;
import com.live.guessnumber.exceptions.BetAlreadyDoneException;
import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.repository.PlayerRepository;
import com.live.guessnumber.repository.RoundRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PlaceBetServiceImpl implements PlaceBetService {

    private final BetRepository betRepository;

    private final RoundRepository roundRepository;

    private final PlayerRepository playerRepository;

    private final GameProperties gameProperties;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Bet placeBet(String nickname, long roundId, BigDecimal stake, int guess) throws Exception {
        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new RoundNotFoundException("Round %s not found".formatted(roundId)));
        Player player = playerRepository.findByNickname(nickname);

        checkGuessCorrectness(guess);
        checkRoundIsActive(round);
        checkPlayerBalanceSufficient(player, stake);
        checkBetIsNotDoneYet(player, round);

        player.setBalance(player.getBalance().subtract(stake));

        Bet bet = new Bet();
        bet.setRound(round);
        bet.setPlayer(player);
        bet.setGuess(guess);
        bet.setBetDt(Timestamp.valueOf(LocalDateTime.now()));
        bet.setStake(stake);
        return betRepository.save(bet);
    }

    private void checkGuessCorrectness(int guess) throws IncorrectGuessNumber {
        if (guess < gameProperties.getMinGuessValue() || guess > gameProperties.getMaxGuessValue()) {
            String errMessage = "Incorrect guess value: %s".formatted(guess);
            throw new IncorrectGuessNumber(errMessage);
        }
    }

    private void checkRoundIsActive(Round round) throws RoundExpiredException {
        if (round.isFinished()) {
            String errMessage = "Round: %s is expired".formatted(round.getId());
            throw new RoundExpiredException(errMessage);
        }
    }

    private void checkPlayerBalanceSufficient(Player player, BigDecimal stake) throws BalanceNotEnoughException {
        BigDecimal currentBalance = player.getBalance();
        BigDecimal newBalance = currentBalance.subtract(stake);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            String errMessage = "Player has insufficient balance: %s for stake: %s".formatted(currentBalance, stake);
            throw new BalanceNotEnoughException(errMessage);
        }
    }

    private void checkBetIsNotDoneYet(Player player, Round round) throws BetAlreadyDoneException {
        Optional<Bet> previouslyDoneBet = betRepository.findBetByPlayerIdAndRoundId(player.getId(), round.getId());
        if (previouslyDoneBet.isPresent()) {
            String errMessage = "Bet is already done";
            throw new BetAlreadyDoneException(errMessage);
        }
    }
}
