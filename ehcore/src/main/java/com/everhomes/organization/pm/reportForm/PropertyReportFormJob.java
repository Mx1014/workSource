package com.everhomes.organization.pm.reportForm;

import java.sql.Timestamp;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.util.DateHelper;

public class PropertyReportFormJob extends QuartzJobBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReportFormJob.class);
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private PropertyReportFormService propertyReportFormService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
	        LOGGER.info("start PropertyReportFormJob at" + new Timestamp(DateHelper.currentGMTTime().getTime()));
	    }
	    //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
        	propertyReportFormService.generateReportFormStatics();
        }
	}

}
