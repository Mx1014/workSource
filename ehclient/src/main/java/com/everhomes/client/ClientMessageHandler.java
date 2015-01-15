package com.everhomes.client;

import com.everhomes.rpc.PduFrame;

public interface ClientMessageHandler {
    /**
     * 
     * @param frame
     * @return original message frame or filtered message frame for pipeline process in the future
     */
    PduFrame onClientMessage(PduFrame frame);
}
