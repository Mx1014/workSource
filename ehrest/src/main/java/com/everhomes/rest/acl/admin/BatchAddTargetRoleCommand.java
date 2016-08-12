package com.everhomes.rest.acl.admin;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id </li>
 * <li>targetType: 对象类型 参考{@link com.everhomes.entity.EntityType} </li>
 * <li>targetId: 对象id </li>
 * <li>roleIds: 多个角色 </li>
 * </ul>
 */
public class BatchAddTargetRoleCommand {
	
	private Long organizationId;
	
	@ItemType(Long.class)
	private List<Long> roleIds;
	
	private String targetType;
	
	private Long targetId;
    
    
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
