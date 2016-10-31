package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>operateType: 人员类型</li>
 * <li>organizationId: 机构id</li>
 * <li>targetType: 类型 {@link com.everhomes.entity.EntityType  EhUsers }</li>
 * <li>targetId: id</li>
 * </ul>
 */
public class CreateTaskOperatePersonCommand {
	private String ownerType;
	private Long ownerId;
	private Byte operateType;
	private Long organizationId;
	
//	@ItemType(String.class)
//	private List<String> targetTypes;
	@ItemType(Long.class)
	private List<Long> targetIds;
	
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Byte getOperateType() {
		return operateType;
	}
	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
	}
	
//	public List<String> getTargetTypes() {
//		return targetTypes;
//	}
//	public void setTargetTypes(List<String> targetTypes) {
//		this.targetTypes = targetTypes;
//	}
	public List<Long> getTargetIds() {
		return targetIds;
	}
	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
