package com.everhomes.scheduler;

import com.everhomes.energy.EnergyConsumptionService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by Rui.Jia  2018/4/19 15 :42
 */
@Component
public class EnergyAutoReadingJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyAutoReadingJob.class);

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("start energy auto Reading task......");
        energyConsumptionService.meterAutoReading(true);
        LOGGER.info("finish  energy auto Reading task......");

    }
}
