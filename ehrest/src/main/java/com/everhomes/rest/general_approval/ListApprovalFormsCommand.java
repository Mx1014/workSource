package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 目前默认是： EhOrganizations</li>
 * <li>ownerId: 目前是 organizationId </li>
 * <li>moduleId: 模块id</li>
 * <li>moduleType: 模块类型,模块类型 默认"any-module",参考 {@link com.everhomes.rest.flow.FlowModuleType} </li>
 * </ul>
 * @author janson
 *
 */
public class ListApprovalFormsCommand {
	private Long ownerId;
	private String ownerType;
	private Long moduleId;
	private String moduleType;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
}
