package com.everhomes.rest.pmtask;

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
public class DeleteTaskOperatePersonCommand {
	private String ownerType;
	private Long ownerId;
	private Long organizationId;
	private Byte operateType;
	private String targetType;
	private Long targetId;
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
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
