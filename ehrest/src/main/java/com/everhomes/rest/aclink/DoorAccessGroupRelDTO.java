// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: 门禁组ID </li>
 * <li>groupName: 门禁组名字</li>
 * <li>doors: 门禁 {@link com.everhomes.rest.aclink.DoorAccessLiteDTO}</li>
 * </ul>
 *
 */
public class DoorAccessGroupRelDTO {
	private Long groupId;
	private String groupName;
	@ItemType(DoorAccessLiteDTO.class)
	private List<DoorAccessLiteDTO> doors;
	
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
	public List<DoorAccessLiteDTO> getDoors() {
		return doors;
	}
	public void setDoors(List<DoorAccessLiteDTO> doors) {
		this.doors = doors;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
