package com.everhomes.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.everhomes.aclink.AclinkWebSocketMessage;
import com.everhomes.aclink.DataUtil;
import com.everhomes.aclink.DoorAccessDTO;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;

public class AclinkWebSocketHandler extends BinaryWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkWebSocketHandler.class);
    
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
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        String uuid = uuidFromSession(session);
        session2State.remove(session);
        uuid2Session.remove(uuid);
        
        LOGGER.info("Aclink disConnected: uuid= " + uuid);
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        String uuid = uuidFromSession(session);
        session2State.remove(session);
        uuid2Session.remove(uuid);
        
        LOGGER.info("Aclink disConnected by transportError: uuid= " + uuid);
    }
    
    @Scheduled(fixedDelay=30000, initialDelay=60000)
    public void tickCheck() {
        this.session2State.forEach((WebSocketSession session, AclinkWebSocketState state) -> {
            state.onTick(session, this);
        });
    }
    
    public void nextMessage(AclinkWebSocketMessage cmd, WebSocketSession session, AclinkWebSocketState state) {
        Map<String, String> params = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, params);
        httpRestCallProvider.restCall("/aclink/syncWebsocketMessages", params, new ListenableFutureCallback<ResponseEntity<String>> () {

        @Override
        public void onSuccess(ResponseEntity<String> result) {
            //TODO for response
        }

        @Override
        public void onFailure(Throwable ex) {
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
        
    }
}
