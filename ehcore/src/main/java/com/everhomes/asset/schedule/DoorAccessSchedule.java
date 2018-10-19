package com.everhomes.asset.schedule;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.scheduler.AssetDoorAccessJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;

/**
 * 缴费欠费对接门禁定时任务
 * 
 * @author created by djm
 */
@Component
public class DoorAccessSchedule implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DoorAccessSchedule.class);

	final String downloadDir = "\\download\\";

	DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	DateTimeFormatter autoDateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Value("${equipment.ip}")
	private String equipmentIp;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ScheduleProvider scheduleProvider;

	static final String TASK_EXECUTE = "energyTask.isexecute";
	final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	
	//定时任务时间
	//static final String cronExpression = "0/10 * *  * * ?";
	static final String cronExpression = "0 0/10 *  * * ?";

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			init();
		}
	}

	// 每天早上5点10分刷自动读表
	// "0 10 5 L * ?"
	// @Scheduled(cron = "0 0 0 * * ?")
	// @Scheduled(cron="0/10 * * * * ? ") //每10秒执行一次

	public void init() {
		String autoReading = "EnergyAutoReading " + System.currentTimeMillis();
		String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
		LOGGER.info("================================================energyTaskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
		
		if (taskServer.equals(equipmentIp)) {
			if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
				scheduleProvider.scheduleCronJob(autoReading, autoReading, cronExpression, AssetDoorAccessJob.class, null);
			}
		}
	}
}
