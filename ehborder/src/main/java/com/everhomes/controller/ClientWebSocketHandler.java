// @formatter:off
package com.everhomes.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.RestResponse;
import com.everhomes.rpc.HeartbeatPdu;
import com.everhomes.rpc.PduFrame;
import com.everhomes.rpc.client.RegisterConnectionRequestPdu;
import com.everhomes.rpc.client.StoredMessageIndicationPdu;
import com.everhomes.rpc.server.ClientForwardPdu;
import com.everhomes.user.AppIdStatusCommand;
import com.everhomes.user.AppIdStatusResponse;
import com.everhomes.user.AppIdStatusRestResponse;
import com.everhomes.user.RegistedOkResponse;
import com.everhomes.util.DateHelper;
import com.everhomes.util.JsonAccessor;
import com.everhomes.util.NamedHandler;
import com.everhomes.util.NamedHandlerDispatcher;
import com.everhomes.util.SignatureHelper;
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
    
    @Value("${border.app.key}")
    private String appKey;
    
    @Value("${border.app.secret}")
    private String secretKey;
    
    @Value("${heartbeat.interval}")
    private long heartbeatInterval;
    private Map<WebSocketSession, SessionStats> sessionStatsMap = new ConcurrentHashMap<>();
    
    private Map<String, WebSocketSession> tokenToSessionMap = new HashMap<>();
    private Map<WebSocketSession, String> sessionToTokenMap = new HashMap<>(); 
    
    public ClientWebSocketHandler() {
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        //LOGGER.info("Received client message. session: " + session.getId() + ", message: " + message.getPayload());
        updateSessionReceiveTick(session);

        PduFrame frame = PduFrame.fromJson((String)message.getPayload());
        if(frame == null) {
            LOGGER.error("Unrecognized client message: " + message.getPayload());
            return;
        }
        
        if(frame.getName() == null || frame.getName().isEmpty()) {
            LOGGER.error("Missing name in frame: " + message.getPayload());
            return;
        }
        
        NamedHandlerDispatcher.invokeHandler(this, frame.getName(), session, frame);
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        LOGGER.info("Connection establed. session: " + session.getId());
        this.sessionStatsMap.put(session, new SessionStats());
    }

    @Override
    public void handleTransportError(WebSocketSession session,
        Throwable exception) throws Exception {
        unregisterSession(session);
        this.sessionStatsMap.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        LOGGER.info("Connection closed. session: " + session.getId());
        unregisterSession(session);
        this.sessionStatsMap.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    public void forward(ClientForwardPdu pdu) {
       String token = pdu.getLoginToken();
       
       WebSocketSession session = null;
       synchronized(this) {
           session = this.tokenToSessionMap.get(token);
       }
       
       if(session != null) {
           TextMessage msg = new TextMessage(pdu.getEncodedFrame());
           try {
               synchronized(session) {
                   session.sendMessage(msg);
               }
               this.updateSessionSendTick(session);
           } catch(IOException e) {
               LOGGER.warn("Unable to send message " + pdu.getEncodedFrame() + " to client", e);
           }
       } else {
           LOGGER.warn("Login: " + token + " no longer exists here");
       }
    }
    
    @Scheduled(fixedDelay=5000)
    private void heartbeat() {
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
                    LOGGER.warn("Unable to send imessage to " + session.getRemoteAddress().toString(), e);
                }
            }
        });
    }
    
    @NamedHandler(value="", byClass=RegisterConnectionRequestPdu.class)
    private void handleRegisterConnectionRequestPdu(final WebSocketSession session, PduFrame frame) {
        final RegisterConnectionRequestPdu cmd = frame.getPayload(RegisterConnectionRequestPdu.class);
        
        LOGGER.info("Handle register connection request. login key: " + cmd.getLoginToken());
        Map<String, String> params = new HashMap<>();
        params.put("borderId", String.valueOf(this.borderId));
        params.put("loginToken", cmd.getLoginToken());
        
        restCall("/admin/registerLogin", params, new ListenableFutureCallback<ResponseEntity<String>> () {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                LOGGER.info("REST call /admin/registerLogin result: " + result.getBody());
                JsonParser parser = new JsonParser();
                JsonElement root = parser.parse(result.getBody());
                if(root != null) {
                    JsonAccessor accessor = new JsonAccessor(root);
                    int id = accessor.getAsInt("response.loginBorderId");
                    if(borderId == id) {
                        registerSession(cmd.getLoginToken(), session);
                            }
                    
                    PduFrame pdu = new PduFrame();
                    RegistedOkResponse respPdu = new RegistedOkResponse();
                    pdu.setPayload(respPdu);
                    
                    try {
                        synchronized(session) {
                            session.sendMessage(new TextMessage(pdu.toJson()));
                        }
                    } catch (IOException e) {
                        LOGGER.error("Send registedOk message error");
                    }
                    
                } else {
                    LOGGER.error("Invalid REST call response");
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Failed to make REST call /admin/registerLogin. exception: " + ex);
            }
        });
    }
    
    @NamedHandler(value="", byClass=HeartbeatPdu.class)
    private void handleHeartbeatPdu(WebSocketSession session, PduFrame frame) {
        HeartbeatPdu pdu = frame.getPayload(HeartbeatPdu.class);
        //if(LOGGER.isDebugEnabled())
        //    LOGGER.debug(String.format("Received heartbeat from client (%s), last receive tick on client is %d", 
        //        session.getRemoteAddress().toString(), pdu.getLastPeerReceiveTime()));
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
            LOGGER.info("invalid Session: " + session.getId());
            return;
            }
        params.put("token", token);
        
        restCall("/user/appIdStatus", params, new ListenableFutureCallback<ResponseEntity<String>> () {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                LOGGER.info("REST call /user/appIdStatus result: " + result.getBody());
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
                    try {
                        synchronized(session) {
                            session.sendMessage(new TextMessage(pdu.toJson()));
                        }
                    } catch (IOException e) {
                        LOGGER.error("Session send error, appId= " + appId.toString() + ", "  + e.getMessage());
                    }    
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("Failed to make REST call /user/appIdStatus. exception: " + ex);
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
            
            restCall("/admin/unregisterLogin", params, new ListenableFutureCallback<ResponseEntity<String>> () {
                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    LOGGER.info("REST call /admin/unregisterLogin result: " + result.getBody());
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.info("Failed to make REST call /admin/unregisterLogin");
                }
            });
        }
    }
    
    private void restCall(String cmd, Map<String, String> params, ListenableFutureCallback<ResponseEntity<String>> responseCallback) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        params.put("appkey", this.appKey);
        String signature = SignatureHelper.computeSignature(params, this.secretKey);
        params.put("signature", signature);
        
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        for(Map.Entry<String, String> entry: params.entrySet()) {
            paramMap.add(entry.getKey(), entry.getValue());
        }
        
        HttpEntity<MultiValueMap<String,String>> requestEntity= new HttpEntity<MultiValueMap<String,String>>(paramMap,headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        future.addCallback(responseCallback);
    }
    
    private String getRestUri(String relativeUri) {
        StringBuffer sb = new StringBuffer(this.coreServiceUri);
        if(!this.coreServiceUri.endsWith("/"))
            sb.append("/");
        
        if(relativeUri.startsWith("/"))
            sb.append(relativeUri.substring(1));
        else
            sb.append(relativeUri);
        
        return sb.toString();
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
}
