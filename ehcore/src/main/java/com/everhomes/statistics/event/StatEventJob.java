// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by xq.tian on 2017/8/3.
 */
@Component
public class StatEventJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatEventJob.class);

    @Autowired
    private StatEventJobService statEventJobService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    private final boolean manuallyExecute = false;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            LocalDate statDate = LocalDate.now().minusDays(1);
            LOGGER.info("stat event job start [{}]", statDate);
            StatEventTaskExecution taskExecution = statEventJobService.getTaskExecution(statDate, false);
            statEventJobService.executeTask(taskExecution);
            LOGGER.info("stat event job finish [{}]", statDate);
        }
    }
}
