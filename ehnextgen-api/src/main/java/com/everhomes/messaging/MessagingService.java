// @formatter:off
package com.everhomes.messaging;

import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.user.FetchMessageCommandResponse;
import com.everhomes.rest.user.FetchPastToRecentMessageCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageAdminCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageCommand;
import com.everhomes.user.UserLogin;

/**
 * Message routing service
 * 
 * @author Kelven Yang
 *
 */
public interface MessagingService {
    FetchMessageCommandResponse fetchPastToRecentMessages(FetchPastToRecentMessageCommand cmd);
    FetchMessageCommandResponse fetchRecentToPastMessages(FetchRecentToPastMessageCommand cmd);
    long getMessageCountInLoginMessageBox(UserLogin login);
    
    void registerRoutingHandler(String channelType, MessageRoutingHandler handler);
    void unregisterRoutingHandler(String channelType);
    
    void routeMessage(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
        MessageDTO message, int deliveryOption);
    
    void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message, int deliveryOption);
    FetchMessageCommandResponse fetchRecentToPastMessagesAny(FetchRecentToPastMessageAdminCommand cmd);
     
}
