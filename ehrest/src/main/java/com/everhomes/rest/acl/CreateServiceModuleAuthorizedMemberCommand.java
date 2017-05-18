package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 当前机构id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，Eh_Users, Eh_Organizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否</li>
 * <li>moduleId:  模块id</li>
 * <li>privilegeIds:  业务模块id集合</li>
 * </ul>
 */
public class CreateServiceModuleAuthorizedMemberCommand {

	private Long organizationId;

	private String ownerType;

	private Long ownerId;

	private Byte allFlag;

	@NotNull
	private Long moduleId;

	@ItemType(AssignmentTarget.class)
	private List<AssignmentTarget> targets;

	@ItemType(Project.class)
	private List<Project> projects;

	@NotNull
	@ItemType(Long.class)
	private List<Long> privilegeIds;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public List<Long> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<Long> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public List<AssignmentTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<AssignmentTarget> targets) {
		this.targets = targets;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
