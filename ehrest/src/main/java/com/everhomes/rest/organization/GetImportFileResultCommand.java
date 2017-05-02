package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>taskId: 任务id</li>
 * </ul>
 */
public class GetImportFileResultCommand {

    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
