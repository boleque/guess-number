package com.live.guessnumber.service.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


@Service
public class SessionRegistryImpl implements SessionRegistry {

    private final ConcurrentMap<WebSocketSession, String> nicknameBySession = new ConcurrentHashMap<>();

    private ConcurrentMap<String, WebSocketSession> sessionByNickname = new ConcurrentHashMap<>();

    @Override
    public void saveSession(String nickname, WebSocketSession session) {
        sessionByNickname.put(nickname, session);
        nicknameBySession.put(session, nickname);
    }

    @Override
    public void dismissSession(WebSocketSession session) {
        nicknameBySession.remove(session);
        sessionByNickname = nicknameBySession
                .entrySet()
                .stream()
                .collect(Collectors.toConcurrentMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    @Override
    public List<WebSocketSession> getAllSessions() {
        return new ArrayList<>(sessionByNickname.values());
    }

    @Override
    public WebSocketSession getSessionByNickname(String nickname) {
        return sessionByNickname.get(nickname);
    }

    @Override
    public String getNicknameBySession(WebSocketSession socketSession) {
        return nicknameBySession.get(socketSession);
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return sessionByNickname.containsKey(nickname);
    }
}
