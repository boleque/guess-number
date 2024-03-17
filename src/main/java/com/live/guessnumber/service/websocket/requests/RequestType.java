package com.live.guessnumber.service.websocket.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum RequestType {

    START_GAME(RequestType.START_GAME_),

    STOP_GAME(RequestType.STOP_GAME_),

    PLACE_BET(RequestType.PLACE_BET_);

    public static final String START_GAME_ = "start_game";

    public static final String STOP_GAME_ = "stop_game";

    public static final String PLACE_BET_ = "place_bet";

    private final String commandName;
}
