package com.everhomes.equipment;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.scheduler.EquipmentInspectionScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Rui.Jia  2018/6/12 18 :44
 */
@Component
public class EquipmentInspectionScheduler implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInspectionScheduler.class);
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Value("${equipment.ip}")
    private String equipmentIp;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_EQUIPMENT_TASK_TIME, "0 0 0 * * ? ");
            //  String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_EQUIPMENT_TASK_TIME, "0 */5 * * * ?");
            String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
            LOGGER.info("=======equipment taskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
            if (taskServer.equals(equipmentIp)) {
                if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                    LOGGER.info("starting  equipment scheduler.....");
                    String equipmentInspectionTriggerName = "EquipmentInspection " + System.currentTimeMillis();
                    scheduleProvider.scheduleCronJob(equipmentInspectionTriggerName, equipmentInspectionTriggerName,
                            cronExpression, EquipmentInspectionScheduleJob.class, null);
                }
            }
        }
    }
}
