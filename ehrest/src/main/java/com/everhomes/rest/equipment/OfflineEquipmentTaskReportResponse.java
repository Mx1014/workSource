package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by rui.jia  2018/1/11 12 :18
 */

public class OfflineEquipmentTaskReportResponse {
    @ItemType(OfflineEquipmentTaskReportLog.class)
    List<OfflineEquipmentTaskReportLog> logs;

    public List<OfflineEquipmentTaskReportLog> getLogs() {
        return logs;
    }

    public void setLogs(List<OfflineEquipmentTaskReportLog> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
