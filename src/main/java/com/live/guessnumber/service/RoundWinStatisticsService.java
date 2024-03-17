package com.live.guessnumber.service;

import java.util.Map;

public interface RoundWinStatisticsService {
    Map<String, Long> calculateWinStatistics();
}
