package com.live.guessnumber.service.websocket;

import com.live.guessnumber.exceptions.RequestParseException;
import com.live.guessnumber.service.websocket.parser.PlayerInputParser;
import com.live.guessnumber.service.websocket.requests.Request;
import com.live.guessnumber.service.websocket.requests.RequestPayload;
import com.live.guessnumber.service.websocket.requests.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Service
public class WebSocketBetHandlerImpl extends WebSocketBetHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketBetHandlerImpl.class);

    private static final String PATH = "/game";

    private final PlayerInputParser playerInputParser;

    private final PlayerRequestDispatcher playerRequestDispatcher;

    public WebSocketBetHandlerImpl(PlayerInputParser playerInputParser, PlayerRequestDispatcher playerRequestDispatcher) {
        this.playerInputParser = playerInputParser;
        this.playerRequestDispatcher = playerRequestDispatcher;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        handlePlayerCommand(session, message);
    }

    public void afterConnectionEstablished(WebSocketSession session) {
        handleStartGame(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        handleStopGame(session);
    }

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    protected void handlePlayerCommand(WebSocketSession session, WebSocketMessage<?> message) {
        String messagePayload = (String) message.getPayload();
        try {
            RequestPayload payload = playerInputParser.parsePayload(messagePayload);
            Request request = new Request(RequestType.PLACE_BET, session, payload);
            playerRequestDispatcher.routeRequest(request);
        } catch (RequestParseException exception) {
            LOG.error("Failed to parse player request, reason: {}", exception.getMessage());
            notifyRequestRejected(session);
        }
    }

    @Override
    protected void handleStopGame(WebSocketSession session) {
        Request request = new Request(RequestType.STOP_GAME, session, null);
        playerRequestDispatcher.routeRequest(request);
    }

    @Override
    protected void handleStartGame(WebSocketSession session) {
        Request request = new Request(RequestType.START_GAME, session, null);
        playerRequestDispatcher.routeRequest(request);
    }

    private void notifyRequestRejected(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage("Command is not recognized, try again"));
        } catch (IOException exception) {
            LOG.error("Failed notify player with a command error, session: {}", session.getId());
        }
    }
}
