// @formatter:off
package com.everhomes.rest.organization;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子Id</li>
 * <li>status: 任务状态，参考{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * <li>organizationId: 组织Id</li>
 * </ul>
 */
public class SetOrgTopicStatusCommand {
	@NotNull
	private Long topicId;
	@NotNull
    private Byte status;
	@NotNull
    private Long organizationId;
    
    public SetOrgTopicStatusCommand() {
    }
    
    public Long getTopicId() {
		return topicId;
	}


	public void setTopicId(Long topicId) {
		this.topicId = topicId; 
	}


	public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
