// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>groupId: 圈id</li>
 * <li>userId: 转移给谁</li>
 * </ul>
 */
public class TransferCreatorPrivilegeCommand {

	private Long groupId;
	
	private Long userId;

	public TransferCreatorPrivilegeCommand() {

	}

	public TransferCreatorPrivilegeCommand(Long groupId) {
		super();
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
