package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.energy.*;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.energy.CreateEnergyTaskCommand;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by ying.xiong on 2017/10/26.
 */
@Component
public class EnergyTaskScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyTaskScheduleJob.class);

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private EnergyMeterTaskProvider energyMeterTaskProvider;

    @Autowired
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
        }

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            closeDelayTasks();
            createTask();
        }
    }

    private void closeDelayTasks() {
        LOGGER.info("EnergyTaskScheduleJob: close delay tasks.");
        //suanqian

    }

    private void createTask() {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob: createTask");
        }
        List<EnergyPlan> plans = energyPlanProvider.listActivePlan();

        if(plans != null && plans.size() > 0) {
            plans.forEach(plan -> {
                List<EnergyPlanMeterMap> maps = energyPlanProvider.listMetersByEnergyPlan(plan.getId());
                if(maps != null && maps.size() > 0) {
                    boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
                    LOGGER.info("EnergyScheduleJob: plan id = " + plan.getId()
                            + "repeat setting id = "+ plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);

                    for(EnergyPlanMeterMap map : maps) {
                        EnergyMeter meter = meterProvider.findById(plan.getNamespaceId(), map.getMeterId());
                        if(meter == null || meter.getStatus() == null
                                || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                            LOGGER.info("EnergyScheduleJob meter is not exist or active! meterId = " + map.getMeterId());
                            continue;
                        } else if(isRepeat){
                            this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_ENERGY_TASK.getCode()).tryEnter(()-> {
                                energyConsumptionService.creatMeterTask(map, plan);
                            });
                        }
                    }
                }
            });
        }
    }

}
