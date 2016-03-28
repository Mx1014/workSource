// @formatter:off
package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>departmentId：部门id</li>
 * <li>organizationId：政府机构id</li>
 * </ul>
 */
public class ListPersonnelNotJoinGroupCommand {
	
	private Long organizationId;
	
	private Long departmentId;
	


	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public Long getDepartmentId() {
		return departmentId;
	}



	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
