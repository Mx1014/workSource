package com.everhomes.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>departmentName: 部门名称</li>
 *  <li>superiorDepartment: 上级部门名称</li>
 *  <li>departmentType: 部门类型</li>
 * </ul>
 *
 */
public class DepartmentDTO {
	
	private Long id;
	
	private String departmentName;
	
	private String superiorDepartment;
	
	private String departmentType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getSuperiorDepartment() {
		return superiorDepartment;
	}

	public void setSuperiorDepartment(String superiorDepartment) {
		this.superiorDepartment = superiorDepartment;
	}

	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
