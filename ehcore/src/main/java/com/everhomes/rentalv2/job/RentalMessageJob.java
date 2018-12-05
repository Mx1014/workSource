package com.everhomes.rentalv2.job;

import com.everhomes.messaging.MessagingService;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
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
public class RentalMessageJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalMessageJob.class);

    @Autowired
    private MessagingService messagingService;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            String userIds = jobMap.getString("userIds");
            String content = jobMap.getString("content");
            rentalCommonService.sendMessageToUser(userIds,content);

        }catch (Exception e) {
            LOGGER.error("RentalMessageJob error", e);
        }
    }
}
