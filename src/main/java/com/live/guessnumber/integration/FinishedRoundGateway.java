package com.live.guessnumber.integration;


import com.live.guessnumber.domains.RoundResult;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;


@MessagingGateway
public interface FinishedRoundGateway {
    @Gateway(requestChannel = "finishedRoundChannel")
    List<RoundResult> process(@Payload long roundId);
}
