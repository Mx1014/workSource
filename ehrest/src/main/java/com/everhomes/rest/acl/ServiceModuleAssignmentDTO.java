package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>web菜单</p>
 * <ul>
 * <li>moduleId: 模块id</li>
 * <li>assignmentType: 分配类型 0全部 1部分</li>
 * <li>privileges: 权限集</li>
 * </ul>
 */
public class ServiceModuleAssignmentDTO implements Serializable {

	private Long moduleId;

	private String moduleName;

	private Byte assignmentType;

	@ItemType(PrivilegeDTO.class)
	private List<PrivilegeDTO> privileges;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Byte getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(Byte assignmentType) {
		this.assignmentType = assignmentType;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<PrivilegeDTO> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<PrivilegeDTO> privileges) {
		this.privileges = privileges;
	}
}