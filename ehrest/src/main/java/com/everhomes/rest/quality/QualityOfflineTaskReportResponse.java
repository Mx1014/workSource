package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportLog;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by rui.jia  2018/1/29 15 :29
 */

public class QualityOfflineTaskReportResponse {
    @ItemType(OfflineEquipmentTaskReportLog.class)
    private List<OfflineEquipmentTaskReportLog> taskReportLogs;

    public List<OfflineEquipmentTaskReportLog> getTaskReportLogs() {
        return taskReportLogs;
    }

    public void setTaskReportLogs(List<OfflineEquipmentTaskReportLog> taskReportLogs) {
        this.taskReportLogs = taskReportLogs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
