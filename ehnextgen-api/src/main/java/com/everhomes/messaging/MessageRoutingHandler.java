// @formatter:off
package com.everhomes.messaging;

import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.user.UserLogin;

/**
 * Messages will be routed inside core-server, routing of the message will be handled
 * by message handlers. This interfaces defines the contract that routing handler needs to implement 
 * 
 * @author Kelven Yang
 *
 */
public interface MessageRoutingHandler {
    
    /**
     * Check if the message is allowed to be routed
     * 
     * @param senderLogin
     * @param dstChannelToken
     * @param messageMeta
     * @param messageBody
     * @return true when message is allowed to route, false when message is not allowed
     */
    boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
        MessageDTO message);
    
    /**
     * Route message to its destination
     * 
     * @param senderLogin
     * @param dstChannelToken
     * @param messageMeta
     * @param messageBody
     */
    void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message, int deliveryOption);
}
