package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>userId: 用户Id</li>
 * <li>moduleId: 模块Id</li>
 * </ul>
 */
public class DeleteServiceModuleAdministratorsCommand {


	@NotNull
	private String ownerType;

	@NotNull
	private Long ownerId;

	@NotNull
	private Long organizationId;

	@NotNull
	private Long userId;

	@NotNull
	private Long moduleId;
	

	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
