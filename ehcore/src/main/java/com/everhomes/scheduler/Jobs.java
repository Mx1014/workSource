package com.everhomes.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Jobs  extends QuartzJobBean{
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	private static String QUALITY_INSPECTION_TRIGGER_NAME = "QualityInspection";
	
	private QualityInspectionScheduleJob qualityInspectionScheduleJob = new QualityInspectionScheduleJob();
	

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		scheduleProvider.scheduleCronJob(QUALITY_INSPECTION_TRIGGER_NAME, QUALITY_INSPECTION_TRIGGER_NAME,
				"0 0 3 * * ? ", qualityInspectionScheduleJob.getClass(), null);
		
	}

}
