package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *  <li>logs: 任务操作记录列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskLogsDTO}</li>
 *  <li>taskType: 任务类型</li>
 *  <li>taskNumber: taskNumber</li>
 *  <li>taskName: 任务name</li>
 *  <li>executeStartTime: 任务开始时间</li>
 *  <li>executeEndTime: 任务结束时间</li>
 * </ul>
 */

public class EquipmentTaskLogs {
    @ItemType(EquipmentTaskLogsDTO.class)
    private List<EquipmentTaskLogsDTO> logs;

    private Byte taskType;

    private String  taskNumber;

    private String taskName;

    private Timestamp executeStartTime;

    private Timestamp executeEndTime;

    public List<EquipmentTaskLogsDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<EquipmentTaskLogsDTO> logs) {
        this.logs = logs;
    }

    public Byte getTaskType() {
        return taskType;
    }

    public void setTaskType(Byte taskType) {
        this.taskType = taskType;
    }

    public Timestamp getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(Timestamp executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public Timestamp getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(Timestamp executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
