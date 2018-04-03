// @formatter:off
package com.everhomes.rest.openapi;

/**
 * 
 * <ul>
 * <li>userId: 用户id</li>
 * <li>groupName: 组名称</li>
 * </ul>
 */
public class CreateBusinessGroupCommand {
	private Long userId;
	private String groupName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
