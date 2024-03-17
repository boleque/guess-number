package com.live.guessnumber.service.notifications;

import com.live.guessnumber.service.websocket.SessionRegistry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class WinStatisticsNotificationServiceImpl implements WinStatisticsNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(WinStatisticsNotificationServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    @Override
    public void notifyWinStatistics(Map<String, Long> winStatistics) {
        TextMessage message = notificationMessage(winStatistics);
        for (WebSocketSession session : sessionRegistry.getAllSessions()) {
            try {
                session.sendMessage(message);
            } catch (IOException ex) {
                LOG.error("Failed to send wins stats notification for the session: {}", session.getId(), ex);
            }
        }
    }

    private TextMessage notificationMessage(Map<String, Long> winStatistics) {
        String message = winStatistics.entrySet().
                stream()
                .map(e -> "nickname: " + e.getKey() + "; total wins: " + e.getValue())
                .collect(Collectors.joining(",\n"));
        return new TextMessage(message);
    }
}
