// @formatter:off
package com.everhomes.statistics.event;

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

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LocalDate statDate = (LocalDate) context.get("statDate");
        statEventJobService.executeTask(statDate);
        LOGGER.info("");
    }
}
