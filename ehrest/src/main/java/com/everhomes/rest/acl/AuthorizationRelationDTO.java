// @formatter:off
package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>allFlag：是否业务模块的全部权限 1：是 0：否 ,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>allProjectFlag：是否全部项目 1：是 0：否 ,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>targets: 分配的对象集，参考{@link AssignmentTarget}</li>
 * <li>privileges: 业务模块信息，{@link PrivilegeDTO}</li>
 * <li>projects: 分配的项目范围集合，参考{@link Project}</li>
 * </ul>
 */
public class AuthorizationRelationDTO {
	private Long id;

	private Byte allFlag;

	private Byte allProjectFlag;

	@ItemType(AssignmentTarget.class)
	private List<AssignmentTarget> targets;

	@ItemType(PrivilegeDTO.class)
	private List<PrivilegeDTO> privileges;

	@ItemType(Project.class)
	private List<Project> projects;

	public AuthorizationRelationDTO() {
    }

	public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
	}

	public List<AssignmentTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<AssignmentTarget> targets) {
		this.targets = targets;
	}

	public List<PrivilegeDTO> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<PrivilegeDTO> privileges) {
		this.privileges = privileges;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Byte getAllProjectFlag() {
		return allProjectFlag;
	}

	public void setAllProjectFlag(Byte allProjectFlag) {
		this.allProjectFlag = allProjectFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
