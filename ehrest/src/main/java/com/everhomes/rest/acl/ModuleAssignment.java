package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>web菜单</p>
 * <ul>
 * <li>moduleId: 模块id</li>
 * <li>assignmentType: 分配类型 0全部 1部分</li>
 * <li>privilegeIds: 权限集</li>
 * </ul>
 */
public class ModuleAssignment implements Serializable {

	private Long moduleId;

	private Byte assignmentType;

	@ItemType(Long.class)
	private List<Long> privilegeIds;

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

	public List<Long> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<Long> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}
}