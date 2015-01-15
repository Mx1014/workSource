package com.everhomes.messaging;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.rpc.PduFrame;
import com.everhomes.rpc.client.RealtimeMessageIndicationPdu;
import com.everhomes.rpc.client.StoredMessageIndicationPdu;
import com.everhomes.rpc.server.ClientForwardPdu;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Name;

/**
 * 
 * Routing handler to dispatch messages targeting individual users
 * 
 * @author Kelven Yang
 *
 */
@Component
@Name("user")
public class UserMessageRoutingHandler implements MessageRoutingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageBoxProvider messageBoxProvider;
    
    @Autowired
    private BorderConnectionProvider borderConnectionProvider;
    
    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            Map<String, String> messageMeta, String messageBody) {
        
        // TODO perform blacklist or filtering in the future
        return true;
    }

    @Override
    public void routeMessage(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            Map<String, String> messageMeta, String messageBody, int deliveryOption) {
        long uid = Long.parseLong(dstChannelToken);
        
        List<UserLogin> logins = this.userService.listUserLogins(uid);
        logins.parallelStream().filter((UserLogin login) -> { 
            if(login.getNamespaceId() != senderLogin.getNamespaceId())
                return false;
            
            if(login.getLoginId() == senderLogin.getLoginId())
                return false;
            
            return true;
        }).forEach((UserLogin login) -> {
            routeMessageTo(senderLogin, appId, login, dstChannelType, dstChannelToken, messageMeta, messageBody, deliveryOption);
        });
    }
    
    private void routeMessageTo(UserLogin senderLogin, long appId, UserLogin dstLogin, String destChannelType, String dstChannelToken,
        Map<String, String> messageMeta, String messageBody, int deliveryOption) {

        if((deliveryOption & MessagingService.MSG_FLAG_STORED) != 0) {
            routeStoredMessage(senderLogin, appId, dstLogin, destChannelType, dstChannelToken, messageMeta, messageBody, deliveryOption);
        } else {
            routeRealtimeMessage(senderLogin, appId, dstLogin, destChannelType, dstChannelToken, messageMeta, messageBody, deliveryOption);
        }
    }
    
    private void routeStoredMessage(UserLogin senderLogin, long appId, 
        UserLogin destLogin, String destChannelType, String destChannelToken,
        Map<String, String> messageMeta, String messageBody, int deliveryOption) {
    
        // stored message should be stored first
        Message msg = new Message();
        msg.setNamespaceId(senderLogin.getNamespaceId());
        if(messageMeta != null) {
            for(Map.Entry<String, String> entry: messageMeta.entrySet()) {
                msg.addMetaParam(entry.getKey(), entry.getValue());
            }
        }
        
        msg.setNamespaceId(senderLogin.getNamespaceId());
        msg.setAppId(appId);
        msg.setChannelType(destChannelType);
        msg.setChannelToken(destChannelToken);
        msg.setContent(messageBody);
        
        String boxKey = getMessageBoxKey(destLogin, senderLogin.getNamespaceId(), appId);
        this.messageBoxProvider.putMessage(boxKey, msg);

        boolean onlineDelivered = false;
        if(destLogin.getLoginBorderId() != null) {
            BorderConnection borderConnection = this.borderConnectionProvider.getBorderConnection(destLogin.getLoginBorderId().intValue());
            if(borderConnection != null) {
                StoredMessageIndicationPdu clientPdu = new StoredMessageIndicationPdu();
                PduFrame clientPduFrame = new PduFrame();
                clientPduFrame.setAppId(appId);
                clientPduFrame.setPayload(clientPdu);
                
                ClientForwardPdu forwardPdu = new ClientForwardPdu();
                forwardPdu.setLoginToken(destLogin.getLoginToken().getTokenString());
                forwardPdu.setClientFrame(clientPduFrame);
        
                try {
                    borderConnection.sendMessage(null, forwardPdu);
                    onlineDelivered = true;
                } catch(IOException e) {
                    LOGGER.warn("Failed to deliver message to border", e);
                }
            }
        }
        
        if(!onlineDelivered && (deliveryOption & MessagingService.MSG_FLAG_PUSH_ENABLED) != 0) {
            // TODO perform push notification
        }
    }
    
    private void routeRealtimeMessage(UserLogin senderLogin, long appId, 
        UserLogin destLogin, String destChannelType, String destChannelToken,
        Map<String, String> messageMeta, String messageBody, int deliveryOption) {

        // push notification is not available in realtime message
        if(destLogin.getLoginBorderId() != null) {
            BorderConnection borderConnection = this.borderConnectionProvider.getBorderConnection(destLogin.getLoginBorderId().intValue());
            if(borderConnection != null) {
                RealtimeMessageIndicationPdu clientPdu = new RealtimeMessageIndicationPdu();
                clientPdu.setAppId(appId);
                clientPdu.setChannelType(destChannelType);
                clientPdu.setChannelToken(destChannelToken);
                clientPdu.setSenderUid(senderLogin.getUserId());
                clientPdu.setContent(messageBody);
                clientPdu.setMetaMap(messageMeta);
                clientPdu.setCreateTime(DateHelper.currentGMTTime().getTime());
                
                PduFrame clientPduFrame = new PduFrame();
                clientPduFrame.setAppId(appId);
                clientPduFrame.setPayload(clientPdu);
                
                ClientForwardPdu forwardPdu = new ClientForwardPdu();
                forwardPdu.setLoginToken(destLogin.getLoginToken().getTokenString());
                forwardPdu.setClientFrame(clientPduFrame);
        
                try {
                    borderConnection.sendMessage(null, forwardPdu);
                } catch(IOException e) {
                    LOGGER.warn("Failed to deliver message to border", e);
                }
            }
        }
    }
    
    private String getMessageBoxKey(UserLogin dstLogin, int namespaceId, long appId) {
        StringBuffer sb = new StringBuffer();
        sb.append("mbx-");
        sb.append(dstLogin.getUserId());
        sb.append("-").append(namespaceId);
        sb.append("-").append(appId);
        sb.append("-").append(dstLogin.getLoginId());
        return sb.toString();
    }
}
