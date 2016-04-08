package com.everhomes.scheduler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public class Jobs {
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	private static String QUALITY_INSPECTION_TRIGGER_NAME = "QualityInspection";
	
	private QualityInspectionScheduleJob qualityInspectionScheduleJob = new QualityInspectionScheduleJob();
	
	@PostConstruct
	public void doScheduleJob() {
		scheduleProvider.scheduleCronJob(QUALITY_INSPECTION_TRIGGER_NAME, QUALITY_INSPECTION_TRIGGER_NAME,
											"0 0 3 * * ? ", qualityInspectionScheduleJob.getClass(), null);
	}

}
