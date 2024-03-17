package com.live.guessnumber.service.websocket;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public abstract class WebSocketBetHandler extends TextWebSocketHandler {

    abstract public String getPath();

    abstract protected void handlePlayerCommand(WebSocketSession session, WebSocketMessage<?> message);

    abstract protected void handleStopGame(WebSocketSession session);

    abstract protected void handleStartGame(WebSocketSession session);
}
