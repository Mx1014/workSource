package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by rui.jia  2018/1/11 12 :18
 */

public class OfflineEquipmentTaskReportResponse {
    @ItemType(OfflineEquipmentTaskReportLog.class)
    private List<OfflineEquipmentTaskReportLog> taskLogs;

    @ItemType(OfflineEquipmentTaskReportLog.class)
    private List<OfflineEquipmentTaskReportLog> repairLogs;

    public List<OfflineEquipmentTaskReportLog> getTaskLogs() {
        return taskLogs;
    }

    public void setTaskLogs(List<OfflineEquipmentTaskReportLog> taskLogs) {
        this.taskLogs = taskLogs;
    }

    public List<OfflineEquipmentTaskReportLog> getRepairLogs() {
        return repairLogs;
    }

    public void setRepairLogs(List<OfflineEquipmentTaskReportLog> repairLogs) {
        this.repairLogs = repairLogs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
