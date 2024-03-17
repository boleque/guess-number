package com.live.guessnumber.service.websocket;

import com.live.guessnumber.service.websocket.handlers.RequestHandler;
import com.live.guessnumber.service.websocket.requests.Request;
import com.live.guessnumber.service.websocket.requests.RequestType;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PlayerRequestDispatcherImpl implements PlayerRequestDispatcher {

    private final Map<String, RequestHandler> commandHandlersMap;

    public PlayerRequestDispatcherImpl(Map<String, RequestHandler> commandHandlersMap) {
        this.commandHandlersMap = commandHandlersMap;
    }

    @Override
    public void routeRequest(Request request) {
        RequestType requestType = request.getRequestType();
        commandHandlersMap.get(requestType.getCommandName()).handle(request);
    }
}
