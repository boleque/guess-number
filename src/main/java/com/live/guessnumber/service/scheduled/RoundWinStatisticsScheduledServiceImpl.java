package com.live.guessnumber.service.scheduled;

import com.live.guessnumber.integration.WinStatisticsGateway;
import com.live.guessnumber.service.RoundWinStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


@Component
@AllArgsConstructor
public class RoundWinStatisticsScheduledServiceImpl implements RoundWinStatisticsScheduledService {

    private static final Logger LOGGER = Logger.getLogger(RoundWinStatisticsScheduledServiceImpl.class.getName());

    private final RoundWinStatisticsService roundWinStatisticsService;

    private final WinStatisticsGateway winStatisticsGateway;

    @Scheduled(fixedRateString = "${game.win-stat-rate-ms}")
    @Transactional(readOnly = true)
    public void calculateWinStatistics() {
        LOGGER.info("Calculate total players wins statistics");
        notifyWinStatsReady(roundWinStatisticsService.calculateWinStatistics());
    }

    private void notifyWinStatsReady(Map<String, Long> playersStat) {
        Executors.newSingleThreadExecutor().submit(() -> winStatisticsGateway.process(playersStat));
    }
}
