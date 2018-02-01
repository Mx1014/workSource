package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>taskId: 任务id</li>
 * </ul>
 */
public class GetImportSocialSecurityPaymentsResultCommand {

    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
