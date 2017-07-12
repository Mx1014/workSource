// @formatter:off
package com.everhomes.rest.openapi;

/**
 * 
 * <ul>
 * <li>userId: 用户id</li>
 * <li>groupId: 组id</li>
 * </ul>
 */
public class JoinBusinessGroupCommand {
	private Long userId;
	private Long groupId;

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

}
