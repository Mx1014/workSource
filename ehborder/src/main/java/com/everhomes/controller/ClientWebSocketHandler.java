// @formatter:off
package com.everhomes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.everhomes.rest.message.MessageRecordSenderTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.rpc.HeartbeatPdu;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.rest.rpc.client.RegisterConnectionRequestPdu;
import com.everhomes.rest.rpc.client.StoredMessageIndicationPdu;
import com.everhomes.rest.rpc.server.ClientForwardPdu;
import com.everhomes.rest.user.AppIdStatusCommand;
import com.everhomes.rest.user.AppIdStatusRestResponse;
import com.everhomes.rest.user.RegistedOkResponse;
import com.everhomes.util.DateHelper;
import com.everhomes.util.JsonAccessor;
import com.everhomes.util.NamedHandler;
import com.everhomes.util.NamedHandlerDispatcher;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * Web socket handler to handle message from native clients
 * 
 * @author Kelven Yang
 *
 */
public class ClientWebSocketHandler implements WebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientWebSocketHandler.class);
    
    @Value("${border.id}")
    private int borderId;
    
    @Value("${core.service.uri}")
    private String coreServiceUri;
    
    @Value("${heartbeat.interval}")
    private long heartbeatInterval;
    private Map<WebSocketSession, SessionStats> sessionStatsMap = new ConcurrentHashMap<>();
    
    private Map<String, WebSocketSession> tokenToSessionMap = new HashMap<>();
    private Map<WebSocketSession, String> sessionToTokenMap = new HashMap<>(); 
    
    @Autowired
    private HttpRestCallProvider httpRestCallProvider;
    
    public ClientWebSocketHandler() {
    }
    
    private void tearDownSession(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException ex) {
            LOGGER.warn("Exception when closing client connection");
        }
        
        LOGGER.info("Close connection due to unavailability of core server. session: " + session.getId());

        unregisterSession(session);
        sessionStatsMap.remove(session);                
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        updateSessionReceiveTick(session);
        
        if (message instanceof TextMessage) {
            //LOGGER.info("Received client message. session= " + session.getId() + ", message: " + message.getPayload());

            try {
                if("Ping".equalsIgnoreCase((String)message.getPayload())) {
                    return;
                }
                PduFrame frame = PduFrame.fromJson((String)message.getPayload());
                if(frame == null) {
                    LOGGER.error("Unrecognized client message, session=" + session.getId() + ", payload=" + message.getPayload());
                    return;
                }
                
                if(frame.getName() == null || frame.getName().isEmpty()) {
                    LOGGER.error("Missing name in frame, session=" + session.getId() + ", payload=" + message.getPayload());
                    return;
                }
                
                NamedHandlerDispatcher.invokeHandler(this, frame.getName(), session, frame);    
            } catch(Exception ex) {
                LOGGER.error("unknown error", ex);
            }
            
        }
        else if (message instanceof PongMessage) {
            handlePongMessage(session, (PongMessage) message);
        } else if (message instanceof PingMessage) {
            LOGGER.info("Got ping message from " + session.getId());
            PongMessage msg = new PongMessage();
            try {
                synchronized(session) {
                    session.sendMessage(msg);
                }
                updateSessionSendTick(session);
            } catch(IOException e) {
                LOGGER.warn("Unable to send imessage to " + session.getRemoteAddress().toString(), e);
            }
        } else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        LOGGER.info("Connection establed, session=" + session.getId());
        this.sessionStatsMap.put(session, new SessionStats());
    }

    @Override
    public void handleTransportError(WebSocketSession session,
        Throwable exception) throws Exception {
        if(session.isOpen()){  
            session.close();  
        }  
        unregisterSession(session);
        this.sessionStatsMap.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        LOGGER.info("Connection closed, session=" + session.getId());
        unregisterSession(session);
        this.sessionStatsMap.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    public void closeClientSessions() {
        List<WebSocketSession> sessions = new ArrayList<>();
        synchronized(this) {
            for(WebSocketSession session: sessionStatsMap.keySet()) {
                sessions.add(session);
            }
            sessionStatsMap.clear();
            tokenToSessionMap.clear();
            sessionToTokenMap.clear();
        }
        
        for(WebSocketSession session: sessions) {
            synchronized(session) {
                try {
                    session.close();
                } catch(IOException e) {
                    LOGGER.warn("Exception when closing client web socket, will go ahead to close anyway", e);
                }
            }
        }
        
        sessions.clear();
    }    
    
    public void forward(ClientForwardPdu pdu) {
       String token = pdu.getLoginToken();
       
       WebSocketSession session = null;
       synchronized(this) {
           session = this.tokenToSessionMap.get(token);
       }

       String frameJson = pdu.getEncodedFrame();
       if(session != null) {
           TextMessage msg = new TextMessage(frameJson);
//           try {
//               synchronized(session) {
//                   session.sendMessage(msg);
//               }
//               if(LOGGER.isDebugEnabled()) {
//                   LOGGER.debug("Forward message, session=" + session.getId() + ", token=" + token + ", json=" + frameJson);
//               }
//               this.updateSessionSendTick(session);
//           } catch(IOException e) {
//               LOGGER.warn("Unable to send message to client, session=" + session.getId() + ", json=" + frameJson, e);
//           }
           WebSocketSessionProxy.sendMessage(session,msg, MessageRecordSenderTag.FORWARD_EVENT.getCode(), sessionToTokenMap.get(session));
           this.updateSessionSendTick(session);
       } else {
           LOGGER.warn("Session is null, loginToken=" + token + ", json=" + frameJson);
       }
    }
    
    public Map<String, Object> getOnlineTokens() {
        Map<String, Object> map = new HashMap<String, Object>();
        this.tokenToSessionMap.forEach((String token, WebSocketSession session) -> {
            map.put(token, session.getId());
        });
        
        return map;
    }
    
//    @Scheduled(fixedDelay=5000)
//    private void heartbeat() {
//        this.sessionStatsMap.forEach((WebSocketSession session, SessionStats stats) -> {
//            long currentTick = DateHelper.currentGMTTime().getTime();
//            
//            if(currentTick - stats.getLastSendTick() > heartbeatInterval) {
//                HeartbeatPdu pdu = new HeartbeatPdu();
//                pdu.setLastPeerReceiveTime(stats.getLastPeerReceiveTick());
//                
//                PduFrame frame = new PduFrame();
//                frame.setPayload(pdu);
//                TextMessage msg = new TextMessage(frame.toJson());
//                try {
//                    synchronized(session) {
//                        session.sendMessage(msg);
//                    }
//                    updateSessionSendTick(session);
//                } catch(IOException e) {
//                    LOGGER.warn("Unable to send imessage to " + session.getRemoteAddress().toString(), e);
//                }
//            }
//        });
//    }
    
    @Scheduled(fixedDelay=5000)
    private void heartbeat() {
        
        List<WebSocketSession> timeouts = new ArrayList<WebSocketSession>();
        
        this.sessionStatsMap.forEach((WebSocketSession session, SessionStats stats) -> {
            long currentTick = DateHelper.currentGMTTime().getTime();
            if(currentTick - stats.getLastPeerReceiveTick() > 2*heartbeatInterval) {
                timeouts.add(session);
                return;
            }
            
            PingMessage msg = new PingMessage();
            
            if(currentTick - stats.getLastSendTick() > heartbeatInterval) {
                try {
                    synchronized(session) {
                        LOGGER.info("Border server ping message to " + session.getId());
                        session.sendMessage(msg);
                    }
                    updateSessionSendTick(session);
                } catch(IOException e) {
                    LOGGER.warn("Unable to send imessage to " + session.getRemoteAddress().toString(), e);
                }
            }
        });
        
        for(WebSocketSession session : timeouts) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("timeout session id=" + session.getId());
            }
            unregisterSession(session);
            this.sessionStatsMap.remove(session);
            try {
                session.close();
            } catch (IOException e) {
                LOGGER.warn("close session error", e);
            }
        }
    }
    
    @NamedHandler(value="", byClass=RegisterConnectionRequestPdu.class)
    private void handleRegisterConnectionRequestPdu(final WebSocketSession session, PduFrame frame) {
        final RegisterConnectionRequestPdu cmd = frame.getPayload(RegisterConnectionRequestPdu.class);
        
        /* 
         * 此处也可以不要，旧的session存在也没有关系，只要旧的session退出，不映像最新的session就没有问题。
         * 并且 registerLogin 已经处理相关的信息。
         */
        
        WebSocketSession oldSession = null;
        synchronized(this) {
            oldSession = this.tokenToSessionMap.get(cmd.getLoginToken());
        }
        if(oldSession != null && oldSession != session) {
            LOGGER.info("tearDown old sesssion, id=" + session.getId());
            tearDownSession(oldSession);
        }
        
        LOGGER.info("Handle register connection request, session=" + session.getId() + ", loginToken=" + cmd.getLoginToken());
        Map<String, String> params = new HashMap<>();
        params.put("borderId", String.valueOf(this.borderId));
        params.put("loginToken", cmd.getLoginToken());
        params.put("borderSessionId", session.getId());
        
        httpRestCallProvider.restCall("/admin/registerLogin", params, new ListenableFutureCallback<ResponseEntity<String>> () {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                LOGGER.info("REST call /admin/registerLogin, session=" + session.getId() + ", result=" + result.getBody());
                JsonParser parser = new JsonParser();
                JsonElement root = parser.parse(result.getBody());
                boolean tearDown = true;
                if(root != null) {
                    JsonAccessor accessor = new JsonAccessor(root);
                    int id = accessor.getAsInt("response.loginBorderId");
                    if(borderId == id) {
                        registerSession(cmd.getLoginToken(), session);
                    }

                    PduFrame pdu = new PduFrame();
                    RegistedOkResponse respPdu = new RegistedOkResponse();
                    pdu.setPayload(respPdu);

                    WebSocketSessionProxy.sendMessage(session, new TextMessage(pdu.toJson()), MessageRecordSenderTag.REGISTER_LOGIN.getCode(), sessionToTokenMap.get(session));
                    tearDown = false;
                    
//                    try {
//                        synchronized(session) {
//                            session.sendMessage(new TextMessage(pdu.toJson()));
//                            tearDown = false;
//                        }
//                    } catch (IOException e) {
//                        LOGGER.error("Send registedOk message error, session=" + session.getId());
//                    }
                    
                } else {
                    LOGGER.error("Invalid REST call response, session=" + session.getId());
                }
                
                if(tearDown) 
                    tearDownSession(session);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Failed to make REST call /admin/registerLogin, session=" + session.getId(), ex);

                tearDownSession(session);
            }
        });
    }
    
    @NamedHandler(value="", byClass=HeartbeatPdu.class)
    private void handleHeartbeatPdu(WebSocketSession session, PduFrame frame) {
//        HeartbeatPdu pdu = frame.getPayload(HeartbeatPdu.class);
//        if(LOGGER.isDebugEnabled())
//            LOGGER.debug(String.format("Received heartbeat from client (%s), last receive tick on client is %d", 
//                session.getRemoteAddress().toString(), pdu.getLastPeerReceiveTime()));
    }
    
    @NamedHandler(value="", byClass=AppIdStatusCommand.class)
    private void handleAppIdStatusCommandPdu(WebSocketSession session, PduFrame frame) {
        final AppIdStatusCommand cmd = frame.getPayload(AppIdStatusCommand.class);
        Map<String, String> params = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, params);
      
        String token = null;
        synchronized(this) {
            token = sessionToTokenMap.get(session);
        }
        if(token == null || token.isEmpty()) {
            LOGGER.info("Invalid token, session=" + session.getId() + ", cmd=" + cmd);
            return;
            }
        params.put("token", token);
        
        httpRestCallProvider.restCall("/user/appIdStatus", params, new ListenableFutureCallback<ResponseEntity<String>> () {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                LOGGER.info("REST call /user/appIdStatus, session=" + session.getId() + ", result=" + result.getBody());
                Gson gson = new Gson();
                AppIdStatusRestResponse resp = gson.fromJson(result.getBody(), AppIdStatusRestResponse.class);

                //Object respObj = StringHelper.fromJsonString(result.getBody(), AppIdStatusRestResponse.class);
                //if(respObj == null) {
                //    LOGGER.error("error for /user/appIdStatus");
                //    return;
                    //}
                //AppIdStatusRestResponse resp = (AppIdStatusRestResponse) respObj;
                for(Long appId : resp.getResponse().getAppIds()) {
                    PduFrame pdu = new PduFrame();
                    StoredMessageIndicationPdu clientPdu = new StoredMessageIndicationPdu();
                    pdu.setPayload(clientPdu);
                    pdu.setAppId(appId);
                    WebSocketSessionProxy.sendMessage(session, new TextMessage(pdu.toJson()), MessageRecordSenderTag.APPIDSTATUS.getCode(), sessionToTokenMap.get(session));

//                    try {
//                        synchronized(session) {
//                            session.sendMessage(new TextMessage(pdu.toJson()));
//                        }
//                    } catch (IOException e) {
//                        LOGGER.error("Session send error, session=" + session.getId() + ", appId= " + appId.toString(), e);
//                        tearDownSession(session);
//                    }
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Failed to make REST call /user/appIdStatus, session=" + session.getId(), ex);
                tearDownSession(session);
            }
        });
    }
    
    private void registerSession(String loginToken, WebSocketSession session) {
        synchronized(this) {
            tokenToSessionMap.put(loginToken, session);
            sessionToTokenMap.put(session, loginToken);
        }
    }
    
    private void unregisterSession(WebSocketSession session) {
        String token = null;
        synchronized(this) {
            token = sessionToTokenMap.get(session);
            sessionToTokenMap.remove(session);
            if(token != null) {
                tokenToSessionMap.remove(token);
            }
        }
        
        if(token != null) {
            Map<String, String> params = new HashMap<>();
            params.put("borderId", String.valueOf(this.borderId));
            params.put("loginToken", token);
            params.put("borderSessionId", session.getId());
            
            httpRestCallProvider.restCall("/admin/unregisterLogin", params, new ListenableFutureCallback<ResponseEntity<String>> () {
                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    LOGGER.info("REST call /admin/unregisterLogin, session=" + session.getId() + ", result=" + result.getBody());
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.info("Failed to make REST call /admin/unregisterLogin, session=" + session.getId(), ex);
                }
            });
        }
    }
    
    private void updateSessionSendTick(WebSocketSession session) {
        SessionStats stats = this.sessionStatsMap.get(session);
        if(stats != null) {
            stats.updateSendTick();
            stats.updatePeerReceiveTick();//tick here, fix for pong timeout error   
        }
            
    }
    
    private void updateSessionReceiveTick(WebSocketSession session) {
        SessionStats stats = this.sessionStatsMap.get(session);
        if(stats != null) {
            if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("update tick, session=" + session.getId());    
            }
            
            stats.updatePeerReceiveTick();   
        }
    }
    
    private void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LOGGER.info("Got pong message from " + session.getId());
    }

}
