package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>resourceType: 资源类型</li>
 * <li>resourceId: 资源Id</li>
 * <li>organizationId: 机构Id</li>
 * </ul>
 */
public class DeleteAuthorizationServiceModuleCommand {
	
	private String resourceType;
	
	private Long resourceId;

	private Long organizationId;
	

	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
