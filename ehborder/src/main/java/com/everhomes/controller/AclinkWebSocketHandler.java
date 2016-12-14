package com.everhomes.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.DataUtil;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.SyncWebsocketMessagesRestResponse;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.util.StringHelper;

public class AclinkWebSocketHandler extends BinaryWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkWebSocketHandler.class);
    
    @Autowired
    private HttpRestCallProvider httpRestCallProvider;
    
    private ConcurrentHashMap<String, AclinkSessionInfo> uuid2Session = new ConcurrentHashMap<>();
    private ConcurrentHashMap<WebSocketSession, AclinkWebSocketState> session2State = new ConcurrentHashMap<>();

    public boolean checkAndSet(String uuid, DoorAccessDTO dto) {
        AclinkSessionInfo info = new AclinkSessionInfo();
        info.setDto(dto);
        AclinkSessionInfo rlt = uuid2Session.putIfAbsent(uuid, info);
        if(rlt != null) {
            return false;
        }
        
        return true;
    }
    
    private String uuidFromSession(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] vs = path.split("/");
        if(vs.length <= 2) {
            return "";
        }
        return vs[2];
    }
    
    public void sendPing(WebSocketSession session) {
        PingMessage pingMessage = new PingMessage();
        try {
            synchronized(session) {
                session.sendMessage(pingMessage);    
                }
            
        } catch (IOException e) {
            LOGGER.info("send ping error", e);
        }
    }
    
    public void aclinkRemote(WebSocketSession serverSession, AclinkRemotePdu pdu) {
        if(null == pdu) {
            LOGGER.error("aclinkRemote pdu is null");
            return;
        }
        
        AclinkSessionInfo aclinkSession = uuid2Session.get(pdu.getUuid());
        if(aclinkSession == null) {
            LOGGER.error("aclink session is null uuid=" + pdu.getUuid());
            return;
        }
        
        WebSocketSession session = aclinkSession.getSession();
        if(session == null) {
            LOGGER.error("session is null uuid=" + pdu.getUuid());
            return;
        }
        
        try {
            synchronized(session) {
                BinaryMessage wsMessage = new BinaryMessage(Base64.decodeBase64(pdu.getBody()));
                session.sendMessage(wsMessage);
                LOGGER.info("sendBody to uuid=" + pdu.getUuid());
            }
        } catch (IOException e) {
            LOGGER.error("sendMessage error", e);
            }
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uuid = uuidFromSession(session);
        
        LOGGER.info("Aclink connecting: uuid= " + uuid);
        
        AclinkSessionInfo sInfo = uuid2Session.get(uuid);
        if(sInfo == null) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }
        
        AclinkWebSocketState state = new AclinkWebSocketState();
        state.setHardwareId(sInfo.getDto().getHardwareId());
        state.setId(sInfo.getDto().getId());
        state.setUuid(uuid);
        sInfo.setSession(session);
        
        session2State.put(session, state);
        
        //sendPing(session);
        
        LOGGER.info("Aclink connected: id= " + sInfo.getDto().getId() + ", uuid=" + uuid);
    }
    
    private void disConnected(WebSocketSession session, String info) throws Exception {
        String uuid = uuidFromSession(session);
        AclinkWebSocketState state = session2State.remove(session);
        uuid2Session.remove(uuid);
        
        LOGGER.info("Aclink disConnected: uuid= " + uuid + " info=" + info);
        
        if(state != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", state.getId().toString());
            httpRestCallProvider.restCall("/aclink/disConnected", params, new ListenableFutureCallback<ResponseEntity<String>> () {
                @Override
                public void onSuccess(ResponseEntity<String> result) {
                }

                @Override
                public void onFailure(Throwable ex) {
                }
            });
        }
        
        
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        disConnected(session, "normal closed");
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        disConnected(session, "error closed=" + arg1.getMessage());
    }
    
    @Scheduled(fixedDelay=20000, initialDelay=5000)
    public void tickCheck() {
        this.session2State.forEach((WebSocketSession session, AclinkWebSocketState state) -> {
            state.onTick(session, this);
        });
    }
    
    public void nextMessage(AclinkWebSocketMessage cmd, WebSocketSession session, AclinkWebSocketState state) {
        Map<String, String> params = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, params);
        final AclinkWebSocketHandler handler = this;
        
        if(cmd.getPayload() != null) {
            LOGGER.info("Got reply=" + cmd);    
        }
        
        httpRestCallProvider.restCall("/aclink/syncWebsocketMessages", params, new ListenableFutureCallback<ResponseEntity<String>> () {

        @Override
        public void onSuccess(ResponseEntity<String> result) {
            if(result.getStatusCode() == HttpStatus.OK) {
                SyncWebsocketMessagesRestResponse resp = (SyncWebsocketMessagesRestResponse)StringHelper.fromJsonString(result.getBody()
                        , SyncWebsocketMessagesRestResponse.class);
                if(resp.getErrorCode().equals(200) && resp.getResponse() != null) {
                    
                    AclinkWebSocketMessage respCmd = resp.getResponse();
                    state.onServerMessage(resp.getResponse(), session, handler);
                    
                    byte[] bPayload = Base64.decodeBase64(respCmd.getPayload());
                    byte[] bSeq = DataUtil.intToByteArray(respCmd.getSeq().intValue());
                    byte[] bLen = DataUtil.intToByteArray(bPayload.length + 6);
                    byte[] mBuf = new byte[bPayload.length + 10];
                    
                    System.arraycopy(bLen, 0, mBuf, 0, bLen.length);
                    System.arraycopy(bSeq, 0, mBuf, 6, bSeq.length);
                    System.arraycopy(bPayload, 0, mBuf, 10, bPayload.length);
                    
                    BinaryMessage wsMessage = new BinaryMessage(mBuf);
                    try {
                        synchronized(session) {
                            session.sendMessage(wsMessage);    
                        }
                        
                    } catch (IOException e) {
                        LOGGER.error("sendMessage error", e);
                    }
                }
                
            }
            
        }

        @Override
        public void onFailure(Throwable ex) {
            LOGGER.error("call core server error=", ex);
        }
      });
    }
    
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        AclinkWebSocketState state = session2State.get(session);
        if(state == null) {
            try { 
                session.close();
            } catch (IOException e) {
                LOGGER.error("closed error", e);
            }
        }
        
        ByteBuffer buffer = message.getPayload();
        byte[] buf = buffer.array();
        int len = message.getPayloadLength() - 4;
        if(len <= 6) {
            LOGGER.info("Message error, ignored, len=" + len);
            return;
        }
        
        byte[] bDataSize = Arrays.copyOfRange(buf, 0, 4);
        int dataSize = DataUtil.byteArrayToInt(bDataSize);
        if(dataSize != len) {
            LOGGER.info("Message error length, ignored, dataSize=" + dataSize + ", len=" + len);
            return;
        }
        
        state.onMessage(buf, session, this);
    }
    
    
    
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LOGGER.info("Got pong message from " + uuidFromSession(session));
    }
}
