package com.everhomes.investment;

import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.investment.CustomerLevelType;
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
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar. getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar. HOUR_OF_DAY, 0);
        calendar.set(Calendar. MINUTE, 0);
        calendar.set(Calendar. SECOND, 0);
        calendar.set(Calendar. MILLISECOND, 0);
        calendar.add(Calendar. DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        System.out.println(date);
        Timestamp statisticStartTime = new Timestamp(date.getTime());
        calendar.set(Calendar. HOUR_OF_DAY, 23);
        calendar.set(Calendar. MINUTE, 59);
        calendar.set(Calendar. SECOND, 59);
        calendar.set(Calendar. MILLISECOND, 999);
        date = calendar.getTime();
        Timestamp statisticEndTime = new Timestamp(date.getTime());

        List<Long> allCommunities = communityProvider.listAllBizCommunities();

        for(Long communityId : allCommunities){
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}", communityId, statisticStartTime, statisticEndTime);

            List<CustomerLevelChangeRecord> listRecord = invitedCustomerProvider.listCustomerLevelChangeRecord(null, communityId, statisticStartTime, statisticEndTime);
            Integer addCustomerNum = invitedCustomerProvider.countCustomerNumByCreateDate(communityId, statisticStartTime, statisticEndTime);
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, add customer num : {}", communityId, statisticStartTime, statisticEndTime, addCustomerNum);

            List<CustomerLevelChangeRecord> listRegisteredCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.REGISTERED_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to registered num : {}", communityId, statisticStartTime, statisticEndTime, listRegisteredCustomer.size());


            List<CustomerLevelChangeRecord> listLossCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.LOSS_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to loss num : {}", communityId, statisticStartTime, statisticEndTime, listLossCustomer.size());

            List<CustomerLevelChangeRecord> listHistoryCustomer =
                    listRecord.stream().filter(record -> record.getNewStatus().equals(CustomerLevelType.HISTORY_CUSTOMER.getCode())).collect(Collectors.toList());
            LOGGER.debug("the scheduleJob of customer statistics at community : {}, query start date : {}, end date : {}, change to history num : {}", communityId, statisticStartTime, statisticEndTime, listHistoryCustomer.size());

            CustomerStatisticDaily daily = new CustomerStatisticDaily();
            daily.setCommunityId(communityId);
            daily.setRegisteredCustomerNum((long)listRegisteredCustomer.size());
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
