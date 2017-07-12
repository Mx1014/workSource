package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>organizationId: 机构ID</li>
 * <li>operateType: 类型</li>
 * <li>pageAnchor: 瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class ListOperatePersonnelsCommand {
	private String ownerType;
	private Long ownerId;
	private Long organizationId;
	private Byte operateType;
	private Long pageAnchor;
	private Integer pageSize;
	
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
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Byte getOperateType() {
		return operateType;
	}
	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
	}
	
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
