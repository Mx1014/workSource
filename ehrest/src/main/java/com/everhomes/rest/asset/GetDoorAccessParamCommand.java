//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>orgId: 管理公司id</li>
 * <li>categoryId: 缴费应用id</li>
 *</ul>
 */
public class GetDoorAccessParamCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long orgId;
    private Long categoryId;
    private Long appId;
    
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
