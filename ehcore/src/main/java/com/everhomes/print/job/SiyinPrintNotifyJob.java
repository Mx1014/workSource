package com.everhomes.print.job;

import com.everhomes.messaging.MessagingService;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.print.SiyinPrintOrderProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.PrintOrderActionData;
import com.everhomes.rest.common.RentalOrderActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/11/17.
 */
@Component
public class SiyinPrintNotifyJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintNotifyJob.class);

    @Autowired
    private MessagingService messagingService;
    @Autowired
	private SiyinPrintOrderProvider siyinPrintOrderProvider;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            String content = jobMap.getString("content");
            SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByOrderNo(jobMap.getLong("orderNo"));
            if (order.getOrderStatus()==PrintOrderStatusType.UNPAID.getCode()){
            	Long userId = order.getCreatorUid();
            	sendMessageToUser(userId, content, order);
            }
        }catch (Exception e) {
            LOGGER.error("SiyinPrintMessageJob error", e);
        }

    }
    private void sendMessageToUser(Long userId, String content, SiyinPrintOrder order) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        PrintOrderActionData actionData = new PrintOrderActionData();
        actionData.setOrderId(order.getOrderNo());

        String routerUri = RouterBuilder.build(Router.CLOUD_PRINT_DETAIL, actionData);

        RouterMetaObject mo = new RouterMetaObject();
        mo.setUrl(routerUri);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        messageDto.setMeta(meta);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
}
