package com.live.guessnumber.integration;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;


@MessagingGateway
public interface WinStatisticsGateway {
    @Gateway(requestChannel = "winsStatisticsChannel")
    void process(@Payload Map<String, Long> winStatistics);
}
