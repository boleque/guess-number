package com.live.guessnumber.integration;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;


@MessagingGateway
public interface StartedRoundGateway {
    @Gateway(requestChannel = "startedRoundChannel")
    void process(@Payload long roundId);
}
