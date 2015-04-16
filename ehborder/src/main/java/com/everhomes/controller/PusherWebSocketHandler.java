package com.everhomes.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.message.HandshakeMessage;
import com.everhomes.rpc.PduFrame;
import com.everhomes.rpc.server.PusherNotifyPdu;
import com.everhomes.util.SignatureHelper;

public class PusherWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherWebSocketHandler.class);
   
    @Value("${border.id}")
    private int borderId;
    
    @Value("${core.service.uri}")
    private String coreServiceUri;
    
    @Value("${border.app.key}")
    private String appKey;
    
    @Value("${border.app.secret}")
    private String secretKey;
    
    //@Value("${heartbeat.interval}")
    //private long heartbeatInterval;
    
    private final long TIMEOUT_TICK = 1000*60*2;    //2 min
    private final long PENDING_TICK = 1000*10;      //10 sec
    
    //Before handshake, add session to pendingSession
    private Map<WebSocketSession, DeviceInfo> pendingSession = new ConcurrentHashMap<>();
    
    private Map<WebSocketSession, DeviceNode> session2deviceMap = new ConcurrentHashMap<>();
    private Map<String, WebSocketSession> device2sessionMap = new ConcurrentHashMap<>();
    private CustomLinkedList<DeviceInfo> timeoutList = new CustomLinkedList<DeviceInfo>(); 
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Connection establed. session: " + session.getId());
        
        //Add to pendingSession, waiting for handshake
        this.pendingSession.put(session, new DeviceInfo(System.currentTimeMillis()));
    }
    
    private void removeSession(WebSocketSession session) {
        //try to remove it from pendingSession
        this.pendingSession.remove(session);
        
        DeviceNode dev = this.session2deviceMap.remove(session);
        if (dev != null) {
            //Set flag to invalid and remove it later
            dev.item.setValid(false);
            if(!dev.item.getDeviceId().isEmpty()) {
                this.device2sessionMap.remove(dev.item.getDeviceId());    
            }
            
         synchronized(this) {
                this.timeoutList.removeWithNode(dev.node);
            }
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        LOGGER.info("Connection closed. session: " + session.getId());
        
        removeSession(session);
            
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        LOGGER.info("Connection error. session: " + session.getId());
        removeSession(session);
    }
    
    @Scheduled(fixedDelay=5000, initialDelay=5000)
    public void tickCheck() {
        //LOGGER.info("tickCheck");
        
        LinkedList<DeviceInfo> invalids = new LinkedList<DeviceInfo>();
        //LinkedList<WebSocketSession> invalidSess = new LinkedList<WebSocketSession>(); 
        
        //check pending list first. the pending list is small, so loop all
        long pendingTick = System.currentTimeMillis() - PENDING_TICK;
        Iterator<Entry<WebSocketSession, DeviceInfo>> it = pendingSession.entrySet().iterator();
        while(it.hasNext()) {
            Entry<WebSocketSession, DeviceInfo> pair = it.next();
            DeviceInfo dev = pair.getValue(); 
            
            if(dev.getLastPingTime() < pendingTick) {
                it.remove();
                
              //if the isValid is True, it must have added to timeoutList, can close session when timeout.    
                if(!dev.isValid()) {
                    WebSocketSession session = pair.getKey();
                    try {
                        session.close();
                        LOGGER.info("timeout in pendingSession, close session: " + session.getId());
                    } catch (IOException e) {
                        LOGGER.info("close session error: " + session.getId());
                    }       
                }
             
            }
        }
        
        long timeoutTick = System.currentTimeMillis() - TIMEOUT_TICK;
        synchronized(this) {
            Iterator<DeviceInfo> iter = timeoutList.iterator();
            while(iter.hasNext()) {
                DeviceInfo dev = iter.next();
                if(!dev.isValid()) {
                    //The deviceNode in sessionMap is invalid after it
                        iter.remove();
                        invalids.add(dev);
                        continue;
                        }
                
                if(dev.getLastPingTime() < timeoutTick) {
                    //Set flag and wait to delete it from sessionMap
                    dev.setValid(false);
                    iter.remove();
                    invalids.add(dev);
                    continue;
                    
                    }
                //No more timeouts
               break;
            }     
        }
        
        //Outside synchronized
        invalids.forEach((DeviceInfo dev) -> {
            if(!dev.getDeviceId().isEmpty()) {
                WebSocketSession session = device2sessionMap.remove(dev.getDeviceId());
                if (session != null) {
                    session2deviceMap.remove(session);
                    try {
                        session.close();
                        LOGGER.info("timeout, close session: " + session.getId() + " tick: " + dev.getLastPingTime() + " tick2: " + timeoutTick);
                    } catch (Exception e) {
                        LOGGER.info("close session error: " + session.getId());
                    }
                }
            }
        });
    }
    
    // Client is responsible for sending ping message for managing connection.
    // No using heartbeat, but use ping and pong.
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        DeviceNode dev = session2deviceMap.get(session);
        if(dev != null) {
            dev.item.setLastPingTime(System.currentTimeMillis());
            
            //Move the item to last   
            synchronized(this) {
                timeoutList.removeWithNode(dev.node);
                dev.node = timeoutList.addLastWithNode(dev.item);
            }
            LOGGER.info("Receiving ping from client, session: " + session.getId());
        }
        
        super.handlePongMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("Received client message. session: " + session.getId() + ", message: " + message.getPayload());
        PduFrame frame = PduFrame.fromJson((String)message.getPayload());
        if(frame == null) {
            LOGGER.error("Unrecognized client message: " + message.getPayload());
            return;
        }
        
        if(frame.getName() == null || frame.getName().isEmpty()) {
            LOGGER.error("Missing name in frame: " + message.getPayload());
            return;
        }
        
        //TODO make it better
        if(frame.getName().equals("HANDSHAKE")) {
            DeviceInfo dev = this.pendingSession.get(session);
            if(dev == null) {
                LOGGER.error("Pending session not exists");
                return;
            }
            
            dev.setLastPingTime(System.currentTimeMillis());
            HandshakeMessage pdu = frame.getPayload(HandshakeMessage.class);
            if(pdu.getDeviceId().isEmpty()) {
                LOGGER.error("Missing deviceId in frame: " + message.getPayload());
                return;
            }
            
            dev.setDeviceId(pdu.getDeviceId());
            dev.setDeviceType(pdu.getDeviceType());
            dev.setMeta(pdu.getMeta());
            
            //Remove it from pending session
            this.pendingSession.remove(session);
            dev.setValid(true);
            
            //Added to sessionMap
            this.device2sessionMap.put(dev.getDeviceId(), session);
            
            DeviceNode devNode = new DeviceNode(null, dev);
            synchronized(this) {
                devNode.node = timeoutList.addLastWithNode(dev);
            }
            this.session2deviceMap.put(session, devNode);
            LOGGER.info("added deviceId: " + dev.getDeviceId());
        } else {
            //Normal message
            LOGGER.error("frame name: " + frame.getName());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
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
    
    public void notify(WebSocketSession serverSession, PusherNotifyPdu pdu) {
        //TODO
        LOGGER.info("Got notify: " + pdu.getMessageId());
        
        //TODO use restCall to get all messages and devices, and send notify to device.
        session2deviceMap.forEach((session, devNode)-> {
            DeviceInfo dev = devNode.item;
            if(dev.isValid()) {
                TextMessage msg = new TextMessage("notify from server hear :)");
                try {
                    session.sendMessage(msg);
                } catch (Exception e) {
                    LOGGER.error("Send message to device: " + dev.getDeviceId() + " failed ");
                }
            }
        });
        
    }
    
    private static class DeviceNode {
        public CustomLinkedList.Node<DeviceInfo> node;
        public DeviceInfo item;
        
        public DeviceNode(CustomLinkedList.Node<DeviceInfo> n, DeviceInfo i) {
            this.node = n;
            this.item = i;
        }
    }
    
}
