package com.everhomes.pushmessage;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.family.FamilyService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.family.FamilyMemberDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.admin.SendMessageAdminTargetType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PushMessageAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PushMessageAction.class);
    private PushMessage pushMessage;
    private long pushCount;
    private final PushMsgInfo msgInfo;
    
    @Autowired
    MessagingService messagingService;
   
    @Autowired
    private  FamilyService familyService;
    
    @Autowired
    private PushMessageProvider pushMessageProvider;
    
    @Autowired
    private PushMessageResultProvider pushMessageResultProvider;
    
    //Because of redis type conversion
    public PushMessageAction(final String id, final String randomId) {
        this.msgInfo = new PushMsgInfo(Long.valueOf(id), Long.valueOf(randomId));
    }
    
    private void runCity() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCityId(this.pushMessage.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId, fm.getCellPhone());
                }
            
            pageOffset++;            
            if(pageOffset > 10000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    
    private void runCommunity() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCommunityId(this.pushMessage.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId, fm.getCellPhone());
                }
            
            pageOffset++;            
            if(pageOffset > 1000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    
    private void runFamily() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = this.familyService.listFamilyMembersByFamilyId(this.pushMessage.getTargetId().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId, fm.getCellPhone());
                }
            
            pageOffset++;            
            if(pageOffset > 1000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    
    private void runUser() {
        this.runByUserId(this.pushMessage.getTargetId().longValue(), null);
    }
    
    private void sendMessageToUser(Long userId, String content, Map<String, String> meta, MessagingConstants deliveryOption) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, deliveryOption.getCode());
    }
    
    private void runByUserId(Long userId, String token) {
        this.pushCount++;
        // 每推送完一批，记录一下数量
        if(this.pushCount % 2000 == 0) {
            try {
                this.pushMessage.setPushCount(new Long(this.pushCount));
                pushMessageProvider.updatePushMessage(this.pushMessage);
            } catch(Exception e) {
                log.error(e.toString());
            }
        }
        
        Map<String, String> extra = new HashMap<String, String>();
        //TODO for pushing title
        extra.put("pushTitle", pushMessage.getTitle());
        
        try {
            if(pushMessage.getMessageType().byteValue() == PushMessageType.NOTIFY.getCode()) {
                //Only for push
                sendMessageToUser(userId, pushMessage.getContent(), extra, MessagingConstants.MSG_FLAG_PUSH_ENABLED);    
            } else {
                sendMessageToUser(userId, pushMessage.getContent(), extra, MessagingConstants.MSG_FLAG_STORED_PUSH);
                }
            
            PushMessageResult msgResult = new PushMessageResult();
            msgResult.setIdentifierToken(token);
            msgResult.setSendTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            msgResult.setMessageId(pushMessage.getId());
            msgResult.setUserId(userId);
            pushMessageResultProvider.createPushMessageResult(msgResult);            
        } catch(Exception ex) {
            log.error("pushmessage error to userId: " + userId + " msg: " + ex.getMessage());
        }

        
    }

    @Override
    public void run() {
        PushMessage newMsg = pushMessageProvider.getPushMessageById(this.msgInfo.getId());
        
        //check randomId and status, not lock hear, but i think it's ok. 
        if(newMsg != null && newMsg.getStatus() == PushMessageStatus.Ready.getCode() && newMsg.getPushCount().equals(this.msgInfo.getRandomId())) {
            newMsg.setPushCount(0l);
            newMsg.setStatus(PushMessageStatus.Processing.getCode());
            newMsg.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            pushMessageProvider.updatePushMessage(newMsg);
            
            this.pushMessage = newMsg;
            this.pushCount = 0;
            
            SendMessageAdminTargetType targetType = SendMessageAdminTargetType.fromCode(this.pushMessage.getTargetType());
            log.info("Running pushmessage id= " + this.pushMessage.getTargetId() + " targetType: " + this.pushMessage.getTargetType());
            
            switch(targetType) {
            case CITY:
                this.runCity();
                break;
            case COMMUNITY:
                this.runCommunity();
                break;
            case FAMILY:
                this.runFamily();
                break;
            case USER:
                this.runUser();
                break;
            }  
            
            newMsg.setPushCount(new Long(this.pushCount));
            newMsg.setStatus(PushMessageStatus.Finished.getCode());
            newMsg.setFinishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            pushMessageProvider.updatePushMessage(newMsg);
        } else {
            if(this.pushMessage != null) {
                log.info("Ignore pushmessage id= " + this.pushMessage.getTargetId() + " targetType: " + this.pushMessage.getTargetType());    
                }
        }
    }
}
