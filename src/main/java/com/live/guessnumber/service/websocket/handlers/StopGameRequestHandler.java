package com.live.guessnumber.service.websocket.handlers;

import com.live.guessnumber.service.websocket.SessionRegistry;
import com.live.guessnumber.service.websocket.requests.Request;
import com.live.guessnumber.service.websocket.requests.RequestType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service(RequestType.STOP_GAME_)
@AllArgsConstructor
public class StopGameRequestHandler implements RequestHandler {

    private final SessionRegistry sessionRegistry;

    @Override
    public void handle(Request request) {
        sessionRegistry.dismissSession(request.getSession());
    }
}
