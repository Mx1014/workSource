package com.everhomes.investment;

import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.StatisticTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class CustomerStatisticsScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerStatisticsScheduleJob.class);

    public static final String SCHEDELE_NAME = "invitedCustomer-";

    public static String CRON_EXPRESSION = "0 0 3 * * ?";
    //public static String CRON_EXPRESSION = "0 13/5 * * * ?";


    @Autowired
    InvitedCustomerProvider invitedCustomerProvider;
    @Autowired
    InvitedCustomerService invitedCustomerService;
    @Autowired
    CommunityProvider communityProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("the scheduleJob of customer statistics is start!");

        invitedCustomerService.statisticCustomerDaily(new Date());
        invitedCustomerService.statisticCustomerDailyTotal(new Date());
        invitedCustomerService.statisticCustomerTotal(new Date());
        Calendar calendar = Calendar. getInstance();
        calendar.setTime(new Date());
        if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
            invitedCustomerService.statisticCustomerMonthly(new Date());
            invitedCustomerService.statisticCustomerMonthlyTotal(new Date());
        }

    }


}
