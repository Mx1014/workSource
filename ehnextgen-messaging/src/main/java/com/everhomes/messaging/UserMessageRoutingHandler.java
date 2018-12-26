// @formatter:off
package com.everhomes.messaging;

import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.message.MessageRecordStatus;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.rest.rpc.client.RealtimeMessageIndicationPdu;
import com.everhomes.rest.rpc.client.StoredMessageIndicationPdu;
import com.everhomes.rest.rpc.server.ClientForwardPdu;
import com.everhomes.rest.user.DeviceIdentifierType;
import com.everhomes.rest.user.UserMuteNotificationFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.*;
import com.everhomes.util.DateHelper;
import com.everhomes.util.MessagePersistWorker;
import com.everhomes.util.Name;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

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
    private final static String MESSAGE_INDEX_ID = "indexId";
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private MessageBoxProvider messageBoxProvider;
    
    @Autowired
    private BorderConnectionProvider borderConnectionProvider;
    
    @Autowired
    private PusherService pusherService;

    @Autowired
    private SequenceProvider sequenceProvider;

//    @Autowired
//    private TaskScheduler taskScheduler;
//
//    @Autowired
//    private MessagePersistWorker messagePersistWorker;

//    private ConcurrentLinkedQueue<MessageRecordDto> queue = new ConcurrentLinkedQueue<>();

