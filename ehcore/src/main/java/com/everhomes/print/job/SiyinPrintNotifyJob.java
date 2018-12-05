package com.everhomes.print.job;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.messaging.MessagingService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.print.SiyinPrintOrderProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.PrintOrderActionData;
import com.everhomes.rest.common.RentalOrderActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
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
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;
	@Autowired
	private PortalVersionProvider portalVersionProvider;
    private final static String CLOUD_PRINT_DETAIL = "%s/cloud-print/build/index.html#/print-detail?orderNo=%s";

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
        String homeurl = configProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String url = String.format(CLOUD_PRINT_DETAIL,homeurl,order.getOrderNo());
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(order.getNamespaceId());
		List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleApp(order.getNamespaceId(), releaseVersion.getId(), ServiceModuleConstants.PRINT_MODULE);
        actionData.setModuleId(ServiceModuleConstants.PRINT_MODULE);
        actionData.setClientHandlerType((byte)2);
        actionData.setAppId(apps.get(0).getOriginId());
        actionData.setCommunityId(order.getOwnerId());
        actionData.setUrl(url);

        String routerUri = RouterBuilder.build(Router.CLOUD_PRINT_DETAIL, actionData);

        RouterMetaObject mo = new RouterMetaObject();
        mo.setRouter(routerUri);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        messageDto.setMeta(meta);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
}
