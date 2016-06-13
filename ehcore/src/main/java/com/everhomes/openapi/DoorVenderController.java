package com.everhomes.openapi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.openapi.NotifyDoorLockCommand;
import com.everhomes.rest.openapi.NotifyDoorMessageCommand;
import com.everhomes.rest.openapi.NotifyDoorMessageResponse;
import com.everhomes.rest.openapi.PhoneStatus;
import com.everhomes.rest.openapi.SyncUserCommand;
import com.everhomes.rest.openapi.SyncUserResponse;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.thirdpartuser.ThirdpartUser;
import com.everhomes.thirdpartuser.ThirdpartUserProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
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
    
    @Autowired
    ThirdpartUserProvider thirdpartUserProvider;
    
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
        User operator = UserContext.current().getUser();
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if(operator != null) {
            namespaceId = operator.getNamespaceId();
        }
        
        NotifyDoorMessageResponse rsp = new NotifyDoorMessageResponse();
        for(String phone: cmd.getPhones()) {
            User u = userService.findUserByIndentifier(namespaceId, phone);
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
    public RestResponse notifyDoorLock(@Valid NotifyDoorLockCommand cmd) {
        User operator = UserContext.current().getUser();
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if(operator != null) {
            namespaceId = operator.getNamespaceId();
        }
        
        NotifyDoorMessageResponse rsp = new NotifyDoorMessageResponse();
        for(String phone: cmd.getPhones()) {
            User u = userService.findUserByIndentifier(namespaceId, phone);
            if(null == u) {
                rsp.getPhoneStatus().add(new PhoneStatus(phone, "NOT_FOUND"));
                continue;
                }
            
            Map<String, String> meta = new HashMap<String, String>();
            meta.put("actionType", Byte.toString(ActionType.OPEN_DOOR.getCode()));
            meta.put("actionData", "{\"remote\": 1, \"vender\":1}");
            sendMessageToUser(u, "Open door", meta);
            rsp.getPhoneStatus().add(new PhoneStatus(phone, "OK"));
        }
        RestResponse response = new RestResponse(rsp);
        return response;
    }
    
    @RequestMapping("syncuserinfo")
    @RestReturn(SyncUserResponse.class)
    public RestResponse syncUserInfo(@Valid SyncUserCommand cmd) {
        ThirdpartUser user = ConvertHelper.convert(cmd, ThirdpartUser.class);
        Date date = new Date(); 
        Timestamp ts = new Timestamp(date.getTime());
        user.setCreateTime(ts);
        thirdpartUserProvider.createUser(user);
        SyncUserResponse rsp = new SyncUserResponse();
        rsp.setTimestamp(ts);
        RestResponse response = new RestResponse(rsp);
        return response;
    }
}
