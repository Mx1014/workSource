//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>orgId: 管理公司id</li>
 * <li>paramsStatus:设置参数的状态</li>
 * <li>arrearageDays:欠费天数</li>
 * <li>dooraccessList:所选择禁用门禁的列表</li>
 * <li>categoryId: 缴费应用id</li>
 *</ul>
 */
public class SetDoorAccessParamCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    private Long orgId;
    private Byte paramsStatus;
    private Long arrearageDays;
    private String dooraccessList;
    private Long categoryId;
    
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

	public Byte getParamsStatus() {
		return paramsStatus;
	}

	public void setParamsStatus(Byte paramsStatus) {
		this.paramsStatus = paramsStatus;
	}

	public Long getArrearageDays() {
		return arrearageDays;
	}

	public void setArrearageDays(Long arrearageDays) {
		this.arrearageDays = arrearageDays;
	}

	public String getDooraccessList() {
		return dooraccessList;
	}

	public void setDooraccessList(String dooraccessList) {
		this.dooraccessList = dooraccessList;
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
