package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2017/10/26.
 */
@Component
public class EnergyTaskScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyTaskScheduleJob.class);

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
        }

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {

//            closeDelayTasks();
//            createTask();
        }
    }
}
