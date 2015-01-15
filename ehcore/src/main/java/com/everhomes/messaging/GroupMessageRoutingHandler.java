package com.everhomes.messaging;

import java.util.Map;

import com.everhomes.user.UserLogin;

/**
 * 
 * Routing handler to dispatch group messages
 * 
 * @author Kelven Yang
 *
 */
public class GroupMessageRoutingHandler implements MessageRoutingHandler {

    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, Map<String, String> messageMeta,
            String messageBody) {
        return true;
    }

    @Override
    public void routeMessage(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, Map<String, String> messageMeta,
            String messageBody, int deliveryOption) {
    }
}
