package com.live.guessnumber.service.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;


public interface SessionRegistry {

    void saveSession(String nickname, WebSocketSession session);

    void dismissSession(WebSocketSession session);

    List<WebSocketSession> getAllSessions();

    WebSocketSession getSessionByNickname(String nickname);

    String getNicknameBySession(WebSocketSession socketSession);

    boolean isPlayerOnline(String nickname);
}
