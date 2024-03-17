package com.live.guessnumber.service;


import com.live.guessnumber.model.Bet;
import com.live.guessnumber.repository.BetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RoundWinStatisticsServiceImpl implements RoundWinStatisticsService {

    private final BetRepository betRepository;

    @Override
    public Map<String, Long> calculateWinStatistics() {
        List<Bet> betList = betRepository.findAll();
        return betList.stream()
                .filter(bet -> bet.getRound().isFinished() && bet.isWon())
                .collect(Collectors.groupingBy(bet -> bet.getPlayer().getNickname(), Collectors.counting()));
    }
}
