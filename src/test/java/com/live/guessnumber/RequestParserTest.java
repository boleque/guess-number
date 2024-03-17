package com.live.guessnumber;

import com.live.guessnumber.exceptions.RequestParseException;
import com.live.guessnumber.service.websocket.parser.PlayerInputParser;
import com.live.guessnumber.service.websocket.parser.PlayerInputParserImpl;
import com.live.guessnumber.service.websocket.requests.PlaceBetRequestPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = {PlayerInputParserImpl.class})
@ExtendWith({SpringExtension.class})
public class RequestParserTest {

    private final static long ROUND_ID = 1;

    private final static long GUESS = 1;

    private final static BigDecimal STAKE = BigDecimal.valueOf(200);

    @Autowired
    private PlayerInputParser playerInputParser;


    @Test
    void shouldCorrectlyParsePlayerBet() {
        final String request = "%s:%s:%s".formatted(ROUND_ID, STAKE, GUESS);
        PlaceBetRequestPayload requestPayload = (PlaceBetRequestPayload) assertDoesNotThrow(
                () -> playerInputParser.parsePayload(request));
        assertThat(requestPayload).isNotNull()
                .matches(rp -> rp.getRoundId() == ROUND_ID)
                .matches(rp -> rp.getStake().compareTo(STAKE) == 0)
                .matches(rp -> rp.getGuess() == GUESS);
    }

    @Test
    void shouldHandleParseError() {
        final String request = "200:3";
        assertThrows(RequestParseException.class, () -> playerInputParser.parsePayload(request));
    }
}
