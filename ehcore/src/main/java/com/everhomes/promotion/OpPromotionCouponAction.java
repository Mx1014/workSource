package com.everhomes.promotion;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.db.DbProvider;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.LinkBody;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.promotion.OpPromotionCouponData;
import com.everhomes.rest.promotion.OpPromotionOwnerType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.StringHelper;

@Component
@Scope("prototype")
public class OpPromotionCouponAction implements OpPromotionAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpPromotionCouponAction.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    MessagingService messagingService;
    
    @Autowired
    PromotionService promotionService;
    
    @Autowired
    OpPromotionMessageProvider promotionMessageProvider;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ScheduleTaskLogProvider scheduleTaskLogProvider;
    
    @Override
    public void fire(OpPromotionContext ctx) {
        OpPromotionActivityContext activityContext = (OpPromotionActivityContext)ctx;
        User user = activityContext.getUser();
        Long userId = user.getId();
        
        String dataStr = activityContext.getPromotion().getActionData();
        OpPromotionCouponData data = (OpPromotionCouponData)StringHelper.fromJsonString(dataStr, OpPromotionCouponData.class);
        
        OpPromotionMessage promotion = this.dbProvider.execute(new TransactionCallback<OpPromotionMessage>() {
            @Override
            public OpPromotionMessage doInTransaction(TransactionStatus arg0) {
                OpPromotionMessage promotionMessage = promotionMessageProvider.findTargetByPromotionId(userId, activityContext.getPromotion().getId());
                if(promotionMessage != null) {
                    return promotionMessage;
                } else {
                    promotionMessage = new OpPromotionMessage();
                    promotionMessage.setMessageText(data.getUrl());
                    promotionMessage.setNamespaceId(activityContext.getPromotion().getNamespaceId());
                    promotionMessage.setSenderUid(User.SYSTEM_UID);
                    promotionMessage.setTargetUid(userId);
                    promotionMessage.setResultData(dataStr);
                    promotionMessage.setOwnerId(activityContext.getPromotion().getId());
                    promotionMessage.setOwnerType(OpPromotionOwnerType.USER.getCode());
                    promotionMessage.setMessageSeq(userService.getNextStoreSequence(User.SYSTEM_USER_LOGIN, 
                            activityContext.getPromotion().getNamespaceId(), AppConstants.APPID_MESSAGING));
                    promotionMessageProvider.createOpPromotionMessage(promotionMessage);
                    
                    return null;
                }
            }
        });
        
        if(promotion != null) {
        	if(LOGGER.isDebugEnabled()){
        		LOGGER.error("already pushed to user=" + promotion.getTargetUid() + ", promotionId=" + promotion.getOwnerId());
        	}
            return;
        }
        
        
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
        
        Map<String, String> meta = new HashMap<String, String>();
        meta.put("popup-flag", "1");
        messageDto.setMeta(meta);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        
        if(activityContext.getNeedUpdate()) {
            promotionService.addPushCountByPromotionId(activityContext.getPromotion().getId(), 1);    
        }
        
        for(Long coupon : data.getCouponList()) {
            promotionService.bizFetchCoupon(userId, coupon);
        }
    }

}
