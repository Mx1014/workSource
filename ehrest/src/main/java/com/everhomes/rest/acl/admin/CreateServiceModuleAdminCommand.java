package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>contactToken: 手机号</li>
 * <li>contactName:  用户姓名</li>
 * <li>moduleId:  业务模块id</li>
 * </ul>
 */
public class CreateServiceModuleAdminCommand {

	@NotNull
	private Long organizationId;

	@NotNull
	private String contactToken;
	
	private String contactName;

	@NotNull
	private Long moduleId;
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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
