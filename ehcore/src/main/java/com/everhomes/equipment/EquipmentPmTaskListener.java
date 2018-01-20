package com.everhomes.equipment;

import com.everhomes.pmtask.PmTask;
import com.everhomes.pmtask.PmTaskListener;
import com.everhomes.rest.equipment.EquipmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rui.jia  2018/1/15 15 :21
 */
@Component(PmTaskListener.PMTASK_PREFIX + EquipmentConstant.EQUIPMENT_REPAIR)
public class EquipmentPmTaskListener implements PmTaskListener {
    @Autowired
    private EquipmentProvider equipmentProvider;
    @Override
    public void onTaskSuccess(PmTask pmTask, Long referId) {
        //更新巡检log状态referId为设备id
        EquipmentInspectionTasksLogs log = equipmentProvider.getMaintanceLogByEquipmentId(referId);
        equipmentProvider.updateMaintanceInspectionLogsById(log.getId(),pmTask.getFlowCaseId());
        //设备状态
        equipmentProvider.updateEquipmentStatus(referId,EquipmentStatus.IN_USE.getCode());

    }
}
