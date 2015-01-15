package com.everhomes.messaging;

import java.util.Map;

import com.everhomes.user.UserLogin;

/**
 * Message routing service
 * 
 * @author Kelven Yang
 *
 */
public interface MessagingService {
    public static final int MSG_FLAG_STORED         = 0x1;
    public static final int MSG_FLAG_PUSH_ENABLED   = 0x2;
    
    void registerRoutingHandler(String channelType, MessageRoutingHandler handler);
    void unregisterRoutingHandler(String channelType);
    
    void routeMessage(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
        Map<String, String> messageMeta, String messageBody, int deliveryOption);
}
