package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 园区id</li>
 * <li>namespaceId: 域空间</li>
 * <li>dataStr: 日期</li>
 * <li>currentOrgId: 当前机构id</li>
 * </ul>
 */
public class GetStatisticsCommand {
	private Long ownerId;
	private String ownerType;
	private Integer namespaceId;
	private Long dateStr;
	private  Long currentOrgId;

	public Long getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getDateStr() {
		return dateStr;
	}
	public void setDateStr(Long dateStr) {
		this.dateStr = dateStr;
	}
	
}
