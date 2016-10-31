// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>groupId: 圈id</li>
 * </ul>
 */
public class TransferCreatorPrivilegeCommand {

	private Long groupId;

	public TransferCreatorPrivilegeCommand() {

	}

	public TransferCreatorPrivilegeCommand(Long groupId) {
		super();
		this.groupId = groupId;
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
