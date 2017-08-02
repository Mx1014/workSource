package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>moduleId：业务模块id</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否，{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>allProjectFlag：是否全部项目 1：是 0：否 ,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>targets: 分配的对象集，参考{@link AssignmentTarget}</li>
 * <li>privileges: 权限集合，{@link PrivilegeDTO}</li>
 * <li>projects: 分配的项目范围集合，参考{@link Project}</li>
 * </ul>
 */
public class CreateAuthorizationRelationCommand {

	@NotNull
	private String ownerType;

	@NotNull
	private Long ownerId;

	private Long moduleId;

	private Byte allFlag;

	private Byte allProjectFlag;

	@ItemType(AssignmentTarget.class)
	private List<AssignmentTarget> targets;

	@ItemType(Long.class)
	private List<Long> privilegeIds;

	@ItemType(Project.class)
	private List<Project> projects;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public List<Long> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<Long> privilegeIds) {
		this.privilegeIds = privilegeIds;
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
