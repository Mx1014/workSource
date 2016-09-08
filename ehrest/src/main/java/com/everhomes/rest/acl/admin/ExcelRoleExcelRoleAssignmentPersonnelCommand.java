package com.everhomes.rest.acl.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roleId: 角色id</li>
 * <li>organizationId: 机构id </li>
 * </ul>
 */
public class ExcelRoleExcelRoleAssignmentPersonnelCommand {
	
	@NotNull
	private Long organizationId;
	
	@NotNull
	private Long roleId;
	
	private String keywords;
    
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
