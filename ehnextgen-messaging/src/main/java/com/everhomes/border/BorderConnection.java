// @formatter:off
package com.everhomes.border;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.bus.LocalBusSubscriber.Action;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.rpc.HeartbeatPdu;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PingRequestPdu;
import com.everhomes.rest.rpc.server.PingResponsePdu;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Name;
import com.everhomes.util.NamedHandler;
import com.everhomes.util.NamedHandlerDispatcher;
import com.everhomes.util.RuntimeErrorException;

/**
 * WebSocket connection to border server
 * 
 * @author Kelven Yang
 *
 */
public class BorderConnection extends AbstractWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BorderConnection.class);
    
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private LocalBusMessageClassRegistry messageClassRegistry;
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    private Border border;
    
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    
    private WebSocketSession wsSession;
    private List<PduFrame> outputQueue = new ArrayList<>();
    private AtomicLong lastSendTick = new AtomicLong();
    private AtomicLong lastReceiveTick = new AtomicLong();
    private AtomicLong exceptionCount = new AtomicLong();
    
    @Value("${heartbeat.interval}")
    private long heartbeatInterval;
    
    // connect failed is a transitional state used to do async-notification when connect process encounters
    // synchronous exception
    private enum State { disconnected, connecting, connected, connectFailed };    
    private volatile State state = State.disconnected;
    
    public BorderConnection() {
    }
    
    private void close() {
        try {
            wsSession.close();
            if(wsSession != null)
                wsSession.close();
            
            localBus.publish(null, "border.close", border);
        } catch (IOException e) {
            LOGGER.error("Unable to close WS socket session", e);
        }
    }
    
    public void onConnectionFailed() {
        state = State.connectFailed;
        taskScheduler.schedule(()-> { heartbeat(); }, new Date());
    }
    
    public void connect(int borderId) throws Exception {
        border = borderProvider.findBorderById(borderId);
        if(state == State.disconnected) {
            border = borderProvider.findBorderById(borderId);
            if(border == null) {
                LOGGER.error("Unable to find border server record, borderId: " + borderId);
                
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find border server record, borderId: " + borderId);
            }
       
            taskScheduler.scheduleAtFixedRate(()-> { heartbeat(); }, heartbeatInterval);
            doConnect();
        }
    }
    
    private void doConnect() throws Exception {
        if(state == State.disconnected) {
            state = State.connecting;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            StandardWebSocketClient wsClient = new StandardWebSocketClient(container);
            WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
            wsClient.setTaskExecutor(new SimpleAsyncTaskExecutor());
            
            URI uri = new URI(String.format("ws://%s:%d/interserver", border.getPrivateAddress(), border.getPrivatePort()));
            
            //Added by Janson
            //this.wsSession = wsClient.doHandshake(this, wsHeaders, uri).get();
            
            // Important: .get() call is mandatory to trigger possible exceptions during connecting phase,
            // these exceptions are mandatory to handle correctly, otherwise, core-server may never be able to contact
            // border server if it fails to connect in its first try
            wsClient.doHandshake(this, wsHeaders, uri).get();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Border connection " + border.getPrivateAddress() + ":" + border.getPrivatePort() + " has been established");
        state = State.connected;
        
        //Added by Janson
        if((null == this.wsSession) || (session != this.wsSession)) {
            synchronized(this) {
                this.wsSession = session;
            }
        }
        
        checkOutput();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        this.lastReceiveTick.set(DateHelper.currentGMTTime().getTime());
        
        PduFrame frame = PduFrame.fromJson((String)message.getPayload());
        if(frame == null) {
            LOGGER.error("Unrecognized frame, frame content: " + message.getPayload());
            return;
        }
        
        if(frame.getName() == null) {
            LOGGER.error("Frame name is missing, frame content: " + message.getPayload());
            return;
        }
        
        //TODO why error ?
        NamedHandlerDispatcher.invokeHandler(this, frame.getName(), frame);
//        if(frame.getName().equals("heartbeat")) {
//            this.handleHeartbeatPdu(frame);
//        }

        if(frame.getRequestId() != null) {
            // do local bus publish in case it is a RPC response
            Class<?> payloadClass = this.messageClassRegistry.getRegisteredClass(frame.getName());
            if(payloadClass != null) {
                Object payload = frame.getPayload(payloadClass);
                String subject = frame.getName();
                subject += "." + frame.getRequestId().longValue();
                localBus.publish(null, subject, payload);
            } else {
                LOGGER.error("Unable to find message class for " + frame.getName() + " in message class registry");
            }
        }
    }
    
    public void sendMessage(Long requestId, Object obj) throws IOException {
        Name annotation = obj.getClass().getAnnotation(Name.class);
        if(annotation == null || annotation.value() == null) {
            String errorMsg = "Object class " + obj.getClass().getName() + " does not have required LocalBusMessage annotation";
            
            LOGGER.error(errorMsg);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, errorMsg);
        }
        
        PduFrame frame = new PduFrame();
        frame.setRequestId(requestId).setPayload(obj);
        this.addMessageToQueue(frame);
        
        checkOutput();
    }

    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        LOGGER.info("Border connection transport error. Connection: " + border.getPrivateAddress() + ":" + border.getPrivatePort());
        state = State.disconnected;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        LOGGER.info("Border connection is closed. Connection: " + border.getPrivateAddress() + ":" + border.getPrivatePort());
        
        state = State.disconnected;
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @NamedHandler(value="", byClass=HeartbeatPdu.class)
    private void handleHeartbeatPdu(PduFrame frame) {
        HeartbeatPdu pdu = frame.getPayload(HeartbeatPdu.class);
//        if(LOGGER.isDebugEnabled())
//            LOGGER.debug(String.format("Received heartbeat from border %d(%s:%d), last receive tick on border is %d", 
//                border.getId(), border.getPrivateAddress(), border.getPrivatePort(), pdu.getLastPeerReceiveTime()));
    }
    
    @NamedHandler(value="", byClass=DeviceRequestPdu.class)
    private void handleDeviceRequest(PduFrame frame) {
        
    }
    
    private void heartbeat() {
        if(this.state == State.connected) {
            long currentTick = DateHelper.currentGMTTime().getTime();
            if(currentTick - this.lastSendTick.get() > this.heartbeatInterval) {
                HeartbeatPdu pdu = new HeartbeatPdu();
                pdu.setLastPeerReceiveTime(this.lastReceiveTick.get());
             
                try {
                    sendMessage(null, pdu);
                } catch(IOException e) {
                    LOGGER.warn(String.format("Exception when sending heartbeat message to border %d(%s:%d)", border.getId(),
                            border.getPrivateAddress(), border.getPrivatePort()));
                }
            }
        } else if(state == State.connectFailed) {
            state = State.disconnected;
            close();
        }
    }
    
    private void addMessageToQueue(PduFrame frame) {
        synchronized(this.outputQueue) {
            this.outputQueue.add(frame);
        }
    }
    
    private void returnMessageToQueue(PduFrame msg) {
        synchronized(this.outputQueue) {
            this.outputQueue.add(0, msg);
        }
    }
    
    private PduFrame getNextOutputMessage() {
        synchronized(this.outputQueue) {
            if(!this.outputQueue.isEmpty())
                return this.outputQueue.remove(0);
        }
        
        return null;
    }
    
    private void checkOutput() {
        // 日志过多，先注释掉 by lqs 20171102
        // LOGGER.info("checkOutput: state=" + state );
        if(state == State.connected) {
            PduFrame rpcMessage = null;
            
            //Added by Janson, not lock hear, should never be hear.
            if(null == this.wsSession) {
                    return;
                }
            
            while((rpcMessage = getNextOutputMessage()) != null) {
                WebSocketMessage<String> message = new TextMessage(rpcMessage.toJson());
                try {
                    synchronized(this) {
                        this.wsSession.sendMessage(message);
                        }
                    this.lastSendTick.set(DateHelper.currentGMTTime().getTime());
                    exceptionCount.set(0);
                } catch(IOException e) {
                    LOGGER.debug("Failed to send current message, return it to queue. message: " + message.getPayload());
                    //Too much error, it may be error
                    exceptionCount.addAndGet(1l);
                    if(exceptionCount.get() >= 10) {
                            state = State.disconnected;
                        }
                    returnMessageToQueue(rpcMessage);
                    break;
                }
            }
        }
    }
    
    public String getConnectionState() {
        switch(this.state) {
        case disconnected:
            return "disconnected";
        case connecting:
            return "connecting";
        case connected:
            return "connected";
        case connectFailed:
            return "connectFailed";
        default:
           return "error";
        }
    }
    
    //TODO better for promise
    public CompletableFuture<DeviceRequestPdu> requestDevice(DeviceRequestPdu request) {
        long requestId = LocalSequenceGenerator.getNextSequence();
        CompletableFuture<DeviceRequestPdu> cf = new CompletableFuture<>();
        
        String subject = LocalBusMessageClassRegistry.getMessageClassSubjectName(DeviceRequestPdu.class);
        localBusSubscriberBuilder.build(subject + "." + requestId, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                    Object resp, String path) {
                DeviceRequestPdu pdu = (DeviceRequestPdu)resp;
                
                cf.complete(pdu);
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                cf.completeExceptionally(new Exception("timeout"));
            }
        }).setTimeout(5000).create();
        
        try {
            this.sendMessage(requestId, request);
        } catch (Exception ex) {
            cf.completeExceptionally(ex);
        }
        
        return cf;
    }
}
