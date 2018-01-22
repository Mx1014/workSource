package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by rui.jia  2018/1/11 12 :19
 */

public class OfflineEquipmentTaskReportLog {

    private Long taskId;

    private Integer errorCode;

    private String errorDescription;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
