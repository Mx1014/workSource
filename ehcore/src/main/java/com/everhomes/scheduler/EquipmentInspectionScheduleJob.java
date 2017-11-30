package com.everhomes.scheduler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.equipment.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.*;

@Component
@Scope("prototype")
public class EquipmentInspectionScheduleJob extends QuartzJobBean {
	
private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInspectionScheduleJob.class);

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private RepeatService repeatService;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ScheduleProvider scheduleProvider;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}

		//双机判断
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			//为防止时间长了的话可能会有内存溢出的可能，把每天过期的定时任务清理一下
			//scheduleProvider.unscheduleJob("EquipmentInspectionNotify ");
			closeDelayTasks();
			//createTask();
			createTaskByPlan();
		}

	}

	private void createTaskByPlan() {
		if (LOGGER.isInfoEnabled()){
			LOGGER.info("EquipmentInspectionScheduleJob:createTask.....");
		}
		int pageSize=200;
		ListingLocator locator =new ListingLocator();
		while (locator.getAnchor()!=null){
			List<EquipmentInspectionPlans> plans = equipmentProvider.ListQualifiedEquipmentInspectionPlans(locator, pageSize);
			for (EquipmentInspectionPlans plan: plans){
				if(checkPlanRepeat(plan)){

				}

			}
		}
	}

	private boolean checkPlanRepeat(EquipmentInspectionPlans plan) {
		boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatsettingId());
		LOGGER.info("checkPlanRepeat: plans  id = " + plan.getId()
				+ "repeat setting id = "+ plan.getRepeatsettingId() + "is repeat setting active: " + isRepeat);

		return isRepeat;
	}

	private void createTask() {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob: createTask");
		}
		List<EquipmentStandardMap> maps = equipmentProvider.listQualifiedEquipmentStandardMap(null);


		Map<Long, Set<EquipmentStandardMap>> standardEquipmentMap = new HashMap<>();

		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				Set<EquipmentStandardMap> equipmentStandardMaps = standardEquipmentMap.get(map.getStandardId());
				if(equipmentStandardMaps == null) {
					equipmentStandardMaps = new HashSet<EquipmentStandardMap>();
				}
				equipmentStandardMaps.add(map);

				standardEquipmentMap.put(map.getStandardId(), equipmentStandardMaps);
			}
		}


		for (Map.Entry<Long, Set<EquipmentStandardMap>> entry : standardEquipmentMap.entrySet()) {
			EquipmentInspectionStandards standard = equipmentProvider.findStandardById(entry.getKey());
			if(checkStandard(standard)) {
				Set<EquipmentStandardMap> equipmentStandardMaps = entry.getValue();
				if(equipmentStandardMaps != null && equipmentStandardMaps.size() > 0) {
					for(EquipmentStandardMap equipmentStandardMap : equipmentStandardMaps) {

						dbProvider.execute((TransactionStatus status) -> {
							if(checkTaskAlreadyCreated(equipmentStandardMap)) {
								EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(equipmentStandardMap.getTargetId());
								if (checkEquipment(equipment)) {
									equipmentService.creatTaskByStandard(equipment, standard);

									equipmentStandardMap.setLastCreateTaskTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
									equipmentProvider.updateEquipmentStandardMap(equipmentStandardMap);
								}

								LOGGER.info("equipmentStandardMap: {} ", equipmentStandardMap);
							}
							return null;
						});

					}

				}

			}

		}
	}


	private boolean checkStandard(EquipmentInspectionStandards standard) {
		if(standard == null || standard.getStatus() == null
						|| !EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
			LOGGER.info("checkStandard: standard is not exist or active! standardId = " + standard.getId());
			return false;
		}

		boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
		LOGGER.info("checkStandard: standard id = " + standard.getId()
				+ "repeat setting id = "+ standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);

		return isRepeat;
	}

	private boolean checkEquipment(EquipmentInspectionEquipments equipment) {
		if(equipment == null || !EquipmentStatus.IN_USE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			LOGGER.info("equipment is not exist or active! equipmentId = " + equipment.getId());
			return false;
		}

		return true;
	}

	private boolean checkTaskAlreadyCreated(EquipmentStandardMap equipmentStandardMap) {
		//只有一台指定的服务器会生成任务 所以不需要再做判断了 
//		if(equipmentStandardMap.getLastCreateTaskTime() != null) {
//			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
//			Long hours = (now.getTime() - equipmentStandardMap.getLastCreateTaskTime().getTime())/(1000*60*60);
//
//			LOGGER.info("checkTaskAlreadyCreated: current time = " + now + ", equipmentStandardMap id: " +equipmentStandardMap.getId()
//					+ ", LastCreateTaskTime  = "+ equipmentStandardMap.getLastCreateTaskTime() + ", interval hours: " + hours);
//			// 2小时之内已生成过任务则不再生成
//			if(hours < 2L) {
//				return false;
//			}
//		}
		return true;
	}
	private void closeDelayTasks() {
		LOGGER.info("EquipmentInspectionScheduleJob: close delay tasks.");
		equipmentProvider.closeDelayTasks();
		
		LOGGER.info("EquipmentInspectionScheduleJob: close expired review tasks.");
		equipmentProvider.closeExpiredReviewTasks();
	}


}
