package com.everhomes.scheduler;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.quality.QualityInspectionTasks;
import com.everhomes.util.CronDateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.quality.QualityInspectionStandards;
import com.everhomes.quality.QualityProvider;
import com.everhomes.quality.QualityService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.util.DateHelper;

@Component
@Scope("prototype")
public class QualityInspectionScheduleJob  extends QuartzJobBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionScheduleJob.class);
	
	@Autowired
	private QualityProvider qualityProvider;
	
	@Autowired
	private QualityService qualityService;
	
	@Autowired
	private RepeatService repeatService;

	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("QualityInspectionScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}

		//双机判断
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			//为防止时间长了的话可能会有内存溢出的可能，把每天过期的定时任务清理一下
			scheduleProvider.unscheduleJob("QualityInspectionNotify ");

			qualityProvider.closeDelayTasks();

			List<QualityInspectionStandards> activeStandards = qualityProvider.listActiveStandards();

			for (QualityInspectionStandards standard : activeStandards) {
				boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
				LOGGER.info("QualityInspectionScheduleJob: standard id = " + standard.getId()
						+ "repeat setting id = " + standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
				if (isRepeat) {
					qualityService.createTaskByStandardId(standard.getId());
				}

			}

			sendTaskMsg();
		}
	}

	private void sendTaskMsg() {
		long current = System.currentTimeMillis();//当前时间毫秒数
		long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数

		long endTime = current + (configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);
		//通知当天零点到11分的所有任务
		qualityService.sendTaskMsg(zero, endTime+60000);
		QualityInspectionTasks task = qualityProvider.findLastestQualityInspectionTask(endTime+60000);
		//没有新任务时，等到零点生成任务之后再发通知
		if(task != null) {
			Timestamp taskStartTime = task.getExecutiveStartTime();
			//默认提前十分钟
			long nextNotifyTime = taskStartTime.getTime() - (configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);

			String cronExpression = CronDateUtils.getCron(new Timestamp(nextNotifyTime));

			String qualityInspectionNotifyTriggerName = "QualityInspectionNotify ";
			String qualityInspectionNotifyJobName = "QualityInspectionNotify " + System.currentTimeMillis();
			scheduleProvider.scheduleCronJob(qualityInspectionNotifyTriggerName, qualityInspectionNotifyJobName,
					cronExpression, QualityInspectionTaskNotifyScheduleJob.class, null);
		}

	}
	

}
