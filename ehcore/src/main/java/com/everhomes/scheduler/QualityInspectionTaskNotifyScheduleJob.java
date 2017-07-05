package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.quality.QualityInspectionTasks;
import com.everhomes.quality.QualityProvider;
import com.everhomes.quality.QualityService;
import com.everhomes.util.CronDateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/3/8.
 */
public class QualityInspectionTaskNotifyScheduleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInspectionTaskNotifyScheduleJob.class);
    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private QualityProvider qualityProvider;

    @Autowired
    private QualityService qualityService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //双机判断
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.WARNING_QUALITY_TASK.getCode()).tryEnter(() -> {
                LOGGER.info("in QualityInspectionTaskNotifyScheduleJob " + System.currentTimeMillis());
                //默认提前十分钟通知
                long executiveStartTime = System.currentTimeMillis() + (configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);

                qualityService.sendTaskMsg(executiveStartTime, executiveStartTime + 60000);

                QualityInspectionTasks task = qualityProvider.findLastestQualityInspectionTask(executiveStartTime + 60000);
                //没有新任务时，等到零点生成任务之后再发通知
                if (task != null) {
                    Timestamp taskStartTime = task.getExecutiveStartTime();
                    //默认提前十分钟
                    long nextNotifyTime = taskStartTime.getTime() - (configurationProvider.getLongValue(ConfigConstants.EQUIPMENT_TASK_NOTIFY_TIME, 10) * 60000);

                    String cronExpression = CronDateUtils.getCron(new Timestamp(nextNotifyTime));

                    String qualityInspectionNotifyTriggerName = "QualityInspectionNotify ";
                    String qualityInspectionNotifyJobName = "QualityInspectionNotify " + System.currentTimeMillis();
                    scheduleProvider.scheduleCronJob(qualityInspectionNotifyTriggerName, qualityInspectionNotifyJobName,
                            cronExpression, QualityInspectionTaskNotifyScheduleJob.class, null);
                }

            });
        }
    }
}
