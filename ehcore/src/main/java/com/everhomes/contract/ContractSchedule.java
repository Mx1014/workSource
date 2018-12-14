package com.everhomes.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;

/**
 * 合同报表定时任务
 * 
 * @author created by djm
 */
@Component
public class ContractSchedule implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContractSchedule.class);

	@Value("${equipment.ip}")
	private String equipmentIp;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ScheduleProvider scheduleProvider;
	
	static final String cronExpression = "0/10 * *  * * ?";

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			init();
		}
	}

	public void init() {
		// String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_CONTRACT_TASK_TIME, "0 30 2 * * ? ");
		String autoReading = "ContractReportFormSchedule " + System.currentTimeMillis();
		String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
		LOGGER.info("================================================contractTaskServer: " + taskServer + ", equipmentIp: " + equipmentIp);

		if (taskServer.equals(equipmentIp)) {
			if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
				try {
					//scheduleProvider.scheduleCronJob(autoReading, autoReading, cronExpression, ContractScheduleJob.class, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
