package com.live.guessnumber.configuration;

import com.live.guessnumber.service.websocket.WebSocketBetHandlerImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@Import({WebSocketBetHandlerImpl.class})
@ComponentScan(value = "com.live.guessnumber.service.websocket")
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketBetHandlerImpl webSocketBetHandler;

    public WebSocketConfiguration(WebSocketBetHandlerImpl webSocketBetHandler) {
        this.webSocketBetHandler = webSocketBetHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketBetHandler, webSocketBetHandler.getPath()).setAllowedOrigins("*");
    }
}
