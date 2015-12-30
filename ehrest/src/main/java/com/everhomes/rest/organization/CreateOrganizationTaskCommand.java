// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>description: 任务描述</li>
 * <li>taskStatus: 任务状态，参考{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * </ul>
 */
public class CreateOrganizationTaskCommand {
    private String description;
    private Byte taskStatus;
    
	public CreateOrganizationTaskCommand() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Byte taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
