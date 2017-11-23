package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.quality.QualityProvider;
import com.everhomes.quality.QualityService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2017/6/14.
 */
public class QualityInspectionStatScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionScheduleJob.class);

    @Autowired
    private QualityProvider qualityProvider;

    @Autowired
    private QualityService qualityService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("QualityInspectionStatScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
        }

        qualityService.updateSampleScoreStat();
    }
}
