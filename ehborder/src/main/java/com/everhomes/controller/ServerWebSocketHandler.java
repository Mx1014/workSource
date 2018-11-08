// @formatter:off
package com.everhomes.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.rpc.HeartbeatPdu;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.rest.rpc.server.ClientForwardPdu;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PingRequestPdu;
import com.everhomes.rest.rpc.server.PingResponsePdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;
import com.everhomes.util.DateHelper;
import com.everhomes.util.NamedHandler;
import com.everhomes.util.NamedHandlerDispatcher;

/**
 * Web socket handler to handle messages from inter-server communications
 * 
 * @author Kelven Yang
 *
 */
public class ServerWebSocketHandler implements WebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    @Autowired
    private ClientWebSocketHandler clientWebSocketHandler;
    
    @Autowired
    private PusherWebSocketHandler pusherWebSocketHandler;
    
    @Autowired
    private AclinkWebSocketHandler aclinkWebSocketHandler;
    
    @Value("${heartbeat.interval}")
    private long heartbeatInterval;
    private Map<WebSocketSession, SessionStats> sessionStatsMap = new ConcurrentHashMap<>();
    
    @Override
    public void handleMessage(WebSocketSession session,
            WebSocketMessage<?> message) throws Exception {
        LOGGER.info("Received inter-server message. session: " + session.getId() + ", message: " + message.getPayload());
        updateSessionReceiveTick(session);
        
        PduFrame frame = PduFrame.fromJson((String)message.getPayload());
        if(frame == null) {
            LOGGER.error("Unrecognized inter-server message: " + message.getPayload());
            return;
        }
        
        if(frame.getName() == null || frame.getName().isEmpty()) {
            LOGGER.error("Missing name in frame: " + message.getPayload());
            return;
        }
        
        NamedHandlerDispatcher.invokeHandler(this, frame.getName(), session, frame);
    }
    
    private void updateSessionSendTick(WebSocketSession session) {
        SessionStats stats = this.sessionStatsMap.get(session);
        if(stats != null)
            stats.updateSendTick();
    }
    
    private void updateSessionReceiveTick(WebSocketSession session) {
        SessionStats stats = this.sessionStatsMap.get(session);
        if(stats != null)
            stats.updatePeerReceiveTick();
    }
    
    @NamedHandler(value="", byClass=PingRequestPdu.class)
    private void handlePingRequestPdu(WebSocketSession session, PduFrame frame) {
        PingRequestPdu request = frame.getPayload(PingRequestPdu.class);
        PingResponsePdu response = new PingResponsePdu(request);
        
        PduFrame responseFrame = new PduFrame(); 
        responseFrame.setPayload(response);
        responseFrame.setRequestId(frame.getRequestId());
        TextMessage msg = new TextMessage(responseFrame.toJson());
        try {
            synchronized(session) {
                session.sendMessage(msg);
            }
            updateSessionSendTick(session);
        } catch(IOException e) {
            LOGGER.warn("Unable to send inter-server message to " + session.getRemoteAddress().toString(), e);
        }
    }
    
    @NamedHandler(byClass=ClientForwardPdu.class)
    private void handleClientForwardPdu(WebSocketSession session, PduFrame frame) {
        ClientForwardPdu pdu = frame.getPayload(ClientForwardPdu.class);
        this.clientWebSocketHandler.forward(pdu);
    }
    
    @NamedHandler(byClass=HeartbeatPdu.class)
    private void handleHeartbeatPdu(WebSocketSession session, PduFrame frame) {
        HeartbeatPdu pdu = frame.getPayload(HeartbeatPdu.class);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("Received heartbeat from core server %s, last receive tick on border is %d", 
                session.getRemoteAddress().toString(), pdu.getLastPeerReceiveTime()));
    }
    
    @NamedHandler(byClass=PusherNotifyPdu.class)
    private void handlePusherNotifyPdu(WebSocketSession session, PduFrame frame) {
        PusherNotifyPdu pdu = frame.getPayload(PusherNotifyPdu.class);
        pusherWebSocketHandler.notify(session, pdu);
    }
    
    @NamedHandler(byClass=AclinkRemotePdu.class)
    private void handleAclinkRemotePdu(WebSocketSession session, PduFrame frame) {
        AclinkRemotePdu pdu = frame.getPayload(AclinkRemotePdu.class);
        aclinkWebSocketHandler.aclinkRemote(session, pdu);
    }
    
    @NamedHandler(byClass=DeviceRequestPdu.class)
    private void handleDeviceRequestPdu(WebSocketSession session, PduFrame frame) {
        DeviceRequestPdu pdu = frame.getPayload(DeviceRequestPdu.class);
        pdu = this.pusherWebSocketHandler.getDeviceInfo(pdu);
        
        PduFrame responseFrame = new PduFrame(); 
        responseFrame.setPayload(pdu);
        responseFrame.setRequestId(frame.getRequestId());
        TextMessage msg = new TextMessage(responseFrame.toJson());
        try {
            synchronized(session) {
                session.sendMessage(msg);
            }
            updateSessionSendTick(session);
        } catch(IOException e) {
            LOGGER.warn("Unable to send inter-server message to " + session.getRemoteAddress().toString(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Connection establed. session: " + session.getId());
        
        this.sessionStatsMap.put(session, new SessionStats());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){  
            session.close();  
        }
        this.sessionStatsMap.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        LOGGER.info("Connection closed. session: " + session.getId());
        this.sessionStatsMap.remove(session);
        
        this.clientWebSocketHandler.closeClientSessions();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    @Scheduled(fixedDelay=5000)
    public void heartbeat() {
        this.sessionStatsMap.forEach((WebSocketSession session, SessionStats stats) -> {
            long currentTick = DateHelper.currentGMTTime().getTime();
            if(currentTick - stats.getLastSendTick() > heartbeatInterval) {
                HeartbeatPdu pdu = new HeartbeatPdu();
                pdu.setLastPeerReceiveTime(stats.getLastPeerReceiveTick());
                
                PduFrame frame = new PduFrame();
                frame.setPayload(pdu);
                TextMessage msg = new TextMessage(frame.toJson());
                try {
                    synchronized(session) {
                        session.sendMessage(msg);
                    }
                    updateSessionSendTick(session);
                } catch(IOException e) {
                    LOGGER.warn("Unable to send inter-server message to " + session.getRemoteAddress().toString(), e);
                }
            }
        });
    }
}
