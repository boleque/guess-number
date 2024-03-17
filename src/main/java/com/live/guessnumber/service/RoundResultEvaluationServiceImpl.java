package com.live.guessnumber.service;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.model.Bet;
import com.live.guessnumber.model.Player;
import com.live.guessnumber.model.Round;
import com.live.guessnumber.repository.BetRepository;
import com.live.guessnumber.repository.RoundRepository;
import com.live.guessnumber.domains.RoundResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class RoundResultEvaluationServiceImpl implements RoundResultEvaluationService {

    private final BetRepository betRepository;

    private final RoundRepository roundRepository;

    private final GameProperties gameProperties;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<RoundResult> evaluateRoundResult(long roundId) {
        List<RoundResult> roundResults = new ArrayList<>();
        Round round = roundRepository.getReferenceById(roundId);
        long winGuess = round.getGuess();
        List<Bet> bets = betRepository.findBetsByRoundId(roundId);
        for (Bet bet : bets) {
            Player player = bet.getPlayer();
            BigDecimal balance = player.getBalance();
            long guess = bet.getGuess();
            boolean isWon = guess == winGuess;
            if (isWon) {
                BigDecimal winCoefficient = gameProperties.getWinStakeMultiplier();
                balance = balance.add(bet.getStake().multiply(winCoefficient));
            } else {
                balance = balance.subtract(bet.getStake());
            }
            player.setBalance(balance);
            bet.setWon(isWon);
            roundResults.add(new RoundResult(roundId, isWon, player.getNickname()));
        }
        return roundResults;
    }
}
