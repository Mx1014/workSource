package com.everhomes.openapi;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.AppConstants;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.ActionType;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value="Door Vender Constroller", site="messaging")
@RestController
@RequestMapping("/openapi")
public class DoorVenderController extends ControllerBase {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    MessagingService messagingService;
    
    private void sendMessageToUser(User u, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), u.getId().toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                u.getId().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    @RequestMapping("notifyMessage")
    @RestReturn(NotifyDoorMessageResponse.class)
    public RestResponse notifyMessage(@Valid NotifyDoorMessageCommand cmd) {
        NotifyDoorMessageResponse rsp = new NotifyDoorMessageResponse();
        for(String phone: cmd.getPhones()) {
            User u = userService.findUserByIndentifier(phone);
            if(null == u) {
                rsp.getPhoneStatus().add(new PhoneStatus(phone, "NOT_FOUND"));
                continue;
                }
            
            sendMessageToUser(u, cmd.getContent(), null);
            rsp.getPhoneStatus().add(new PhoneStatus(phone, "OK"));
        }
        
        RestResponse response = new RestResponse(rsp);
        return response;        
    }
    
    @RequestMapping("notifyDoorLock")
    @RestReturn(NotifyDoorMessageResponse.class)
    public RestResponse notifyDoorLock(@Valid notifyDoorLockCommand cmd) {
        NotifyDoorMessageResponse rsp = new NotifyDoorMessageResponse();
        for(String phone: cmd.getPhones()) {
            User u = userService.findUserByIndentifier(phone);
            if(null == u) {
                rsp.getPhoneStatus().add(new PhoneStatus(phone, "NOT_FOUND"));
                continue;
                }
            
            Map<String, String> meta = new HashMap<String, String>();
            meta.put("actionType", Byte.toString(ActionType.OPEN_DOOR.getCode()));
            meta.put("actionData", "{}");
            sendMessageToUser(u, "Open door", meta);
            rsp.getPhoneStatus().add(new PhoneStatus(phone, "OK"));
        }
        RestResponse response = new RestResponse(rsp);
        return response;
    }
}
