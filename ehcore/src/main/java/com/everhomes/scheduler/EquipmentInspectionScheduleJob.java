package com.everhomes.scheduler;

import java.sql.Timestamp;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.equipment.EquipmentInspectionEquipments;
import com.everhomes.equipment.EquipmentInspectionStandards;
import com.everhomes.equipment.EquipmentProvider;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.equipment.EquipmentStandardMap;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.util.DateHelper;

@Component
public class EquipmentInspectionScheduleJob extends QuartzJobBean {
	
private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInspectionScheduleJob.class);
	
	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private RepeatService repeatService;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		
		List<EquipmentStandardMap> maps = equipmentProvider.listQualifiedEquipmentStandardMap(InspectionStandardMapTargetType.EQUIPMENT.getCode());
		
//		if(equipments != null && equipments.size() > 0) {
//			for(EquipmentInspectionEquipments equipment : equipments) {
//				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId());
//				if(standard == null || standard.getStatus() == null
//						|| EquipmentStandardStatus.fromStatus(standard.getStatus()) != EquipmentStandardStatus.ACTIVE) {
//					LOGGER.info("EquipmentInspectionScheduleJob standard is not exist or active! standardId = " + equipment.getStandardId());
//					continue;
//				} else {
//					boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
//					LOGGER.info("EquipmentInspectionScheduleJob: standard id = " + standard.getId() 
//							+ "repeat setting id = "+ standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
//					if(isRepeat) {
//						this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(()-> {
//							equipmentService.creatTaskByStandard(equipment, standard);
//						});
//					}
//				}
//			}
//		}
		
		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
				if(standard == null || standard.getStatus() == null
						|| !EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
					LOGGER.info("EquipmentInspectionScheduleJob standard is not exist or active! standardId = " + map.getStandardId());
					continue;
				} else if(equipment == null || !EquipmentStatus.IN_USE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
						LOGGER.info("EquipmentInspectionScheduleJob equipment is not exist or active! equipmentId = " + map.getTargetId());
						continue;
					
				} else {
					boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
					LOGGER.info("EquipmentInspectionScheduleJob: standard id = " + standard.getId() 
							+ "repeat setting id = "+ standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
					if(isRepeat) {
						this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(()-> {
							equipmentService.creatTaskByStandard(equipment, standard);
						});
					}
				}
			}
		}
	}

}
