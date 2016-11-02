package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>userId: 用户Id</li>
 * <li>moduleId: 模块Id</li>
 * </ul>
 */
public class DeleteServiceModuleAdministratorsCommand {
	
	private Long organizationId;
	
	private Long userId;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
