package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.contract.ContractChargingItem;
import com.everhomes.contract.ContractChargingItemAddress;
import com.everhomes.contract.ContractChargingItemAddressProvider;
import com.everhomes.contract.ContractChargingItemProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.energy.*;
import com.everhomes.organization.pm.DefaultChargingItemProperty;
import com.everhomes.organization.pm.DefaultChargingItemProvider;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.asset.FeeRules;
import com.everhomes.rest.energy.CreateEnergyTaskCommand;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.rest.energy.TaskGeneratePaymentFlag;
import com.everhomes.rest.organization.pm.DefaultChargingItemPropertyType;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
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
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Autowired
    private EnergyMeterTaskProvider taskProvider;

    @Autowired
    private EnergyMeterAddressProvider meterAddressProvider;

    @Autowired
    private EnergyMeterProvider energyMeterProvider;

    @Autowired
    private ContractChargingItemAddressProvider contractChargingItemAddressProvider;

    @Autowired
    private ContractChargingItemProvider contractChargingItemProvider;

    @Autowired
    private DefaultChargingItemProvider defaultChargingItemProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
        }

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            calculateCloseTaskFee();
            createTask();
        }
    }

    private void calculateCloseTaskFee() {
        LOGGER.info("EnergyTaskScheduleJob: calculateCloseTaskFee.");
        //suanqian
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                generatePaymentExpectancies();
            }
        });

    }

    private void generatePaymentExpectancies() {
        List<EnergyMeterTask> tasks = taskProvider.listNotGeneratePaymentEnergyMeterTasks();
        if(tasks != null && tasks.size() > 0) {
            tasks.forEach(task -> {
                Boolean generateFlag = false;
                EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
                if(meter == null || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                    return ;
                }
                //task关联的表关联的门牌有没有合同

                List<EnergyMeterAddress> addresses = meterAddressProvider.listByMeterId(task.getMeterId());
                if(addresses != null && addresses.size() > 0) {
                    EnergyMeterAddress address = addresses.get(0);
                    //eh_contract_charging_item_addresses
                    List<ContractChargingItemAddress> contractChargingItemAddresses = contractChargingItemAddressProvider.findByAddressId(address.getId(), meter.getMeterType());
                    if(contractChargingItemAddresses != null && contractChargingItemAddresses.size() > 0) {
                        contractChargingItemAddresses.forEach(contractChargingItemAddress -> {
                            ContractChargingItem item = contractChargingItemProvider.findById(contractChargingItemAddress.getContractChargingItemId());
                        });
                        //suanqian paymentExpectancies_re_struct();
                        generateFlag = true;
                    } else {//门牌有没有默认计价条款、所属楼栋有没有默认计价条款、所属园区有没有默认计价条款 eh_default_charging_item_properties
                        List<DefaultChargingItemProperty> properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.APARTMENT.getCode(), address.getId(), meter.getMeterType());
                        if(properties != null) {
                            //suanqian paymentExpectancies_re_struct();
                            generateFlag = true;
                        } else {
                            properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.BUILDING.getCode(), address.getBuildingId(), meter.getMeterType());
                            if(properties != null) {
                                //suanqian paymentExpectancies_re_struct();
                                generateFlag = true;
                            } else {
                                properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.BUILDING.getCode(), meter.getCommunityId(), meter.getMeterType());
                                if(properties != null) {
                                    //suanqian paymentExpectancies_re_struct();
                                    generateFlag = true;
                                }
                            }
                        }
                    }

                }
                if(generateFlag) {
                    task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.GENERATED.getCode());
                } else {
                    task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.NON_CHARGING_ITEM.getCode());
                }
                taskProvider.updateEnergyMeterTask(task);
            });
        }

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
