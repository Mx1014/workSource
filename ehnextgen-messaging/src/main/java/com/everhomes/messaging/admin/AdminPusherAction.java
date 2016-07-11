package com.everhomes.messaging.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.SelectQuery;
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
import com.everhomes.rest.messaging.admin.SendMessageAdminCommand;
import com.everhomes.rest.messaging.admin.SendMessageAdminTargetType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminPusherAction implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AdminPusherAction.class);
    
    @Autowired
    MessagingService messagingService;
   
    @Autowired
    private  FamilyService familyService;
    
    private final SendMessageAdminCommand cmd;
    
    public AdminPusherAction(SendMessageAdminCommand cmd) {
        this.cmd = cmd;    
     }
    
    private void runCity() {
        int pageSize = 200;
        int pageOffset = 1;
        
        for(;;) {
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCityId(this.cmd.getTargetToken().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
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
            List<FamilyMemberDTO> familyDtos = familyService.listFamilyMembersByCommunityId(this.cmd.getTargetToken().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
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
            List<FamilyMemberDTO> familyDtos = this.familyService.listFamilyMembersByFamilyId(this.cmd.getTargetToken().longValue(), pageOffset, pageSize);
            
            if(null == familyDtos || familyDtos.size() == 0) {
                break;
            }
            
            for(FamilyMemberDTO fm : familyDtos) {
                Long userId = fm.getMemberUid();
                this.runByUserId(userId);
                }
            
            pageOffset++;            
            if(pageOffset > 1000) {
                log.error("Loop so large!");
                break;
                }
            }
    }
    private void runUser() {
        this.runByUserId(this.cmd.getTargetToken());
    }
    
    private void sendMessageToUser(Long userId, String content, Map<String, String> meta) {
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
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    private void runByUserId(Long userId) {
        sendMessageToUser(userId, cmd.getBody(), cmd.getMeta());
    }
    
    @Override
    public void run() {
        SendMessageAdminTargetType targetType = SendMessageAdminTargetType.fromCode(this.cmd.getTargetToken());
        log.info("Running config id= " + cmd.getTargetToken());
        
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
    }

}
