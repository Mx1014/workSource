package com.everhomes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.everhomes.message.HandshakeMessage;
import com.everhomes.rest.pusher.PusherMessageResp;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;

public class PusherWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherWebSocketHandler.class);
   
    @Value("${border.id}")
    private int borderId;
    
    @Value("${core.service.uri}")
    private String coreServiceUri;
    
    @Autowired
    private HttpRestCallProvider httpRestCallProvider;
    
    private final long TIMEOUT_TICK = 1000*60*2;    //2 min
    private final long PENDING_TICK = 1000*10;      //10 sec
    
    //Before handshake, add session to pendingSession
    private Map<WebSocketSession, DeviceInfo> pendingSession = new ConcurrentHashMap<>();
    
    private Map<WebSocketSession, DeviceNode> session2deviceMap = new ConcurrentHashMap<>();
    private Map<String, WebSocketSession> device2sessionMap = new ConcurrentHashMap<>();
    private CustomLinkedList<DeviceInfo> timeoutList = new CustomLinkedList<DeviceInfo>(); 
    
    static private Object lockobj = new Object();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Connection establed, session=" + session.getId());
        
        //Add to pendingSession, waiting for handshake
        this.pendingSession.put(session, new DeviceInfo(System.currentTimeMillis()));
    }
    
    private void removeSession(WebSocketSession session) {
        //try to remove it from pendingSession
        this.pendingSession.remove(session);
        
        DeviceNode dev = this.session2deviceMap.remove(session);
        if (dev != null) {
            //Set flag to invalid and remove it later
            dev.Item().setValid(false);
            if(!dev.Item().getDeviceId().isEmpty()) {
                this.device2sessionMap.remove(dev.Item().getDeviceId());    
            }
            
         synchronized(lockobj) {
                this.timeoutList.removeWithNode(dev.node);
            }
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
        LOGGER.info("Connection closed, session=" + session.getId());
        
        removeSession(session);
            
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
        LOGGER.info("Connection error, session=" + session.getId());
        if(session.isOpen()){  
            session.close();  
        }
        removeSession(session);
    }
    
    private void tickInner() {
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
                        LOGGER.info("timeout in pendingSession, session=" + session.getId());
                    } catch (IOException e) {
                        LOGGER.info("close session error, session=" + session.getId(), e);
                    }       
                }
             
            }
        }
        
        long timeoutTick = System.currentTimeMillis() - TIMEOUT_TICK;
        synchronized(lockobj) {
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
                        LOGGER.info("Register connection timeout, session=" + session.getId() + ", deviceId=" + dev.getDeviceId() 
                            + ", tick=" + dev.getLastPingTime() + ", tick2=" + timeoutTick);
                    } catch (Exception e) {
                        LOGGER.info("Register session close error, session=" + session.getId(), e);
                    }
                }
            }
        });        
    }
    
    @Scheduled(fixedDelay=5000, initialDelay=5000)
    public void tickCheck() {
        try {
            tickInner();
        } catch(Exception ex) {
            LOGGER.warn("tickInner error", ex);
        }
    }
    
    public Map<String, Object> getOnlineDevices() {
        Map<String, Object> map = new HashMap<String, Object>();
        this.device2sessionMap.forEach((String deviceId, WebSocketSession session) -> {
            map.put(deviceId, session.getId());
        });
        
        return map;
    }
    
    // Client is responsible for sending ping message for managing connection.
    // use simple heartbeat to support for javascript
    private void heartbeat(WebSocketSession session) {
        DeviceNode dev = session2deviceMap.get(session);
        if(dev != null) {
            dev.Item().setLastPingTime(System.currentTimeMillis());
            
            //Move the item to last   
            synchronized(lockobj) {
                timeoutList.removeWithNode(dev.node);
                dev.node = timeoutList.addLastWithNode(dev.Item());
            }
            LOGGER.info("Receiving ping from client, session=" + session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("Received client message, session=" + session.getId() + ", message=" + message.getPayload());
        if (message.getPayload().equals("Ping")) {
            heartbeat(session);
            return;
        }
        
        PduFrame frame = PduFrame.fromJson((String)message.getPayload());
        if(frame == null) {
            LOGGER.error("Unrecognized client message, session=" + session.getId() + ", message=" + message.getPayload());
            return;
        }
        
        if(frame.getName() == null || frame.getName().isEmpty()) {
            LOGGER.error("Missing name in frame, session=" + session.getId() + ", message=" + message.getPayload());
            return;
        }
        
        //TODO make it better
        if(frame.getName().equals("HANDSHAKE")) {
            DeviceInfo dev = this.pendingSession.get(session);
            if(dev == null) {
                LOGGER.error("Pending session not exists, session=" + session.getId() + ", message=" + message.getPayload());
                return;
            }
            
            dev.setLastPingTime(System.currentTimeMillis());
            HandshakeMessage pdu = frame.getPayload(HandshakeMessage.class);
            if(pdu.getDeviceId().isEmpty()) {
                LOGGER.error("Missing deviceId in frame, session=" + session.getId() + ", message=" + message.getPayload());
                return;
            }
            
            dev.setDeviceId(pdu.getDeviceId());
            dev.setDeviceType(pdu.getDeviceType());
            dev.setMeta(pdu.getMeta());
            
            Map<String, String> params = new HashMap<String, String>();
            
            //Add for name space
//            if(pdu.getNamespaceId() != null && !pdu.getNamespaceId().equals(0)) {
//                params.put("namespaceId", pdu.getNamespaceId().toString());
//                dev.setDeviceId("ns:" + pdu.getNamespaceId() + ":" + dev.getDeviceId());
//                }
            
            params.put("deviceId", dev.getDeviceId());
            params.put("platform", "android");
            params.put("product", "");
            params.put("brand", "");
            params.put("deviceModel", "");
            params.put("systemVersion", "");
            params.put("meta", "{}");
            
            httpRestCallProvider.restCall("pusher/registDevice", params, new ListenableFutureCallback<ResponseEntity<String>> () {
                @Override
                public void onSuccess(ResponseEntity<String> result) {
                  //Remove it from pending session
                    pendingSession.remove(session);
                    dev.setValid(true);
                    
                    //Added to sessionMap
                    WebSocketSession oldSession = device2sessionMap.put(dev.getDeviceId(), session);
                    if(oldSession != null) {
                        try {
                            oldSession.close();
                        } catch (IOException e) {
                            LOGGER.info("Cannot close session: " + oldSession.getId());
                            }
                        }
                    
                    DeviceNode devNode = new DeviceNode(null, dev);
                    synchronized(PusherWebSocketHandler.lockobj) {
                        devNode.node = timeoutList.addLastWithNode(dev);
                        }
                    session2deviceMap.put(session, devNode);
                    LOGGER.info("added deviceId, deviceId=" + dev.getDeviceId() + ", session=" + session.getId());
                }
                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("RestCall pusher/registDevice failed, session=" + session.getId(), ex);
                  //Remove it from pending session
                    pendingSession.remove(session);
                    dev.setValid(false);
                    
                    try {
                        session.close();
                    } catch (IOException e) {
                        LOGGER.info("close session error, session=" + session.getId(), e);
                    }
                }
            });
            
        } else if(frame.getName().equals("REQUEST")) {
            DeviceNode devNode = this.session2deviceMap.get(session);
            if(devNode == null || (!devNode.Item().isValid())) {
                LOGGER.error("Device not exists, session=" + session.getId());
                return;
            }
            DeviceInfo dev = devNode.Item();
            RecentMessageCommand msgCmd = frame.getPayload(RecentMessageCommand.class);
            if(msgCmd == null) {
                LOGGER.error("request message error, missing anchor, session=" + session.getId() + ", deviceId=" + dev.getDeviceId());
                return;
                }
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", dev.getDeviceId());
            if(msgCmd.getAnchor() != null) {
                params.put("anchor", msgCmd.getAnchor().toString());    
                }
            if(msgCmd.getCount() != null) {
                params.put("count", msgCmd.getCount().toString());
            } else {
                params.put("count", "10");
                }
            
            if(msgCmd.getNamespaceId() != null && !msgCmd.getNamespaceId().equals(0)) {
                params.put("namespaceId", msgCmd.getNamespaceId().toString());
            }
                       
            httpRestCallProvider.restCall("pusher/recentMessages", params, new ListenableFutureCallback<ResponseEntity<String>> () {
                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    PduFrame pdu = new PduFrame();
                    pdu.setName("MESSAGES");
                    pdu.setPayLoadForString(result.getBody());
                    WebSocketSessionProxy.sendMessage(session, new TextMessage(pdu.getEncodedPayload()), MessageRecordSenderTag.NOTIFY_REQUEST.getCode(), dev.getDeviceId());
//                    try {
//                        synchronized(session) {
//                            session.sendMessage(new TextMessage(pdu.getEncodedPayload()));
//                        }
//                        LOGGER.info("Restcall pusher/recentMessages, session=" + session.getId() + ", deviceId=" + dev.getDeviceId() + ", param=" + params);
//                    } catch (IOException e) {
//                        LOGGER.error("Session send error, session=" + session.getId() + ", deviceId=" + dev.getDeviceId(), e);
//                    }
                    
                }
                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("restCall failed, session=" + session.getId() + ", deviceId=" + dev.getDeviceId(), ex);
                }
            });
            
        } else {
            //Normal message
            LOGGER.error("Unsupported frame name, frameName=" + frame.getName() + ", session=" + session.getId());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        heartbeat(session);
    }
    
    public void notify(WebSocketSession serverSession, PusherNotifyPdu pduServer) {
        LOGGER.info("Got notify from core server, pdu=" + pduServer);
        
//        session2deviceMap.forEach((session, devNode)-> {
//            DeviceInfo dev = devNode.item;
//            if(dev.isValid()) {
//                PusherMessageResp resp = new PusherMessageResp();
//                resp.setName("NOTIFY");
//                resp.setContent("notify from server :)");
//                PduFrame pdu = new PduFrame();
//                pdu.setPayload(resp);
//                try {
//                        synchronized(session) {
//                          session.sendMessage(new TextMessage(pdu.getEncodedPayload()));
//                        }
//                } catch (IOException e) {
//                      LOGGER.error("Notify server error: " + e.getMessage());
//                     }
//            }
//        });
        
        //TODO 推送策略控制 ?
        if(pduServer.getMessageType().equals("UNICAST")) {
            String deviceId = pduServer.getDeviceId();//already has name space hear.
            WebSocketSession clientSession = device2sessionMap.get(deviceId);
            if (clientSession == null) {
                LOGGER.warn("unicast device id not found, deviceId=" + deviceId);
                return;
            }
            DeviceNode devNode = session2deviceMap.get(clientSession);
            if(devNode == null || !devNode.Item().isValid()) {
                LOGGER.warn("Got session but device id not found, session=" + clientSession.getId() + ", deviceId=" + deviceId);
                return;
            }
            PusherMessageResp resp = new PusherMessageResp();
            resp.setContent("notify");
            PduFrame pdu = new PduFrame();
            pdu.setPayload(resp);
            WebSocketSessionProxy.sendMessage(clientSession, new TextMessage(pdu.toJson()), MessageRecordSenderTag.NOTIFY_EVENT.getCode(), deviceId);

//            try {
//                synchronized(clientSession) {
//                  clientSession.sendMessage(new TextMessage(pdu.toJson()));
//                }
//                if(LOGGER.isInfoEnabled()) {
//                    LOGGER.info("Forward unicast message to client sucessfully, session=" + clientSession.getId() + ", deviceId=" + deviceId);
//                }
//            } catch (IOException e) {
//                    LOGGER.error("Forward unicast message to client error, session=" + clientSession.getId() + ", deviceId=" + deviceId, e);
//               }
        }
        
        //pduServer.getNotification();
        
    }
    
    public DeviceRequestPdu getDeviceInfo(DeviceRequestPdu pdu) {
        List<Long> valids = new ArrayList<Long>();
        pdu.setLastValids(valids);
        
        for(String deviceId : pdu.getDevices()) {
            WebSocketSession clientSession = device2sessionMap.get(deviceId);
            valids.add(0l);
            if (clientSession == null) {
                continue;
            }
            
            DeviceNode devNode = session2deviceMap.get(clientSession);
            if(devNode == null || !devNode.Item().isValid()) {
                continue;
            }
            
            valids.set(valids.size()-1, devNode.Item().getLastPingTime());
        }
        
        return pdu;
    }
    
    private static class DeviceNode {
        public CustomLinkedList.Node<DeviceInfo> node;
        private DeviceInfo nochanged_item;
        
        public DeviceInfo Item() {
            return this.nochanged_item;
        }
        
        public DeviceNode(CustomLinkedList.Node<DeviceInfo> n, DeviceInfo i) {
            this.node = n;
            
            //The this.node.item will be changed somewhere, so hear the one will never be changed
            this.nochanged_item = i;
        }
    }
    
}
