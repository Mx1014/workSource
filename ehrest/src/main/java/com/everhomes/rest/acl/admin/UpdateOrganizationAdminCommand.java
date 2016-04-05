package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>userId: 用户Id</li>
 * <li>contactName: 姓名</li>
 * </ul>
 */
public class UpdateOrganizationAdminCommand {
	
	private Long organizationId;
	
	private Long userId;
	
	private String contactName;
	
	

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



	public String getContactName() {
		return contactName;
	}



	public void setContactName(String contactName) {
		this.contactName = contactName;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
