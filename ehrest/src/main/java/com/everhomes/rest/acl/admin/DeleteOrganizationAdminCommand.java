package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>userId: 用户Id</li>
 * </ul>
 */
public class DeleteOrganizationAdminCommand {
	
	private Long organizationId;
	
	private Long userId;
	

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



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
