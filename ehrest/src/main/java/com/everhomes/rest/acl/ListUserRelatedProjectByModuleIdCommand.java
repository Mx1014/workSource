package com.everhomes.rest.acl;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListUserRelatedProjectByModuleIdCommand {

    private String ownerType;


    private Long ownerId;

    @NotNull
    private Long organizationId;

    @NotNull
    private Long moduleId;

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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
