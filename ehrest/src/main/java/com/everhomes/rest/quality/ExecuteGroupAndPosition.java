package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>groupId: 部门id</li>
 *  <li>positionId: 岗位id</li>
 * </ul>
 *
 */
public class ExecuteGroupAndPosition {

	private Long groupId;
	
	private Long positionId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
