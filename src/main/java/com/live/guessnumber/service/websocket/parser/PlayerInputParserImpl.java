package com.live.guessnumber.service.websocket.parser;

import com.live.guessnumber.exceptions.RequestParseException;
import com.live.guessnumber.service.websocket.requests.PlaceBetRequestPayload;
import com.live.guessnumber.service.websocket.requests.RequestPayload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class PlayerInputParserImpl implements PlayerInputParser {

    private final static String DELIMITER = ":";

    private final static int ROUND_IDX = 0;

    private final static int STAKE_IDX = 1;

    private final static int GUESS_IDX = 2;

    @Override
    public RequestPayload parsePayload(String payload) throws RequestParseException {
        try {
            String[] payloadArr = parseRawMessage(payload);
            long roundId = Long.parseLong(payloadArr[ROUND_IDX]);
            BigDecimal stake = BigDecimal.valueOf(Integer.parseInt(payloadArr[STAKE_IDX]));
            int guess = Integer.parseInt(payloadArr[GUESS_IDX]);
            return new PlaceBetRequestPayload(roundId, stake, guess);
        } catch (Exception ex) {
            throw new RequestParseException("Failed to parse request payload");
        }
    }

    private String[] parseRawMessage(String message) {
        return message.replaceAll("[\\r\\n]+", "").split(DELIMITER);
    }
}
