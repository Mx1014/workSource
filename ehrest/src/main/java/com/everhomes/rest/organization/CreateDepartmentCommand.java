package com.everhomes.rest.organization;


/**
 * <ul>
 * <li>parentId：父机构id。没有填科技园的organizationid</li>
 * <li>departmentName：名称</li>
 * <li>departmentType：部门组织类型：参考{@link com.everhomes.rest.organization.DepartmentType}</li>
 * <li>roleId：角色id</li>
 * </ul>
 */
public class CreateDepartmentCommand {

	private String departmentName;
	
	private Long parentId;
	
	private String departmentType;
	
	private Long roleId;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