//    @PostConstruct
//    public void setup(){
//        taskScheduler.scheduleAtFixedRate(()-> {
//            while (!queue.isEmpty()){
//                MessageRecordDto record = queue.poll();
//                this.messagePersistWorker.handleMessagePersist(record);
//            }
//        }, 5*1000);
//    }

    
    @Override
    public boolean allowToRoute(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message) {
        
        // TODO perform blacklist or filtering in the future
        return true;
    }

    @Override
    public void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message, int deliveryOption) {

        //把消息添加到队列里
        MessageRecordDto record = new MessageRecordDto();
        record.setAppId(appId);
        record.setNamespaceId(senderLogin.getNamespaceId());
        record.setMessageSeq(0L);
        record.setSenderUid(senderLogin.getUserId());
        record.setSenderTag(MessageRecordSenderTag.ROUTE_MESSAGE.getCode());
        MessageChannel messageChannel = message.getChannels().get(0);
        record.setDstChannelType(messageChannel.getChannelType());
        record.setDstChannelToken(messageChannel.getChannelToken());
        record.setChannelsInfo(messageChannel.toString());
        record.setBodyType(message.getBodyType());
        record.setBody(message.getBody());
        record.setDeliveryOption(deliveryOption);
        record.setStatus(MessageRecordStatus.CORE_HANDLE.getCode());
        record.setIndexId(message.getMeta().get(MESSAGE_INDEX_ID) != null ?Long.valueOf(message.getMeta().get(MESSAGE_INDEX_ID)) : 0);
        record.setMeta(message.getMeta());
        record.setCreateTime(new Timestamp(message.getCreateTime() != null ? message.getCreateTime() : System.currentTimeMillis()));
        MessagePersistWorker.getQueue().offer(record);

        long uid = Long.parseLong(dstChannelToken);
        
        if(uid == 0) {
            //ignore the anonymous added by Janson
            return;
        }

        List<UserLogin> logins = this.userService.listUserLogins(uid);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Routing message, appId=" + appId + ", senderId" + senderLogin.getUserId() + ", dstChannelType=" + dstChannelType 
                + ", dstChannelToken=" + dstChannelToken + ", receiverId= " + uid + ", senderLogin=" + senderLogin);
            for(UserLogin receiverLogin : logins) {
                LOGGER.debug("Routing message, appId=" + appId + ", senderId" + senderLogin.getUserId() + ", receiverLogin=" + receiverLogin);
            }
        }
        
        // reflect back message to sender
        if((deliveryOption & MessagingConstants.MSG_FLAG_REFLECT_BACK.getCode()) != 0) {
            routeMessageTo(senderLogin, appId, senderLogin, dstChannelType, dstChannelToken, message, deliveryOption);

            /*logins.stream().filter((UserLogin login) -> {
                if ((login.getStatus() == UserLoginStatus.LOGGED_IN) && login.getUserId() == senderLogin.getUserId() && login.getLoginId() == senderLogin.getLoginId()) {
                    return true;
                }
                return context.checkAndAdd(login.getUserId(), login.getLoginId());
            }).forEach((UserLogin login) -> {
                routeMessageTo(senderLogin, appId, login, dstChannelType, dstChannelToken, message, deliveryOption);
            });*/
        }

        MessageDTO shadowClone = message;

        if((deliveryOption & MessagingConstants.MSG_FLAG_REFLECT_BACK.getCode()) != 0) {
            try {
                shadowClone = (MessageDTO) message.clone();
                shadowClone.setSenderTag(null);
            } catch (CloneNotSupportedException e) {
                LOGGER.error("Unexpected exception", e);
            }
        }
        
        final MessageDTO shadowCloneFinal = shadowClone;
        logins.parallelStream().filter((UserLogin login) -> {
//            if (login.getNamespaceId() != senderLogin.getNamespaceId()) {
//                LOGGER.warn(String.format("Namespace %d of target login does not match namespace %d of sender",
//                        login.getNamespaceId(), senderLogin.getNamespaceId()));
//                return false;
//            }

            if (login.getUserId() == senderLogin.getUserId() && login.getLoginId() == senderLogin.getLoginId()) {
                LOGGER.info("Drop message that will be routed to the current sender, userId=" + login.getUserId() 
                    + ", loginId=" + login.getLoginId());
                return false;
            }
            
            // 系统用户不接收消息，故即使系统用户有其它的login也不能路由过去，以免消息并发的时候产生过多无用消息影响性能 by lqs 20160129
            // 用户ID小于某值的用户为系统用户，这样判断简单点、以免影响性能
            if(login.getUserId() < User.MAX_SYSTEM_USER_ID) {
                LOGGER.info("Drop message that will be routed to system user, userId=" + login.getUserId() 
                    + ", loginId=" + login.getLoginId());
                return false;
            }
            
            if(login.getDeviceIdentifier() == null || login.getDeviceIdentifier().equals(DeviceIdentifierType.INNER_LOGIN.name())) {
                return false;
            }

            return context.checkAndAdd(login.getUserId(), login.getLoginId());
        }).forEach((UserLogin login) -> {
            routeMessageTo(senderLogin, appId, login, dstChannelType, dstChannelToken, shadowCloneFinal, deliveryOption);
        });
    }
    
    private void routeMessageTo(UserLogin senderLogin, long appId, UserLogin dstLogin, String destChannelType, String dstChannelToken,
        MessageDTO message, int deliveryOption) {

        if((deliveryOption & MessagingConstants.MSG_FLAG_STORED.getCode()) != 0) {
            routeStoredMessage(senderLogin, appId, dstLogin, destChannelType, dstChannelToken, message, deliveryOption);
        } else {
            routeRealtimeMessage(senderLogin, appId, dstLogin, destChannelType, dstChannelToken, message, deliveryOption);
        }
    }
    
    private void routeStoredMessage(UserLogin senderLogin, long appId, 
        UserLogin destLogin, String destChannelType, String destChannelToken,
        MessageDTO message, int deliveryOption) {

        MessageChannel mainChannel = message.getChannels().get(0);

        // 免打扰, 不推送     add by xq.tian  2017/04/18
        // 这里只检查用户给用户发消息的免打扰状态，群组的消息在群组handler里检查过了
        if (ChannelType.USER.getCode().equals(mainChannel.getChannelType())) {
            UserNotificationSetting notificationSetting = userProvider.findUserNotificationSetting(
                    EntityType.USER.getCode(), destLogin.getUserId(), EntityType.USER.getCode(), senderLogin.getUserId());
            if (notificationSetting != null && notificationSetting.getMuteFlag() == UserMuteNotificationFlag.MUTE.getCode()) {
                deliveryOption &= ~MessagingConstants.MSG_FLAG_PUSH_ENABLED.getCode();
            }
        }

        // stored message should be stored first
        Message msg = new Message();
        //update by Janson.
        //msg.setNamespaceId(senderLogin.getNamespaceId());
        msg.setNamespaceId(destLogin.getNamespaceId());
        msg.setAppId(appId);
        msg.setSenderUid(senderLogin.getUserId());
        msg.setContextType(message.getContextType());
        msg.setContextToken(message.getContextToken());
        msg.setChannelType(mainChannel.getChannelType());
        msg.setChannelToken(mainChannel.getChannelToken());
        msg.setContent(message.getBody());
        msg.setSenderTag(message.getSenderTag());
        msg.setMetaAppId(message.getMetaAppId());
        msg.setMeta(message.getMeta());

        if(message.getBodyType() != null && !message.getBodyType().isEmpty()) {
            if(null == message.getMeta()) {
                message.setMeta(new HashMap<>());
            }
            message.getMeta().put("bodyType", message.getBodyType());    
        }

        if (senderLogin.getUserId() < User.MAX_SYSTEM_USER_ID) {
            //标志位：SYS_MSG_ROUTER_FLAG
            message.getMeta().put(MessageMetaConstant.SYS_MSG_ROUTER_FLAG,MessageMetaConstant.SYS_MSG_ROUTER_FLAG);
        }

        long msgId = this.messageBoxProvider.createMessage(msg);
        
        String boxKey = getMessageBoxKey(destLogin, destLogin.getNamespaceId(), appId);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Routing message(storage), appId=" + appId + ", senderId" + senderLogin.getUserId() + ", dstChannelType=" + destChannelType 
                + ", dstChannelToken=" + destChannelToken + ", msgChannelType=" + msg.getChannelType() + ", msgChannelToken=" + msg.getChannelToken() 
                + ", boxKey= " + boxKey + ", senderLogin=" + senderLogin + ", destLogin=" + destLogin);
        }
        this.messageBoxProvider.putMessage(boxKey, msgId);

        boolean onlineDelivered = false;

        //把消息添加到队列里
        MessageRecordDto record = new MessageRecordDto();
        record.setAppId(appId);
        record.setNamespaceId(senderLogin.getNamespaceId());
        record.setMessageSeq(message.getStoreSequence());
        record.setSenderUid(senderLogin.getUserId());
        record.setSenderTag(MessageRecordSenderTag.ROUTE_STORE_MESSAGE.getCode());
        record.setDstChannelType(mainChannel.getChannelType());
        record.setDstChannelToken(mainChannel.getChannelToken());
        record.setChannelsInfo(mainChannel.toString());
        record.setBodyType(message.getBodyType());
        record.setBody(message.getBody());
        record.setDeliveryOption(deliveryOption);
        record.setStatus(MessageRecordStatus.CORE_ROUTE.getCode());
        record.setIndexId(message.getMeta().get(MESSAGE_INDEX_ID) != null ? Long.valueOf(message.getMeta().get(MESSAGE_INDEX_ID)) : 0);
        record.setMeta(message.getMeta());
        record.setCreateTime(new Timestamp(message.getCreateTime() != null ? message.getCreateTime() : System.currentTimeMillis()));
        MessagePersistWorker.getQueue().offer(record);

        //If not push only, send it by border server
        if((MessagingConstants.MSG_FLAG_PUSH_ENABLED.getCode() != deliveryOption) && (destLogin.getLoginBorderId() != null)) {
            BorderConnection borderConnection = this.borderConnectionProvider.getBorderConnection(destLogin.getLoginBorderId());
            if(borderConnection != null) {
                StoredMessageIndicationPdu clientPdu = new StoredMessageIndicationPdu();
                ClientForwardPdu forwardPdu = buildForwardPdu(destLogin, appId, clientPdu);
                try {
                    borderConnection.sendMessage(null, forwardPdu);
                    onlineDelivered = true; // 为苹果设备做冗余，即苹果设备在线，也要发推送
                } catch(IOException e) {
                    LOGGER.warn("Failed to deliver message to border", e);
                }
            }
        }

        if((deliveryOption & MessagingConstants.MSG_FLAG_PUSH_ENABLED.getCode()) != 0) {
            if(onlineDelivered) {
                this.pusherService.checkAndPush(senderLogin, destLogin, msgId, msg);
            } else {
                this.pusherService.pushMessage(senderLogin, destLogin, msgId, msg);
            }
        }
    }
    
    private void routeRealtimeMessage(UserLogin senderLogin, long appId, 
        UserLogin destLogin, String destChannelType, String destChannelToken,
        MessageDTO message, int deliveryOption) {

        MessageChannel mainChannel = message.getChannels().get(0);
        
        // push notification is not available in realtime message
        if(destLogin.getLoginBorderId() != null) {
            BorderConnection borderConnection = this.borderConnectionProvider.getBorderConnection(destLogin.getLoginBorderId());
            if(borderConnection != null) {
                RealtimeMessageIndicationPdu clientPdu = new RealtimeMessageIndicationPdu();
                clientPdu.setAppId(appId);
                clientPdu.setChannelType(mainChannel.getChannelType());
                clientPdu.setChannelToken(mainChannel.getChannelToken());
                clientPdu.setSenderUid(senderLogin.getUserId());
                clientPdu.setContextType(message.getContextType());
                clientPdu.setContextToken(message.getContextToken());
                clientPdu.setContent(message.getBody());
                clientPdu.setSenderTag(message.getSenderTag());
                clientPdu.setMetaAppId(message.getMetaAppId());
                clientPdu.setMetaMap(message.getMeta());
                clientPdu.setCreateTime(DateHelper.currentGMTTime().getTime());
                
                ClientForwardPdu forwardPdu = buildForwardPdu(destLogin, appId, clientPdu);
                try {
                    //把消息添加到队列里
                    MessageRecordDto record = new MessageRecordDto();
                    record.setAppId(appId);
                    record.setNamespaceId(senderLogin.getNamespaceId());
                    record.setMessageSeq(0L);
                    record.setSenderUid(senderLogin.getUserId());
                    record.setSenderTag(MessageRecordSenderTag.ROUTE_REALTIME_MESSAGE.getCode());
                    record.setDstChannelType(mainChannel.getChannelType());
                    record.setDstChannelToken(mainChannel.getChannelToken());
                    record.setChannelsInfo(mainChannel.toString());
                    record.setBodyType(message.getBodyType());
                    record.setBody(message.getBody());
                    record.setDeliveryOption(deliveryOption);
                    record.setStatus(MessageRecordStatus.CORE_ROUTE.getCode());
                    record.setIndexId(message.getMeta().get(MESSAGE_INDEX_ID) != null ? Long.valueOf(message.getMeta().get(MESSAGE_INDEX_ID)) : 0);
                    record.setMeta(message.getMeta());
                    record.setCreateTime(new Timestamp(message.getCreateTime() != null ? message.getCreateTime() : System.currentTimeMillis()));
                    MessagePersistWorker.getQueue().offer(record);

                    borderConnection.sendMessage(null, forwardPdu);
                } catch(IOException e) {
                    LOGGER.warn("Failed to deliver message to border", e);
                }
            }
        }
    }
    
    private static ClientForwardPdu buildForwardPdu(UserLogin destLogin, long forwardedPduAppId, Object forwardedPdu) {
        PduFrame clientPduFrame = new PduFrame();
        clientPduFrame.setAppId(forwardedPduAppId);
        clientPduFrame.setPayload(forwardedPdu);

        ClientForwardPdu forwardPdu = new ClientForwardPdu();
        forwardPdu.setLoginToken(WebTokenGenerator.getInstance().toWebToken(destLogin.getLoginToken()));
        forwardPdu.setClientFrame(clientPduFrame);

        return forwardPdu;
    }
    
    public static String getMessageBoxKey(UserLogin dstLogin, int namespaceId, long appId) {
        StringBuffer sb = new StringBuffer();
        sb.append("mbx-");
        sb.append(dstLogin.getUserId());
        sb.append("-").append(namespaceId);
        sb.append("-").append(appId);
        sb.append("-").append(dstLogin.getLoginId());
        return sb.toString();
    }
}
