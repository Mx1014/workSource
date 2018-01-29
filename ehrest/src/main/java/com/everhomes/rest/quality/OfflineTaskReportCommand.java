package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>tasks:任务列表</li>
 * <li>offlineReportDetail:上报详情</li>
 * </ul>
 */

public class OfflineTaskReportCommand {
    @ItemType(QualityInspectionTaskDTO.class)
    private List<QualityInspectionTaskDTO> tasks;

    @ItemType(offlineReportDetailDTO.class)
    private List<offlineReportDetailDTO> offlineReportDetail;


    public List<QualityInspectionTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<QualityInspectionTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<offlineReportDetailDTO> getOfflineReportDetail() {
        return offlineReportDetail;
    }

    public void setOfflineReportDetail(List<offlineReportDetailDTO> offlineReportDetail) {
        this.offlineReportDetail = offlineReportDetail;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
