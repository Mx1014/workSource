package com.everhomes.scheduler;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.quality.QualityInspectionStandards;
import com.everhomes.quality.QualityProvider;
import com.everhomes.quality.QualityService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.util.DateHelper;

public class QualityInspectionScheduleJob implements ScheduleJob {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionScheduleJob.class);
	
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("QualityInspectionScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		
		QualityService qualityService = PlatformContext.getComponent(QualityService.class);
		QualityProvider qualityProvider = PlatformContext.getComponent(QualityProvider.class);
		RepeatService repeatService = PlatformContext.getComponent(RepeatService.class);
		
		List<QualityInspectionStandards> activeStandards = qualityProvider.listActiveStandards();
		
		for(QualityInspectionStandards standard : activeStandards) {
			boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
			if(isRepeat) {
				qualityService.createTaskByStandardId(standard.getId());
			}
				
		}
		
	}

}
