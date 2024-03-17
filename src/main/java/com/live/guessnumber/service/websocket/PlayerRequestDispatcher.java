package com.live.guessnumber.service.websocket;

import com.live.guessnumber.service.websocket.requests.Request;

public interface PlayerRequestDispatcher {
    void routeRequest(Request request);
}
