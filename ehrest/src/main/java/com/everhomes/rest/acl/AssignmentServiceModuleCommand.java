package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.AuthorizationServiceModule;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * <li>allModuleFlag: 是否全部模块,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>targets: 分配的对象集，参考{@link com.everhomes.rest.acl.AssignmentTarget}</li>
 * <li>moduleIds: 分配的业务模块ID集合</li>
 * <li>projects: 分配的项目范围集合，参考{@link com.everhomes.rest.acl.ProjectDTO}</li>
 * </ul>
 */
public class AssignmentServiceModuleCommand {

	private String ownerType;

	private Long ownerId;

	private Byte allModuleFlag;

	@ItemType(AssignmentTarget.class)
	private List<AssignmentTarget> targets;

	@ItemType(Long.class)
	private List<Long> moduleIds;

//	@ItemType(ProjectDTO.class)
//	private List<ProjectDTO> projects;

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

	public Byte getAllModuleFlag() {
		return allModuleFlag;
	}

	public void setAllModuleFlag(Byte allModuleFlag) {
		this.allModuleFlag = allModuleFlag;
	}

	public List<AssignmentTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<AssignmentTarget> targets) {
		this.targets = targets;
	}

	public List<Long> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Long> moduleIds) {
		this.moduleIds = moduleIds;
	}

//	public List<ProjectDTO> getProjects() {
//		return projects;
//	}
//
//	public void setProjects(List<ProjectDTO> projects) {
//		this.projects = projects;
//	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
