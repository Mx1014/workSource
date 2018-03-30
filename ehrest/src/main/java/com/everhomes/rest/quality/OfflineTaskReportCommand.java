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
    @ItemType(OfflineQualityTaskCommand.class)
    private List<OfflineQualityTaskCommand> tasks;

    @ItemType(OfflineReportDetailDTO.class)
    private List<OfflineReportDetailDTO> offlineReportDetail;


    public List<OfflineQualityTaskCommand> getTasks() {
        return tasks;
    }

    public void setTasks(List<OfflineQualityTaskCommand> tasks) {
        this.tasks = tasks;
    }

    public List<OfflineReportDetailDTO> getOfflineReportDetail() {
        return offlineReportDetail;
    }

    public void setOfflineReportDetail(List<OfflineReportDetailDTO> offlineReportDetail) {
        this.offlineReportDetail = offlineReportDetail;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
