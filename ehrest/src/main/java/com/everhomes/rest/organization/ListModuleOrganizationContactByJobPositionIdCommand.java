// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>moduleId：业务模块</li>
 * <li>organizationId：所在当前机构</li>
 * <li>ownerType：范围类型</li>
 * <li>ownerId：范围id</li>
 * <li>groupTypes : 机构类型 参考{@link OrganizationGroupType} </li>
 * <li>jobPositionId：通用岗位id</li>
 * </ul>
 */
public class ListModuleOrganizationContactByJobPositionIdCommand {

	private Long moduleId;

	private Long organizationId;

	private String ownerType;

	private Long ownerId;

	private Long jobPositionId;

	@ItemType(String.class)
	private List<String> groupTypes;

	public ListModuleOrganizationContactByJobPositionIdCommand() {
    }

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public List<String> getGroupTypes() {
		return groupTypes;
	}

	public void setGroupTypes(List<String> groupTypes) {
		this.groupTypes = groupTypes;
	}

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

	public Long getJobPositionId() {
		return jobPositionId;
	}

	public void setJobPositionId(Long jobPositionId) {
		this.jobPositionId = jobPositionId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
