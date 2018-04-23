package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/3/29 15 :38
 */

public class ExportTaskLogsCommand {

    private String taskId;

    private Byte processType;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Byte getProcessType() {
        return processType;
    }

    public void setProcessType(Byte processType) {
        this.processType = processType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
