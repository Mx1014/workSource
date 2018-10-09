package com.everhomes.print.job;

import com.everhomes.messaging.MessagingService;
import com.everhomes.print.SiyinPrintNotificationTemplateCode;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
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
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            String appName = jobMap.getString("appName");
            Integer namespaceId = jobMap.getIntegerFromString("namespaceId");
            Long creatorUid = jobMap.getLong("creatorUid");
            sendMessageToUser(appName, namespaceId, creatorUid);

        }catch (Exception e) {
            LOGGER.error("SiyinPrintMessageJob error", e);
        }

    }
    private void sendMessageToUser(String appName, Integer namespaceId, Long creatorUid) {
        String templateScope = SiyinPrintNotificationTemplateCode.SCOPE;
        String templateLocale = SiyinPrintNotificationTemplateCode.locale;
        int templateId = SiyinPrintNotificationTemplateCode.PRINT_UNPAID_MESSAGE;

        List<Tuple<String, Object>> variables = smsProvider.toTupleList("appName", appName);

        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(creatorUid,
                IdentifierType.MOBILE.getCode());
        if (null == userIdentifier) {
            LOGGER.error("userIdentifier is null...userId = " + creatorUid);
        } else {
            smsProvider.sendSms(namespaceId, userIdentifier.getIdentifierToken(), templateScope,
                    templateId, templateLocale, variables);
        }
    }
}
