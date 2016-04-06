package com.everhomes.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

public class AclinkWebSocketHandler extends BinaryWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkWebSocketHandler.class);
    
    private Map<String, WebSocketSession> uuidToSession = new ConcurrentHashMap<>();
    private Map<WebSocketSession, AclinkWebSocketState> session2State = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("headers is " + session.getHandshakeHeaders());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        
    }
    
    @Scheduled(fixedDelay=20000, initialDelay=20000)
    public void tickCheck() {
        
    }
    
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        
    }
}
