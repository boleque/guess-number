package com.live.guessnumber.service.websocket.handlers;

import com.live.guessnumber.service.websocket.requests.Request;


public interface RequestHandler {
    void handle(Request request);
}
