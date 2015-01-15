package com.everhomes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rpc.HeartbeatPdu;
import com.everhomes.rpc.PduFrame;
import com.everhomes.util.NamedHandler;
import com.everhomes.util.NamedHandlerDispatcher;

public class ControlMessageHandler implements ClientMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientImpl.class);

    public PduFrame onClientMessage(PduFrame frame) {
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Received message: " + frame.getEncodedPayload());

        if(frame.getName() == null || frame.getName().isEmpty()) {
            LOGGER.error("Missing name in frame: " + frame.getEncodedPayload());
            return null;
        }
        
        NamedHandlerDispatcher.invokeHandler(this, frame.getName(), frame);
        return null;
    }
    
    @NamedHandler(value="", byClass=HeartbeatPdu.class)
    private void handleHeartbeatPdu(PduFrame frame) {
        HeartbeatPdu pdu = frame.getPayload(HeartbeatPdu.class);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Received heartbeat from server, last activity tick received by peer: " + pdu.getLastPeerReceiveTime());
    }
}
