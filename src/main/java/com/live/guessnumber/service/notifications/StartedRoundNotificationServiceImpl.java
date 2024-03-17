package com.live.guessnumber.service.notifications;

import com.live.guessnumber.service.websocket.SessionRegistry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
@AllArgsConstructor
public class StartedRoundNotificationServiceImpl implements StartedRoundNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(StartedRoundNotificationServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    @Override
    public void notifyRoundStarted(long roundId) {
        TextMessage message = notificationMessage(roundId);
        for (WebSocketSession session : sessionRegistry.getAllSessions()) {
            try {
                session.sendMessage(message);
            } catch (IOException ex) {
                LOG.error("Failed to send round {} start notification for the session: {}", roundId, session.getId(), ex);
            }
        }
    }

    private TextMessage notificationMessage(long roundId) {
        String message = "Round %s started. Make a guess!".formatted(roundId);
        return new TextMessage(message);
    }
}
