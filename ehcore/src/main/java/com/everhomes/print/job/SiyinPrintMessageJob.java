package com.everhomes.print.job;

import com.everhomes.print.SiyinPrintNotificationTemplateCode;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.print.SiyinPrintOrderProvider;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.Tuple;

import java.util.List;

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
public class SiyinPrintMessageJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintMessageJob.class);

    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
	private SiyinPrintOrderProvider siyinPrintOrderProvider;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByOrderNo(jobMap.getLong("orderNo"));
            if (order.getOrderStatus()==PrintOrderStatusType.UNPAID.getCode()){
            	Integer namespaceId = order.getNamespaceId();
            	Long creatorUid = order.getCreatorUid();
            	sendMessageToUser(namespaceId, creatorUid);
            }
        }catch (Exception e) {
            LOGGER.error("SiyinPrintMessageJob error", e);
        }

    }
    private void sendMessageToUser(Integer namespaceId, Long creatorUid) {
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = SiyinPrintNotificationTemplateCode.locale;
        int templateId = SmsTemplateCode.PRINT_UNPAID_MESSAGE;
        List<Tuple<String, Object>> variables = null;
        //List<Tuple<String, Object>> variables = smsProvider.toTupleList("appName", appName);

        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(creatorUid,
                IdentifierType.MOBILE.getCode());
        if (null == userIdentifier) {
            LOGGER.error("userIdentifier is null...userId = " + creatorUid);
        } else {
        	LOGGER.info("send message to unpaid order, user = {}, phone = {}.",creatorUid,userIdentifier.getIdentifierToken());
            smsProvider.sendSms(namespaceId, userIdentifier.getIdentifierToken(), templateScope,
                    templateId, templateLocale, variables);
        }
    }
}
