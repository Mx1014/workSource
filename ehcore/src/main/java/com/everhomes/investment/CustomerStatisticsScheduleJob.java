package com.everhomes.investment;

import com.everhomes.community.CommunityProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Scope("prototype")
public class CustomerStatisticsScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerStatisticsScheduleJob.class);

    public static final String SCHEDELE_NAME = "invitedCustomer-";

    public static String CRON_EXPRESSION = "0 0 3 * * ?";
    //public static String CRON_EXPRESSION = "0 3/5 * * * ?";


    @Autowired
    private InvitedCustomerService invitedCustomerService;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.coordinationProvider.getNamedLock(CoordinationLocks.INVITED_CUSTOMER_STATISTIC.getCode()).tryEnter(()-> {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("the scheduleJob of customer statistics is start!");
            }
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                invitedCustomerService.statisticCustomerDaily(new Date());
                invitedCustomerService.statisticCustomerDailyTotal(new Date());
                invitedCustomerService.statisticCustomerTotal(new Date());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    invitedCustomerService.statisticCustomerMonthly(new Date());
                    invitedCustomerService.statisticCustomerMonthlyTotal(new Date());
                }
            }
        });

    }


}
