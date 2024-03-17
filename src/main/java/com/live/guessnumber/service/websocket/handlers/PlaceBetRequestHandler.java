package com.live.guessnumber.service.websocket.handlers;

import com.live.guessnumber.service.PlaceBetService;
import com.live.guessnumber.service.websocket.SessionRegistry;
import com.live.guessnumber.service.websocket.requests.PlaceBetRequestPayload;
import com.live.guessnumber.service.websocket.requests.Request;
import com.live.guessnumber.service.websocket.requests.RequestType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;


@Service(RequestType.PLACE_BET_)
@AllArgsConstructor
public class PlaceBetRequestHandler implements RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PlaceBetRequestHandler.class);

    private final PlaceBetService betService;

    private final SessionRegistry sessionRegistry;

    @Override
    public void handle(Request request) {
        PlaceBetRequestPayload payload = (PlaceBetRequestPayload) request.getPayload();
        WebSocketSession session = request.getSession();
        String nickname = sessionRegistry.getNicknameBySession(session);
        long roundId = payload.getRoundId();
        BigDecimal stake = payload.getStake();
        int guess = payload.getGuess();

        boolean isBetPlaced = placeBet(nickname, roundId, stake, guess);
        if (!isBetPlaced) {
            notifyBetRejected(request.getSession());
        }
    }

    private boolean placeBet(String nickname, long roundId, BigDecimal stake, int guess) {
        try {
            betService.placeBet(nickname, roundId, stake, guess);
        } catch (Exception ex) {
            LOG.error("Failed to place bet; reason: {}", ex.getMessage());
            return false;
        }
        return true;
    }

    private void notifyBetRejected(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage("Something went wrong, try again"));
        } catch (IOException exception) {
            LOG.error("Failed to sent bet error the player with session: {}", session.getId());
        }
    }
}
