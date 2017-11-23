package com.everhomes.activity;

import com.everhomes.scheduler.RunningFlag;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.scheduler.ScheduleProvider;

@Component
@Scope("prototype")
public class ActivityVideoScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private ActivityService activityService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) {

        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();

            String idStr = (String) jobMap.get("id");
            Long id = Long.parseLong(idStr);
            String endTimeStr = (String) jobMap.get("endTime");
            Long endTime = Long.parseLong(endTimeStr);

            activityService.onActivityFinished(id, endTime);
        }
    }
}
