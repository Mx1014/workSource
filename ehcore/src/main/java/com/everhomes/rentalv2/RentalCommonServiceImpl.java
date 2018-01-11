package com.everhomes.rentalv2;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.RentalOrderActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sw on 2018/1/9.
 */
@Component
public class RentalCommonServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ServiceImpl.class);

    @Autowired
    private MessagingService messagingService;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    public RentalResourceHandler getRentalResourceHandler(String handlerName) {
        RentalResourceHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalOrderHandler getRentalOrderHandler(String handlerName) {
        RentalOrderHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalMessageHandler getRentalMessageHandler(String handlerName) {
        RentalMessageHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalResource getRentalResource(String resourceType, Long resourceId) {
        String handlerName = RentalResourceHandler.DEFAULT;
        if (StringUtils.isNotBlank(resourceType)) {
            handlerName = resourceType;
        }
        RentalResourceHandler handler = getRentalResourceHandler(handlerName);
        RentalResource rs = handler.getRentalResourceById(resourceId);

        return rs;
    }

    public void sendMessageToUser(Long userId, String content) {
        if(null == userId)
            return;
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        LOGGER.debug("messageDTO : {}", messageDto);
        // 发消息 +推送
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    public void sendRouterMessageToUser(Long userId, String content, Long orderId, String resourceType) {
        if(null == userId)
            return;
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        RentalOrderActionData actionData = new RentalOrderActionData();
        actionData.setOrderId(orderId);
        actionData.setResourceType(resourceType);

        String routerUri = RouterBuilder.build(Router.RENTAL_ORDER_DETAIL, actionData);

        RouterMetaObject mo = new RouterMetaObject();
        mo.setUrl(routerUri);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        messageDto.setMeta(meta);

        LOGGER.debug("messageDTO : {}", messageDto);
        // 发消息 +推送
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    public void sendMessageCode(Long uid, Map<String, String> map, int code) {
        String scope = RentalNotificationTemplateCode.SCOPE;
        String locale = RentalNotificationTemplateCode.locale;

        String notifyText = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyText);
    }
}
