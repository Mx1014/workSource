//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerId:所属者ID</li>
 * <li>ownerType:所属者类型</li>
 * <li>organizationId:当前登录企业ID（主要用于做权限校验）</li>
 * <li>billIdList:账单id列表</li>
 *</ul>
 */
public class BatchDeleteBillCommand {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long organizationId;
    private List<Long> billIdList;
    
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
	public List<Long> getBillIdList() {
		return billIdList;
	}
	public void setBillIdList(List<Long> billIdList) {
		this.billIdList = billIdList;
	}

}
