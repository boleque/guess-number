package com.live.guessnumber.service.websocket.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@AllArgsConstructor
@Getter
public class PlaceBetRequestPayload implements RequestPayload {

    private final long roundId;

    private final BigDecimal stake;

    private final int guess;
}
