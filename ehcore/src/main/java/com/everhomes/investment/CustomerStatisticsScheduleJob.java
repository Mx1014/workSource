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

    //public static String CRON_EXPRESSION = "0 0 3 * * ?";
    public static String CRON_EXPRESSION = "0 0/5 * * * ?";


    @Autowired
    InvitedCustomerProvider invitedCustomerProvider;
    @Autowired
    InvitedCustomerService invitedCustomerService;
    @Autowired
    CommunityProvider communityProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        statisticCustomerDaily();
        Calendar calendar = Calendar. getInstance();
        calendar.setTime(new Date());
        if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
            statisticCustomerMonthly();
        }

    }

    private void statisticCustomerDaily(){
        LOGGER.info("the scheduleJob of customer statistics is start!");
        //Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        StatisticTime statisticTime = invitedCustomerService.getBeforeForStatistic(new Date(), Calendar. DAY_OF_MONTH);
        if(statisticTime != null){
            invitedCustomerService.startCustomerStatistic(statisticTime);
        }else{
            LOGGER.error("only support day or month before one");
        }

    }

    private void statisticCustomerMonthly(){

    }

    public static void main(String[] args) {
        Calendar calendar = Calendar. getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar. HOUR_OF_DAY, 0);
        calendar.set(Calendar. MINUTE, 0);
        calendar.set(Calendar. SECOND, 0);
        calendar.set(Calendar. MILLISECOND, 0);
        calendar.add(Calendar. DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        System.out.println(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        String str = sdf.format(date);
        System. out.println(str);

        calendar.setTime(new Date());
        calendar.set(Calendar. HOUR_OF_DAY, 23);
        calendar.set(Calendar. MINUTE, 59);
        calendar.set(Calendar. SECOND, 59);
        calendar.set(Calendar. MILLISECOND, 999);
        date = calendar.getTime();
        System.out.println(date);
        str = sdf.format(date);
        System. out.println(str);

        calendar.setTime(new Date());
        calendar.set(Calendar. HOUR_OF_DAY, 23);
        calendar.set(Calendar. MINUTE, 59);
        calendar.set(Calendar. SECOND, 59);
        calendar.add(Calendar. MONTH, -1);
        calendar.set(Calendar. MILLISECOND, 999);
        calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        date = calendar.getTime();
        System.out.println(date);
        str = sdf.format(date);
        System. out.println(str);


        calendar.setTime(new Date());
        calendar.set(Calendar. HOUR_OF_DAY, 0);
        calendar.set(Calendar. MINUTE, 0);
        calendar.set(Calendar. SECOND, 0);
        calendar.add(Calendar. MONTH, -1);
        calendar.set(Calendar. MILLISECOND, 0);
        calendar.set(Calendar. DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));


        date = calendar.getTime();
        System.out.println(date);
        str = sdf.format(date);
        System. out.println(str);


        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

    }

}
