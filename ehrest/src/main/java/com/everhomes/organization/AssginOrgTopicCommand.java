// @formatter:off
package com.everhomes.organization;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId : 组织id</li>
 * <li>topicId: 帖子ID</li>
 * <li>userId: 接收任务的用户Id</li>
 * </ul>
 */
public class AssginOrgTopicCommand {
	@NotNull
	private Long organizationId;
	@NotNull
	private Long topicId;
	@NotNull
    private Long userId;
    
    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public AssginOrgTopicCommand() {
    }

    public Long getTopicId() {
		return topicId;
	}
    

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
