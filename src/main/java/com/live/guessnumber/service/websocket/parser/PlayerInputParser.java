package com.live.guessnumber.service.websocket.parser;

import com.live.guessnumber.exceptions.RequestParseException;
import com.live.guessnumber.service.websocket.requests.RequestPayload;


public interface PlayerInputParser {
    RequestPayload parsePayload(String payload) throws RequestParseException;
}
