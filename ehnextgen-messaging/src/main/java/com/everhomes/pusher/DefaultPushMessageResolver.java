package com.everhomes.pusher;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.appurl.AppUrlProvider;
import com.everhomes.appurl.AppUrls;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.PushMessageResolver;
import com.everhomes.msgbox.Message;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.OpenMsgSessionActionData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.rest.messaging.DeviceMessageType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessagingLocalStringCode;
import com.everhomes.rest.messaging.MessagingPriorityConstants;
import com.everhomes.user.OSType;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;

@Component(PushMessageResolver.PUSH_MESSAGE_RESOLVER_DEFAULT)
public class DefaultPushMessageResolver implements PushMessageResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPushMessageResolver.class);
	
    @Autowired
    MessagingService messagingService;
   
    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private AppUrlProvider appUrlProvider;
    
    @Override
    public DeviceMessage resolvMessage(UserLogin senderLogin, UserLogin destLogin, Message msg) {
        DeviceMessage deviceMessage = new DeviceMessage();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if(null != UserContext.current().getUser()) {
            locale = UserContext.current().getUser().getLocale(); 
        }
        deviceMessage.setAlert(this.localeStringService.getLocalizedString(
                MessagingLocalStringCode.SCOPE,
                String.valueOf(MessagingLocalStringCode.NEW_MESSAGE_ALERT),
                locale,
                "You have a message"));
                
        deviceMessage.setAlertType(DeviceMessageType.SIMPLE.getCode());
        
        // 由于eh_locale_strings表没有namespace_id，故只能先把配置项放配置表，
        // 按产品要求每个域空间需要使用不同的标题 BUG:http://devops.lab.everhomes.com/issues/4448  by lqs 20161217
        Integer namespaceId = destLogin.getNamespaceId();
//        String messageTitle = this.configurationProvider.getValue(namespaceId, "message.title", "左邻App");        
        
        //去掉title 已有logo和应用名 by xiongying20170110
//        //默认取eh_app_urls的ios版的应用名称，eh_app_urls没收录的取左邻 by xiongying20161228
//        deviceMessage.setTitle("左邻App");
//        
//        AppUrls appUrls = appUrlProvider.findByNamespaceIdAndOSType(senderLogin.getNamespaceId(), OSType.IOS.getCode());
//        if(appUrls != null) {
//        	deviceMessage.setTitle(appUrls.getName());
//        }
//        
//        if(LOGGER.isInfoEnabled()) {
//            LOGGER.info("resolvMessage： appUrls = " + appUrls + " , senderLogin namespaceId is " + senderLogin.getNamespaceId());
//        }
        
        deviceMessage.setBadge(new Integer((int)messagingService.getMessageCountInLoginMessageBox(destLogin)));
        
        deviceMessage.setAppId(msg.getAppId());
        deviceMessage.setAudio("doorRing.caf");
        
        if(senderLogin.getUserId() < 10) {
            deviceMessage.setPriorigy(MessagingPriorityConstants.LOW.getCode());
        } else {
            deviceMessage.setPriorigy(MessagingPriorityConstants.MEDIUM.getCode());
        }
        
        String bodyType = msg.getMeta().get("bodyType");
        if(null == bodyType) {
            return deviceMessage;
            }
        
        deviceMessage.getExtra().putAll(msg.getMeta());
        
        if(msg.getMeta().containsKey("actionType")) {
            deviceMessage.setAction(msg.getMeta().get("actionType"));
        } else if(msg.getAppId() == AppConstants.APPID_MESSAGING) {
            deviceMessage.setAction(Byte.toString((ActionType.OPEN_MSG_SESSION.getCode())));
            OpenMsgSessionActionData actionData = new OpenMsgSessionActionData();
            actionData.setSenderUid(senderLogin.getUserId());
            actionData.setDstChannel(msg.getChannelType());
            actionData.setDstChannelId(Long.parseLong(msg.getChannelToken()));
            actionData.setSrcChannel(msg.getContextType());
            actionData.setSrcChannelId((null == msg.getContextToken() ? null : Long.parseLong(msg.getContextToken())));
            deviceMessage.getExtra().put("actionData", actionData.toString());
        }
//        if(deviceMessage.getExtra().containsKey("jumpType")) {
//            deviceMessage.setAlertType(DeviceMessageType.Jump.getCode());
//            }
        
        MessageBodyType metaType;
        if(msg.getMetaAppId() != null) {
            metaType = MessageBodyType.fromCode(bodyType);
            switch(metaType) {
            case TEXT:
                if(null != msg.getContent()) {
                    deviceMessage.setAlert(msg.getContent());    
                    }
                break;
            case AUDIO:
                deviceMessage.setAlert(this.localeStringService.getLocalizedString(
                        MessagingLocalStringCode.SCOPE,
                        String.valueOf(MessagingLocalStringCode.NEW_MESSAGE_AUDIO_ALERT),
                        locale,
                        "You have a audio message."));
                break;
            case IMAGE:
                deviceMessage.setAlert(this.localeStringService.getLocalizedString(
                        MessagingLocalStringCode.SCOPE,
                        String.valueOf(MessagingLocalStringCode.NEW_MESSAGE_IMAGE_ALERT),
                        locale,
                        "You have a image message."));
                break;
            case VIDEO:
                deviceMessage.setAlert(this.localeStringService.getLocalizedString(
                        MessagingLocalStringCode.SCOPE,
                        String.valueOf(MessagingLocalStringCode.NEW_MESSAGE_IMAGE_ALERT),
                        locale,
                        "You have a video message."));
                break;
            case LINK:
                deviceMessage.setAlert(msg.getContent());
                break;
            default:
                break;
                }
            }
        
        return deviceMessage;
    }

}
