package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.equipment.EquipmentInspectionStandardGroupMap;
import com.everhomes.equipment.EquipmentInspectionTasks;
import com.everhomes.equipment.EquipmentProvider;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.util.CronDateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/3/8.
 */
@Component
public class EquipmentInspectionTaskNotifyScheduleJob extends QuartzJobBean {

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EquipmentProvider equipmentProvider;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.coordinationProvider.getNamedLock(CoordinationLocks.WARNING_EQUIPMENT_TASK.getCode()).tryEnter(()-> {
            //默认提前十分钟通知
            long executiveStartTime = System.currentTimeMillis()+(configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);

            equipmentService.sendTaskMsg(executiveStartTime, executiveStartTime+60000);

            EquipmentInspectionTasks task = equipmentProvider.findLastestEquipmentInspectionTask(executiveStartTime+60000);
            //没有新任务时，等到零点生成任务之后再发通知
            if(task != null) {
                Timestamp taskStartTime = task.getExecutiveStartTime();
                //默认提前十分钟
                long nextNotifyTime = taskStartTime.getTime() - (configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);

                String cronExpression = CronDateUtils.getCron(new Timestamp(nextNotifyTime));

                String equipmentInspectionTriggerName = "EquipmentInspectionNotify " + System.currentTimeMillis();
                scheduleProvider.scheduleCronJob(equipmentInspectionTriggerName, equipmentInspectionTriggerName,
                        cronExpression, EquipmentInspectionTaskNotifyScheduleJob.class, null);
            }

        });
    }

}
