// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId:门禁组id 必填</li>
 * <li>groupName:门禁组名称</li>
 * <li>status:删除时传0</li>
 * </ul>
 */
public class UpdateDoorAccessGroupCommand {
    @NotNull
	private Long groupId;
	private String groupName;
//	private List<Long> doorIds;
	private Byte status;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
//	public List<Long> getDoorIds() {
//		return doorIds;
//	}
//	public void setDoorIds(List<Long> doorIds) {
//		this.doorIds = doorIds;
//	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
