// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>groupId: 门禁组ID </li>
 * <li>groupName: 门禁组名字</li>
 * <li>doors: 门禁 {@link DoorAccessLiteDTO}</li>
 * </ul>
 *
 */
public class AclinkGroupDTO {
	private Long groupId;
	private String groupName;
	private Integer count;
	private Timestamp createTime;
	private Byte status;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

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
