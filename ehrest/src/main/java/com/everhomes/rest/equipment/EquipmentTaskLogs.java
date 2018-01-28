package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *  <li>logs: 任务操作记录列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskLogsDTO}</li>
 *  <li>taskType: 任务类型</li>
 *  <li>taskId: 任务id</li>
 *  <li>executeStartTime: 任务开始时间</li>
 *  <li>executeEndTime: 任务结束时间</li>
 * </ul>
 */

public class EquipmentTaskLogs {
    @ItemType(EquipmentTaskLogsDTO.class)
    private List<EquipmentTaskLogsDTO> logs;

    private Byte taskType;

    private Long taskId;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
