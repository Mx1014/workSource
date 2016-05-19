package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.LinkBody;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.promotion.OpPromotionWebPageData;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.StringHelper;

public class OpPromotionStaticWebPageAction implements OpPromotionAction {
    @Autowired
    MessagingService messagingService;
    
    @Autowired
    PromotionService promotionService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    OpPromotionMessageProvider promotionMessageProvider;
    
    @Override
    public void fire(OpPromotionContext ctx) {
        
        OpPromotionActivityContext activityContext = (OpPromotionActivityContext)ctx;
        User user = activityContext.getUser();
        Long userId = user.getId();
        
        String dataStr = activityContext.getPromotion().getActionData();
        OpPromotionWebPageData data = (OpPromotionWebPageData)StringHelper.fromJsonString(dataStr, OpPromotionWebPageData.class);
        
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()), 
                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.BIZ_USER_LOGIN.getUserId())));
        
        messageDto.setBodyType(MessageBodyType.LINK.getCode());
        LinkBody linkBody = new LinkBody();
        linkBody.setActionUrl(data.getUrl());
        linkBody.setContent(activityContext.getPromotion().getDescription());
        linkBody.setTitle(activityContext.getPromotion().getTitle());
        String bodyStr = StringHelper.toJsonString(linkBody);
        
        messageDto.setBody(bodyStr);
        
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode()); 
        
        if(activityContext.getNeedUpdate()) {
            promotionService.addPushCountByPromotionId(activityContext.getPromotion().getId(), 1);    
        }
        
        OpPromotionMessage promotionMessage = new OpPromotionMessage();
        promotionMessage.setMessageText(data.getUrl());
        promotionMessage.setNamespaceId(activityContext.getPromotion().getNamespaceId());
        promotionMessage.setSenderUid(User.SYSTEM_UID);
        promotionMessage.setTargetUid(userId);
        promotionMessage.setResultData(dataStr);
        promotionMessage.setMessageSeq(userService.getNextStoreSequence(User.SYSTEM_USER_LOGIN, 
                activityContext.getPromotion().getNamespaceId(), AppConstants.APPID_MESSAGING));
        promotionMessageProvider.createOpPromotionMessage(promotionMessage);
        
    }

}
