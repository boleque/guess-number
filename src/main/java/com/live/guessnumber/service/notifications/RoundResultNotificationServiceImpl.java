package com.live.guessnumber.service.notifications;

import com.live.guessnumber.service.websocket.SessionRegistry;
import com.live.guessnumber.domains.RoundResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
@AllArgsConstructor
public class RoundResultNotificationServiceImpl implements RoundResultNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(RoundResultNotificationServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    @Override
    public void notifyRoundResult(RoundResult itemResult) {
        String nickname = itemResult.getNickname();
        if (sessionRegistry.isPlayerOnline(nickname)) {
            WebSocketSession session = sessionRegistry.getSessionByNickname(nickname);
            TextMessage message = notificationMessage(itemResult);
            try {
                session.sendMessage(message);
            } catch (IOException ex) {
                long roundId = itemResult.getRoundId();
                LOG.error("Failed to send round: {} result for the player with session: {}", roundId, nickname, ex);
            }
        }
    }

    private TextMessage notificationMessage(RoundResult roundResult) {
        return new TextMessage("You %s! Round id: %s".formatted(
                roundResult.isWin() ? "win" : "lost",
                roundResult.getRoundId()
        ));
    }
}

