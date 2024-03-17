package com.live.guessnumber.service.websocket.handlers;

import com.live.guessnumber.configuration.GameProperties;
import com.live.guessnumber.service.PlayerRegistrationService;
import com.live.guessnumber.service.websocket.SessionRegistry;
import com.live.guessnumber.service.websocket.requests.Request;
import com.live.guessnumber.service.websocket.requests.RequestType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;


@Service(RequestType.START_GAME_)
@AllArgsConstructor
public class StartGameRequestHandler implements RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(StartGameRequestHandler.class);

    private final SessionRegistry sessionRegistry;

    private final PlayerRegistrationService playerRegistrationService;

    private final GameProperties gameProperties;

    @Override
    public void handle(Request request) {
        WebSocketSession session = request.getSession();
        String nickname = getNickname(session.getId());
        BigDecimal initialBalance = gameProperties.getInitialBalance();
        playerRegistrationService.register(nickname, initialBalance);
        sessionRegistry.saveSession(nickname, session);
        notifyPlayerRegistered(session, nickname);
    }

    private String getNickname(String sessionId) {
        return "Player_%s".formatted(sessionId);
    }

    private void notifyPlayerRegistered(WebSocketSession session, String nickname) {
        String message = "Created new user with nickname: %s".formatted(nickname);
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException exception) {
            LOG.error("Failed to notify player, session: {}", session.getId());
        }
    }
}
