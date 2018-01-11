package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by rui.jia  2018/1/11 11 :32
 */

public class OfflineEquipmentTaskReportCommand {

    @ItemType(ReportEquipmentTaskCommand.class)
    private List<ReportEquipmentTaskCommand> reportEquipmentTaskCommands;


    public List<ReportEquipmentTaskCommand> getReportEquipmentTaskCommands() {
        return reportEquipmentTaskCommands;
    }

    public void setReportEquipmentTaskCommands(List<ReportEquipmentTaskCommand> reportEquipmentTaskCommands) {
        this.reportEquipmentTaskCommands = reportEquipmentTaskCommands;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
