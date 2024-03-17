package com.live.guessnumber.service.websocket.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;


@AllArgsConstructor
@Getter
public class Request {
    private final RequestType requestType;

    private final WebSocketSession session;

    private final RequestPayload payload;
}


