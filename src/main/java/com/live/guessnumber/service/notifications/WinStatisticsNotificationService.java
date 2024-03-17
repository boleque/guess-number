package com.live.guessnumber.service.notifications;

import java.util.Map;


public interface WinStatisticsNotificationService {
    void notifyWinStatistics(Map<String, Long> winStatistics);
}
