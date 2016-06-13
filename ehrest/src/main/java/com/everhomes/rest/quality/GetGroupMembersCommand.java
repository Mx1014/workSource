package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * groupId: 业务组id
 *
 */
public class GetGroupMembersCommand {
	
	@NotNull
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
